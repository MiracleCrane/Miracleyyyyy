/*
 * 文 件 名:  OpenGaussDynamicTableSource.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2021/10/15
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.opengauss.table;

import com.huawei.dataservice.sql.connector.gaussdb.table.GaussBaseDynamicTableSource;
import com.huawei.dataservice.sql.connector.gaussdb.table.GaussBaseRowDataLookupFunction;

import org.apache.flink.annotation.Internal;
import org.apache.flink.connector.jdbc.internal.options.JdbcConnectorOptions;
import org.apache.flink.connector.jdbc.internal.options.JdbcLookupOptions;
import org.apache.flink.connector.jdbc.internal.options.JdbcReadOptions;
import org.apache.flink.table.connector.source.TableFunctionProvider;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.logical.RowType;

/**
 * openGauss connector source
 * 获取维表数据相关
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2021/10/15]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Internal
public class OpenGaussDynamicTableSource extends GaussBaseDynamicTableSource {
    public OpenGaussDynamicTableSource(JdbcConnectorOptions options, JdbcReadOptions readOptions,
            JdbcLookupOptions lookupOptions, DataType physicalRowDataType) {
        super(options, readOptions, lookupOptions, physicalRowDataType);
    }

    @Override
    public LookupRuntimeProvider getLookupRuntimeProvider(LookupContext context) {
        String[] keyNames = getKeyNames(context, getPhysicalRowDataType());
        final RowType rowType = (RowType) getPhysicalRowDataType().getLogicalType();
        return TableFunctionProvider.of(new GaussBaseRowDataLookupFunction(getOptions(), getLookupOptions(),
                DataType.getFieldNames(getPhysicalRowDataType()).toArray(new String[0]),
                DataType.getFieldDataTypes(getPhysicalRowDataType()).toArray(new DataType[0]), keyNames, rowType,
                getDialect()));
    }
}
