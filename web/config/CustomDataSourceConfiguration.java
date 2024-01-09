/*
 * 文 件 名:  CustomDataSourceConfiguration.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2021/2/9
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.config;

import com.huawei.smartcampus.datatool.utils.CfgParser;
import com.huawei.smartcampus.datatool.utils.DataToolCryptor;

import com.alibaba.druid.pool.DruidDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 数据库连接
 *
 * @author z00569896
 * @version [SmartCampus V100R001C00, 2021/2/9]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Configuration
public class CustomDataSourceConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomDataSourceConfiguration.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String pwSecret;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    /**
     * druid数据源
     *
     * @return 数据源
     */
    @Bean("dataSource")
    public DataSource druidDataSource() {
        System.setProperty("file.encoding", "UTF-8");
        try (DruidDataSource druidDataSource = new DruidDataSource()) {
            String decryedPwSecret = DataToolCryptor.decodingSecret(pwSecret);
            druidDataSource.configFromPropety(CfgParser.getApplicationProperties());
            druidDataSource.setUrl(url);
            druidDataSource.setPassword(decryedPwSecret);
            druidDataSource.setUsername(username);
            druidDataSource.setDriverClassName(driverClassName);
            return druidDataSource;
        }
    }
}
