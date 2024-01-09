/*
 * 文 件 名:  DataToolJdbcDynamicTableSink.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/9/9
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.jdbc.table;

import com.huawei.dataservice.sql.connector.jdbc.DataToolJdbcOutputFormatBuilder;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.internal.GenericJdbcSinkFunction;
import org.apache.flink.connector.jdbc.internal.options.JdbcConnectorOptions;
import org.apache.flink.connector.jdbc.internal.options.JdbcDmlOptions;
import org.apache.flink.connector.jdbc.table.JdbcDynamicTableSink;
import org.apache.flink.connector.jdbc.table.JdbcOutputFormatBuilder;
import org.apache.flink.table.connector.sink.SinkFunctionProvider;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.DataType;

/**
 * DataTool定制的JdbcDynamicTableSink，添加数据库黑名单过滤功能，用于代替flink原生的JdbcDynamicTableSink
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/9/9]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DataToolJdbcDynamicTableSink extends JdbcDynamicTableSink {
    private final JdbcConnectorOptions jdbcOptions;

    private final JdbcExecutionOptions executionOptions;

    private final JdbcDmlOptions dmlOptions;

    private final DataType physicalRowDataType;

    public DataToolJdbcDynamicTableSink(JdbcConnectorOptions jdbcOptions, JdbcExecutionOptions executionOptions,
            JdbcDmlOptions dmlOptions, DataType physicalRowDataType) {
        super(jdbcOptions, executionOptions, dmlOptions, physicalRowDataType);
        this.jdbcOptions = jdbcOptions;
        this.executionOptions = executionOptions;
        this.dmlOptions = dmlOptions;
        this.physicalRowDataType = physicalRowDataType;
    }

    @Override
    public SinkRuntimeProvider getSinkRuntimeProvider(Context context) {
        final TypeInformation<RowData> rowDataTypeInformation = context.createTypeInformation(physicalRowDataType);
        final JdbcOutputFormatBuilder builder = new DataToolJdbcOutputFormatBuilder();
        builder.setJdbcOptions(jdbcOptions);
        builder.setJdbcDmlOptions(dmlOptions);
        builder.setJdbcExecutionOptions(executionOptions);
        builder.setRowDataTypeInfo(rowDataTypeInformation);
        builder.setFieldDataTypes(DataType.getFieldDataTypes(physicalRowDataType).toArray(new DataType[0]));
        return SinkFunctionProvider.of(new GenericJdbcSinkFunction<>(builder.build()), jdbcOptions.getParallelism());
    }
}