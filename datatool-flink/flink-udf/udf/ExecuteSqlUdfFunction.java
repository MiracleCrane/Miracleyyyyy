/*
 * 文 件 名:  ExecuteSqlUdfFunction.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2021/11/2
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.udf;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.flink.table.functions.ScalarFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 执行 SQL 语句
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2021/11/2]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class ExecuteSqlUdfFunction extends ScalarFunction {
    private static final long serialVersionUID = 5746027086724560378L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteSqlUdfFunction.class);
    private boolean isInit;

    // 数据库连接池
    private BasicDataSource dataSource;

    /**
     * 在执行数据库执行SQL语句
     *
     * @param driver 数据库驱动
     * @param url 数据库连接串
     * @param username 数据库用户名
     * @param password 数据库密码
     * @param sql 执行的SQL，可以使用预编译的
     * @param paramNum sql传参数量
     * @param param sql参数列表
     * @return 返回单个值
     */
    public String eval(
            String driver, String url, String username, String password, String sql, int paramNum, String... param) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Receive sql: {}", sql);
        }
        // 获取数据库连接
        if (!isInit) {
            dataSource = new BasicDataSource();
            initDataSource(driver, url, username, password);
        }
        String result = "";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < paramNum; i++) {
                ps.setString(i + 1, param[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getString(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Execute sql error.");
            return "";
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (dataSource != null) {
            dataSource.close();
        }
    }

    private void initDataSource(String driver, String url, String username, String password) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Init DB connection pool......");
        }
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(1);
        dataSource.setMaxTotal(2);
        dataSource.setMinIdle(1);
        dataSource.setMaxWaitMillis(60000);
        isInit = true;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Init DB connection pool success!");
        }
    }

    @Override
    public boolean isDeterministic() {
        return false;
    }
}
