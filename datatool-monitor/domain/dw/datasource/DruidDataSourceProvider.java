/*
 * 文 件 名:  DruidDataSourceProvider.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.datasource;

import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnInfo;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.SQLException;
import java.util.Locale;

import javax.sql.DataSource;

/**
 * 基于Druid实现的数据源
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/27]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DruidDataSourceProvider implements RealDataSourceProvider {
    /**
     * true：在创建数据源时立即初始化，但在延迟初始化的情况下，在调用getConnection方法时候也会初始化的
     * false：延迟初始化，可以避免在createDataSource的时候连不上数据库而报错，ut用例时可以延迟初始化
     */
    private boolean initializeNow;

    /**
     * 提供一个默认的加解密实现
     */
    private DWCryptor dwCryptor = new DefaultCryptor();

    public DruidDataSourceProvider(boolean initializeNow, DWCryptor dwCryptor) {
        this.initializeNow = initializeNow;
        this.dwCryptor = dwCryptor;
    }

    public DruidDataSourceProvider() {
        this.initializeNow = true;
    }

    @Override
    public DataSource createDataSource(DWConnInfo connInfo) throws SQLException {
        DBType dbType = DBType.valueOf(connInfo.getType().toUpperCase(Locale.ROOT));
        String url = String.format(Locale.ROOT, dbType.url(), connInfo.getHost(), connInfo.getPort(),
                connInfo.getDatabase());
        String decryedPwSecret = dwCryptor.decrypt(connInfo.getEncryptedPwd());
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(connInfo.getUser());
        druidDataSource.setPassword(decryedPwSecret);
        druidDataSource.setDriverClassName(dbType.driver());
        druidDataSource.setMaxActive(20);
        druidDataSource.setInitialSize(2);
        druidDataSource.setMinIdle(2);
        druidDataSource.setMaxWait(10000L);
        druidDataSource.setValidationQuery("SELECT 1");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000L);
        druidDataSource.setMinEvictableIdleTimeMillis(30000L);
        druidDataSource.setMaxOpenPreparedStatements(20);
        druidDataSource.setRemoveAbandoned(true);
        druidDataSource.setRemoveAbandonedTimeout(1800);
        druidDataSource.setLogAbandoned(true);
        druidDataSource.setTimeBetweenConnectErrorMillis(60000);
        druidDataSource.setConnectionErrorRetryAttempts(3); // 重试次数
        druidDataSource.setBreakAfterAcquireFailure(true); // 重试失败后停止重试

        if (initializeNow) {
            druidDataSource.init();
        }

        return druidDataSource;
    }
}