/*
 * 文 件 名:  AbstractGaussBaseDialect.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2022/4/25
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.gaussdb.table;

import org.apache.flink.connector.jdbc.converter.JdbcRowConverter;
import org.apache.flink.connector.jdbc.dialect.AbstractDialect;
import org.apache.flink.connector.jdbc.internal.converter.PostgresRowConverter;
import org.apache.flink.table.types.logical.LogicalTypeRoot;
import org.apache.flink.table.types.logical.RowType;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

/**
 * gauss database common abstract dialect
 * <功能详细描述>
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2022/4/25]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public abstract class AbstractGaussBaseDialect extends AbstractDialect {
    private static final long serialVersionUID = -8040348806361084725L;
    private static final int MAX_TIMESTAMP_PRECISION = 6;
    private static final int MIN_TIMESTAMP_PRECISION = 1;
    private static final int MAX_DECIMAL_PRECISION = 1000;
    private static final int MIN_DECIMAL_PRECISION = 1;

    @Override
    public JdbcRowConverter getRowConverter(RowType rowType) {
        return new PostgresRowConverter(rowType);
    }

    @Override
    public String getLimitClause(long limit) {
        return "LIMIT " + limit;
    }

    @Override
    public String quoteIdentifier(String identifier) {
        return identifier;
    }

    @Override
    public Set<LogicalTypeRoot> supportedTypes() {
        return EnumSet.of(LogicalTypeRoot.CHAR, LogicalTypeRoot.VARCHAR, LogicalTypeRoot.BOOLEAN,
                LogicalTypeRoot.VARBINARY, LogicalTypeRoot.DECIMAL, LogicalTypeRoot.TINYINT, LogicalTypeRoot.SMALLINT,
                LogicalTypeRoot.INTEGER, LogicalTypeRoot.BIGINT, LogicalTypeRoot.FLOAT, LogicalTypeRoot.DOUBLE,
                LogicalTypeRoot.DATE, LogicalTypeRoot.TIME_WITHOUT_TIME_ZONE,
                LogicalTypeRoot.TIMESTAMP_WITHOUT_TIME_ZONE, LogicalTypeRoot.TIMESTAMP_WITH_LOCAL_TIME_ZONE,
                LogicalTypeRoot.ARRAY);
    }

    @Override
    public Optional<String> getUpsertStatement(String tableName, String[] fieldNames, String[] uniqueKeyFields) {
        return Optional.empty();
    }

    @Override
    public Optional<Range> timestampPrecisionRange() {
        return Optional.of(Range.of(1, 9));
    }

    @Override
    public Optional<Range> decimalPrecisionRange() {
        return Optional.of(Range.of(1, 1000));
    }
}
