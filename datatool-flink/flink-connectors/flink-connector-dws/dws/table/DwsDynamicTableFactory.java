/*
 * 文 件 名:  DwsDynamicTableFactory.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2022/01/06
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.dws.table;

import com.huawei.dataservice.sql.connector.dws.dialect.DwsDialect;
import com.huawei.dataservice.sql.connector.jdbc.table.DataToolJdbcDynamicTableSink;
import com.huawei.dataservice.sql.connector.gaussdb.table.GaussBaseDynamicTableSource;
import com.huawei.dataservice.sql.connector.jdbc.table.factory.DataToolJDBCDynamicTableFactory;

import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.connector.jdbc.dialect.JdbcDialect;
import org.apache.flink.connector.jdbc.internal.options.JdbcConnectorOptions;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.factories.FactoryUtil;

/**
 * dws connector factory
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2021/10/15]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class DwsDynamicTableFactory extends DataToolJDBCDynamicTableFactory {
    private final JdbcDialect dialect = new DwsDialect();

    @Override
    public DynamicTableSink doCreateDynamicTableSink(Context context) {
        final FactoryUtil.TableFactoryHelper helper = FactoryUtil.createTableFactoryHelper(this, context);
        ReadableConfig config = getAndValidateReadableConfig(helper);
        validateDataTypeWithJdbcDialect(context.getPhysicalRowDataType(), dialect);
        JdbcConnectorOptions jdbcOptions = getJdbcOptions(config, dialect);
        return new DataToolJdbcDynamicTableSink(jdbcOptions, getJdbcExecutionOptions(config),
                getJdbcDmlOptions(jdbcOptions, context.getPhysicalRowDataType(), context.getPrimaryKeyIndexes()),
                context.getPhysicalRowDataType());
    }

    @Override
    public DynamicTableSource doCreateDynamicTableSource(Context context) {
        final FactoryUtil.TableFactoryHelper helper = FactoryUtil.createTableFactoryHelper(this, context);
        ReadableConfig config = getAndValidateReadableConfig(helper);
        // 校验dataType 与 dialect是否匹配
        validateDataTypeWithJdbcDialect(context.getPhysicalRowDataType(), dialect);
        // 使用自己的JDBC DynamicTableSource
        return new GaussBaseDynamicTableSource(getJdbcOptions(config, dialect), getJdbcReadOptions(config),
                getJdbcLookupOptions(config), context.getPhysicalRowDataType());
    }

    @Override
    public String factoryIdentifier() {
        return DwsDialect.DIALECT_NAME;
    }
}
