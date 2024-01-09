/*
 * 文 件 名:  GaussBaseDynamicTableSource.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2022/4/25
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.gaussdb.table;

import org.apache.flink.connector.jdbc.dialect.JdbcDialect;
import org.apache.flink.connector.jdbc.internal.options.JdbcConnectorOptions;
import org.apache.flink.connector.jdbc.internal.options.JdbcLookupOptions;
import org.apache.flink.connector.jdbc.internal.options.JdbcReadOptions;
import org.apache.flink.connector.jdbc.split.JdbcNumericBetweenParametersProvider;
import org.apache.flink.connector.jdbc.table.JdbcDynamicTableSource;
import org.apache.flink.connector.jdbc.table.JdbcRowDataInputFormat;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.Projection;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.connector.source.InputFormatProvider;
import org.apache.flink.table.connector.source.LookupTableSource;
import org.apache.flink.table.connector.source.ScanTableSource;
import org.apache.flink.table.connector.source.TableFunctionProvider;
import org.apache.flink.table.connector.source.abilities.SupportsLimitPushDown;
import org.apache.flink.table.connector.source.abilities.SupportsProjectionPushDown;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.logical.RowType;
import org.apache.flink.util.Preconditions;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2022/4/25]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class GaussBaseDynamicTableSource
        implements ScanTableSource, LookupTableSource, SupportsProjectionPushDown, SupportsLimitPushDown {
    private static final boolean SUPPORTS_NESTED_PROJECTION = false;

    /**
     * jdbc connector options
     */
    private JdbcConnectorOptions options;

    /**
     * read options
     */
    private JdbcReadOptions readOptions;

    /**
     * lookup options
     */
    private JdbcLookupOptions lookupOptions;

    /**
     * table schema
     */
    private DataType physicalRowDataType;

    /**
     * dialect
     */
    private JdbcDialect dialect;

    private long limit = -1;

    public GaussBaseDynamicTableSource(JdbcConnectorOptions options, JdbcReadOptions readOptions,
            JdbcLookupOptions lookupOptions, DataType physicalRowDataType) {
        this.options = options;
        this.readOptions = readOptions;
        this.lookupOptions = lookupOptions;
        this.physicalRowDataType = physicalRowDataType;
        this.dialect = options.getDialect();
    }

    public JdbcConnectorOptions getOptions() {
        return options;
    }

    public void setOptions(JdbcConnectorOptions options) {
        this.options = options;
    }

    public JdbcReadOptions getReadOptions() {
        return readOptions;
    }

    public void setReadOptions(JdbcReadOptions readOptions) {
        this.readOptions = readOptions;
    }

    public JdbcLookupOptions getLookupOptions() {
        return lookupOptions;
    }

    public void setLookupOptions(JdbcLookupOptions lookupOptions) {
        this.lookupOptions = lookupOptions;
    }

    public DataType getPhysicalRowDataType() {
        return physicalRowDataType;
    }

    public void setPhysicalRowDataType(DataType physicalRowDataType) {
        this.physicalRowDataType = physicalRowDataType;
    }

    public JdbcDialect getDialect() {
        return dialect;
    }

    public void setDialect(JdbcDialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public LookupRuntimeProvider getLookupRuntimeProvider(LookupContext context) {
        String[] keyNames = getKeyNames(context, physicalRowDataType);
        final RowType rowType = (RowType) physicalRowDataType.getLogicalType();
        return TableFunctionProvider.of(new GaussBaseRowDataLookupFunction(options, lookupOptions,
                DataType.getFieldNames(physicalRowDataType).toArray(new String[0]),
                DataType.getFieldDataTypes(physicalRowDataType).toArray(new DataType[0]), keyNames, rowType, dialect));
    }

    @Override
    public ScanRuntimeProvider getScanRuntimeProvider(ScanContext runtimeProviderContext) {
        return InputFormatProvider.of(getJdbcRowDataInputFormat(runtimeProviderContext, options, readOptions));
    }

    @Override
    public ChangelogMode getChangelogMode() {
        return ChangelogMode.insertOnly();
    }

    @Override
    public boolean supportsNestedProjection() {
        return SUPPORTS_NESTED_PROJECTION;
    }

    @Override
    public void applyProjection(int[][] projectedFields) {
        this.physicalRowDataType = Projection.of(projectedFields).project(physicalRowDataType);
    }

    @Override
    public void applyProjection(int[][] projectedFields, DataType producedDataType) {
        SupportsProjectionPushDown.super.applyProjection(projectedFields, producedDataType);
    }

    @Override
    public DynamicTableSource copy() {
        return new JdbcDynamicTableSource(options, readOptions, lookupOptions, physicalRowDataType);
    }

    @Override
    public String asSummaryString() {
        return "JDBC:" + dialect.dialectName();
    }

    /**
     * get jdbcRowDataInputFormat
     *
     * @param runtimeProviderContext runtimeProviderContext
     * @param options options
     * @param readOptions readOptions
     * @return jdbcRowDataInputFormat
     */
    public JdbcRowDataInputFormat getJdbcRowDataInputFormat(ScanTableSource.ScanContext runtimeProviderContext,
            JdbcConnectorOptions options, JdbcReadOptions readOptions) {
        final JdbcRowDataInputFormat.Builder builder = JdbcRowDataInputFormat.builder()
                .setDrivername(options.getDriverName()).setDBUrl(options.getDbURL())
                .setUsername(options.getUsername().orElse(null)).setPassword(options.getPassword().orElse(null))
                .setAutoCommit(readOptions.getAutoCommit());

        if (readOptions.getFetchSize() != 0) {
            builder.setFetchSize(readOptions.getFetchSize());
        }
        String query = dialect.getSelectFromStatement(options.getTableName(),
                DataType.getFieldNames(physicalRowDataType).toArray(new String[0]), new String[0]);
        if (readOptions.getPartitionColumnName().isPresent()) {
            long lowerBound = readOptions.getPartitionLowerBound().get();
            long upperBound = readOptions.getPartitionUpperBound().get();
            int numPartitions = readOptions.getNumPartitions().get();
            builder.setParametersProvider(
                    new JdbcNumericBetweenParametersProvider(lowerBound, upperBound).ofBatchNum(numPartitions));
            query += " WHERE " + dialect.quoteIdentifier(readOptions.getPartitionColumnName().get())
                    + " BETWEEN ? AND ?";
        }
        if (limit >= 0) {
            query = String.format("%s %s", query, dialect.getLimitClause(limit));
        }
        builder.setQuery(query);
        final RowType rowType = (RowType) physicalRowDataType.getLogicalType();
        builder.setRowConverter(dialect.getRowConverter(rowType));
        builder.setRowDataTypeInfo(runtimeProviderContext.createTypeInformation(physicalRowDataType));
        return builder.build();
    }

    /**
     * 获取字段名集合keyNames
     *
     * @param context context
     * @param physicalSchema physicalSchema
     * @return 返回字段名集合keyNames
     */
    public String[] getKeyNames(LookupTableSource.LookupContext context, DataType physicalSchema) {
        // JDBC only support non-nested look up keys
        String[] keyNames = new String[context.getKeys().length];
        for (int i = 0; i < keyNames.length; i++) {
            int[] innerKeyArr = context.getKeys()[i];
            Preconditions.checkArgument(innerKeyArr.length == 1, "JDBC only support non-nested look up keys");
            keyNames[i] = DataType.getFieldNames(physicalRowDataType).get(innerKeyArr[0]);
        }
        return keyNames;
    }

    @Override
    public void applyLimit(long limit) {
        this.limit = limit;
    }
}
