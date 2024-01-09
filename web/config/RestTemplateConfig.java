/*
 * 文 件 名:  RestTemplateConfig.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/7/24
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.config;

import com.huawei.smartcampus.datatool.properties.ApplicationProperties;

/**
 * resttemplate配置项
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/7/24]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class RestTemplateConfig {
    private static ApplicationProperties properties = ApplicationProperties.instance();

    private static final String CONNECT_TIMEOUT = "resttemplate.connect.timeout";

    private static final String READ_TIMEOUT = "resttemplate.read.timeout";

    public static int connectTimeout() {
        return properties.getInt(CONNECT_TIMEOUT, 15000);
    }

    public static int readTimeout() {
        return properties.getInt(READ_TIMEOUT, 30000);
    }
}