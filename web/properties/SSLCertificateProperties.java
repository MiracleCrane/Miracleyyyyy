/*
 * 文 件 名:  SSLCertificateProperties.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/10/24
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.properties;

/**
 * SSL证书配置
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/10/24]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class SSLCertificateProperties {
    private static final ApplicationProperties PROPERTIES = ApplicationProperties.instance();
    private static final String KEY_STORE_TYPE = "client.ssl.key-store-type";
    private static final String KEY_STORE_PASSWORD = "client.ssl.key-store-password";
    private static final String KEY_STORE = "client.ssl.key-store";

    public static String getKeyStoreType() {
        return PROPERTIES.getString(KEY_STORE_TYPE);
    }

    public static String getKeyStorePassword() {
        return PROPERTIES.getString(KEY_STORE_PASSWORD);
    }

    public static String getKeyStore() {
        return PROPERTIES.getString(KEY_STORE);
    }
}