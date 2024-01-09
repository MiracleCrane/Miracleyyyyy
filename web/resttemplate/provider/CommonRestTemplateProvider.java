/*
 * 文 件 名:  CommonRestTemplateProvider.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/7/24
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.resttemplate.provider;

import com.huawei.smartcampus.datatool.resttemplate.HttpCustomizer;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

/**
 * 一个普通的resttemplate
 * 支持http代理
 * 提供Bean注入和静态方法两种方式获取resttemplate
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/7/24]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Configuration
public class CommonRestTemplateProvider {
    private final RestTemplateBuilder builder = getBuilder();

    /**
     * 组装builder，添加http代理能力
     *
     * @return RestTemplateBuilder
     */
    private RestTemplateBuilder getBuilder() {
        return new RestTemplateBuilder(new HttpCustomizer());
    }

    /**
     * RestTemplate
     *
     * @return RestTemplate
     */
    @Bean("commonRestTemplate")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RestTemplate restTemplate() {
        return builder.build();
    }

    /**
     * 获取一个commonRestTemplate的bean
     *
     * @return rest template
     */

    public static RestTemplate getRestTemplate() {
        return SpringContextHelper.getBean("commonRestTemplate", RestTemplate.class);
    }
}