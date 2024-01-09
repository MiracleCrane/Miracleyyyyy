/*
 * 文 件 名:  OpenGaussDynamicTableFactory.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2021/10/15
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.opengauss.table;

import com.huawei.dataservice.sql.connector.jdbc.table.DataToolJdbcDynamicTableSink;
import com.huawei.dataservice.sql.connector.jdbc.table.factory.DataToolJDBCDynamicTableFactory;
import com.huawei.dataservice.sql.connector.opengauss.dialect.OpenGaussDialect;

import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.connector.jdbc.dialect.JdbcDialect;
import org.apache.flink.connector.jdbc.internal.options.JdbcConnectorOptions;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.factories.FactoryUtil;

/**
 * openGauss connector factory
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2021/10/15]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class OpenGaussDynamicTableFactory extends DataToolJDBCDynamicTableFactory {
    private final JdbcDialect dialect = new OpenGaussDialect();

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
        validateDataTypeWithJdbcDialect(context.getPhysicalRowDataType(), dialect);
        return new OpenGaussDynamicTableSource(getJdbcOptions(config, dialect), getJdbcReadOptions(config),
                getJdbcLookupOptions(config), context.getPhysicalRowDataType());
    }

    @Override
    public String factoryIdentifier() {
        return OpenGaussDialect.DIALECT_NAME;
    }
}
