/*
 * 文 件 名:  FlinkClientPool.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30006786
 * 修改时间： 2022/1/26
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils.http;

import com.huawei.hicampus.campuscommon.common.util.ClearSensitiveDataUtil;
import com.huawei.smartcampus.datatool.properties.SSLCertificateProperties;
import com.huawei.smartcampus.datatool.utils.DataToolCryptor;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;

/**
 * httpclient连接池
 *
 * @author l30006786
 * @version [SmartCampus V100R001C00, 2022/1/26]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Component
public class HttpClientPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientPool.class);
    private static PoolingHttpClientConnectionManager manager = null;

    @Value("${httpclient.max.total}")
    private int poolMaxTotal;

    @Value("${httpclient.max.per.route}")
    private int poolMaxPerRoute;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        setManagerByValue(poolMaxTotal, poolMaxPerRoute);
    }

    /**
     * Description: 获取HttpClient
     *
     * @return org.apache.http.impl.client.CloseableHttpClient
     * @date 2022/1/27 17:19
     */
    public static CloseableHttpClient getHttpClient() {
        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(StandardCookieSpec.IGNORE).build();
        return HttpClients.custom().setConnectionManager(manager).setDefaultRequestConfig(globalConfig).build();
    }

    private void setManagerByValue(int poolMaxTotal, int poolMaxPerRoute) {
        setManager(poolMaxTotal, poolMaxPerRoute);
    }

    private static void setManager(int poolMaxTotal, int poolMaxPerRoute) {
        String keyStorePassword = null;
        try {
            String encryptPassword;
            File file = new File(SSLCertificateProperties.getKeyStorePassword());
            try (InputStream is = new FileInputStream(file);
                    InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(isr)) {
                // 读取证书密码的密文
                encryptPassword = reader.readLine();
            }
            KeyStore clientKeyStore = KeyStore.getInstance(SSLCertificateProperties.getKeyStoreType());
            keyStorePassword = DataToolCryptor.decodingSecret(encryptPassword);
            try (FileInputStream fis = new FileInputStream(SSLCertificateProperties.getKeyStore())) {
                clientKeyStore.load(fis, keyStorePassword.toCharArray());
                SSLContext sslContext = SSLContexts.custom()
                        .loadKeyMaterial(clientKeyStore, keyStorePassword.toCharArray()).build();
                SSLConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                        .setSslContext(sslContext).build();
                manager = PoolingHttpClientConnectionManagerBuilder.create()
                        .setSSLSocketFactory(sslConnectionSocketFactory).build();
                manager.setMaxTotal(poolMaxTotal);
                manager.setDefaultMaxPerRoute(poolMaxPerRoute);
            }
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException
                | UnrecoverableKeyException | KeyManagementException e) {
            LOGGER.error("load client certificate failed.");
        } catch (Exception e) {
            LOGGER.error("set http client manager failed.", e);
        } finally {
            ClearSensitiveDataUtil.clearPlainSensitiveData(keyStorePassword);
        }
    }
}
