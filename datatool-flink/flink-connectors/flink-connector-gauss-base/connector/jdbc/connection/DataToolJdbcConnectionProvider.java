/*
 * 文 件 名:  DataToolJdbcConnectionProvider.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/9/9
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.jdbc.connection;

import com.huawei.dataservice.sql.connector.gaussdb.exp.DataToolJDBCConnectorRuntimeException;
import com.huawei.smartcampus.datatool.utils.DataToolUtils;

import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.internal.connection.SimpleJdbcConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 做了安全防护，添加Database黑名单功能
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/9/9]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DataToolJdbcConnectionProvider extends SimpleJdbcConnectionProvider {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(DataToolJdbcConnectionProvider.class);

    public DataToolJdbcConnectionProvider(JdbcConnectionOptions jdbcOptions) {
        super(jdbcOptions);
    }

    @Override
    public Connection getOrEstablishConnection() throws SQLException, ClassNotFoundException {
        Connection conn = super.getOrEstablishConnection();
        checkDatabaseBlacklist(conn);
        return conn;
    }

    private void checkDatabaseBlacklist(Connection conn) {
        // 获取待连接的数据库名
        String databaseName = getDatabaseName(conn);
        // 判断待连接的数据库是否在黑名单中
        if (DataToolUtils.isBlackListDataBase(databaseName)) {
            String msg = "database not exist or no permission to access.";
            LOGGER.error(msg);
            throw new DataToolJDBCConnectorRuntimeException(msg);
        }
    }

    /*
     * 获取数据库的名称。
     * 这里获取数据库的名称如果直接通过解析JDBC url有两种方式
     * 一种是用正则解析，但是实际上不同数据库类型的jdbc串可能是不一样的无法通用
     * 另一种是使用DriverManager.getDriver().getPropertyInfo()方法，但是试验发现不同数据库的数据库属性名称不一样，
     * 并且mysql的驱动返回里不包含数据库名称
     * 没有办法只能尝试先连接一下数据库然后来获取数据库名称
     */
    private String getDatabaseName(Connection conn) {
        try {
            return conn.getCatalog();
        } catch (SQLException e) {
            throw new DataToolJDBCConnectorRuntimeException("database access error occurs or connection is closed.");
        }
    }
}