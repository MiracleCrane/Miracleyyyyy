/*
 * 文 件 名:  JdbcConnectorOptionsBuilder.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  w00318695
 * 修改时间： 2022/10/8
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.gaussdb.options;

import static org.apache.flink.util.Preconditions.checkNotNull;

import com.huawei.dataservice.sql.connector.gaussdb.exp.GaussBaseConnectorRuntimeException;

import org.apache.flink.connector.jdbc.dialect.JdbcDialect;
import org.apache.flink.connector.jdbc.internal.options.JdbcConnectorOptions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * JdbcConnectorOptions的Builder
 *
 * @author w00318695
 * @version [SmartCampus V100R001C00, 2022/10/8]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class JdbcConnectorOptionsBuilder {
    private String dbURL;
    private String tableName;
    private String driverName;
    private String username;
    private String password;
    private JdbcDialect dialect;
    private Integer parallelism;
    private int connectionCheckTimeoutSeconds = 60;

    public static JdbcConnectorOptionsBuilder builder() {
        return new JdbcConnectorOptionsBuilder();
    }

    /**
     * 表名，必选
     *
     * @param tableName 表名称
     * @return this
     */
    public JdbcConnectorOptionsBuilder setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * 数据库连接用户名，必选
     *
     * @param username 数据库连接用户名
     * @return this
     */
    public JdbcConnectorOptionsBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * 数据库连接密码，必选
     *
     * @param password 数据库连接密码
     * @return this
     */
    public JdbcConnectorOptionsBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * 连接超时时间
     *
     * @param connectionCheckTimeoutSeconds 超时时间
     * @return this
     */
    public JdbcConnectorOptionsBuilder setConnectionCheckTimeoutSeconds(int connectionCheckTimeoutSeconds) {
        this.connectionCheckTimeoutSeconds = connectionCheckTimeoutSeconds;
        return this;
    }

    /**
     * 数据库驱动名称
     *
     * @param driverName 驱动名
     * @return this
     */
    public JdbcConnectorOptionsBuilder setDriverName(String driverName) {
        this.driverName = driverName;
        return this;
    }

    /**
     * jdbc连接串
     *
     * @param dbURL 连接串
     * @return this
     */
    public JdbcConnectorOptionsBuilder setDBUrl(String dbURL) {
        this.dbURL = dbURL;
        return this;
    }

    /**
     * jdbc方言类
     *
     * @param dialect 方言实例
     * @return this
     */
    public JdbcConnectorOptionsBuilder setDialect(JdbcDialect dialect) {
        this.dialect = dialect;
        return this;
    }

    /**
     * 并行度
     *
     * @param parallelism 并行度
     * @return this
     */
    public JdbcConnectorOptionsBuilder setParallelism(Integer parallelism) {
        this.parallelism = parallelism;
        return this;
    }

    /**
     * 构造实例
     *
     * @return JdbcConnectorOptions
     */
    public JdbcConnectorOptions build() {
        checkNotNull(dbURL, "No dbURL supplied.");
        checkNotNull(tableName, "No tableName supplied.");
        if (this.driverName == null) {
            Optional<String> optional = dialect.defaultDriverName();
            this.driverName = optional.orElseThrow(() -> new NullPointerException("No driverName supplied."));
        }
        Constructor<?> con = null;
        try {
            con = JdbcConnectorOptions.class.getDeclaredConstructor(String.class, String.class, String.class,
                    String.class, String.class, JdbcDialect.class, Integer.class, int.class);
        } catch (NoSuchMethodException e) {
            throw new GaussBaseConnectorRuntimeException("JdbcConnectionOptions build failed!", e);
        }
        con.setAccessible(true);
        try {
            return (JdbcConnectorOptions) con.newInstance(dbURL, tableName, driverName, username, password, dialect,
                    parallelism, connectionCheckTimeoutSeconds);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new GaussBaseConnectorRuntimeException("JdbcConnectionOptions build failed!", e);
        }
    }
}
