/*
 * 文 件 名:  DataToolJdbcOutputFormatBuilder.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/9/9
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.jdbc;

import com.huawei.dataservice.sql.connector.gaussdb.exp.DataToolJDBCConnectorRuntimeException;
import com.huawei.dataservice.sql.connector.jdbc.connection.DataToolJdbcConnectionProvider;
import com.huawei.dataservice.sql.connector.util.InvokeUtils;

import org.apache.flink.connector.jdbc.internal.JdbcOutputFormat;
import org.apache.flink.connector.jdbc.internal.options.JdbcConnectorOptions;
import org.apache.flink.connector.jdbc.table.JdbcOutputFormatBuilder;
import org.apache.flink.table.data.RowData;

/**
 * DataTool定制JdbcOutputFormatBuilder，内部使用DataToolJdbcConnectionProvider
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/9/9]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DataToolJdbcOutputFormatBuilder extends JdbcOutputFormatBuilder {
    private static final long serialVersionUID = 1L;

    private JdbcConnectorOptions jdbcOptions;

    @Override
    public JdbcOutputFormatBuilder setJdbcOptions(JdbcConnectorOptions jdbcOptions) {
        super.setJdbcOptions(jdbcOptions);
        this.jdbcOptions = jdbcOptions;
        return this;
    }

    @Override
    public JdbcOutputFormat<RowData, ?, ?> build() {
        JdbcOutputFormat<RowData, ?, ?> jdbcOutputFormat = super.build();
        try {
            // 这里只能反射来修改JdbcConnectionProvider了
            InvokeUtils.invokeModify(jdbcOutputFormat, "connectionProvider",
                    new DataToolJdbcConnectionProvider(jdbcOptions));
        } catch (Exception e) {
            throw new DataToolJDBCConnectorRuntimeException("set DataToolJdbcConnectionProvider failed.");
        }
        return jdbcOutputFormat;
    }

}