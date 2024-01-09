/*
 * 文 件 名:  HttpCustomizer.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/7/24
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.resttemplate;

import com.huawei.smartcampus.datatool.config.RestTemplateConfig;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 设置resttemplate参数
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/7/24]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class HttpCustomizer implements RestTemplateCustomizer {
    @Override
    public void customize(RestTemplate restTemplate) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 超时时间设置30s
        factory.setReadTimeout(RestTemplateConfig.readTimeout());
        // 连接超时时间设置10s
        factory.setConnectTimeout(RestTemplateConfig.connectTimeout());
        restTemplate.setRequestFactory(factory);
    }
}

