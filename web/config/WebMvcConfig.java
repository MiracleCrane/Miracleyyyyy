/*
 * 文 件 名:  WebMvcConfig.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2021/2/9
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.config;

import com.huawei.smartcampus.datatool.auditlog.AuditInterceptor;
import com.huawei.smartcampus.datatool.i18n.I18nLocaleResolver;
import com.huawei.smartcampus.datatool.interceptor.ApiLogInterceptor;
import com.huawei.smartcampus.datatool.interceptor.GetUserInterceptor;
import com.huawei.smartcampus.datatool.interceptor.GetIpInterceptor;
import com.huawei.smartcampus.datatool.properties.I18nConfig;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import org.hibernate.validator.BaseHibernateValidatorConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * web mvc配置类
 *
 * @author z00569896
 * @version [SmartCampus V100R001C00, 2021/2/9]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private GetUserInterceptor getUserInterceptor;

    @Autowired
    private ApiLogInterceptor apiLogInterceptor;

    @Autowired
    private AuditInterceptor auditInterceptor;

    @Autowired
    private GetIpInterceptor getIpInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getIpInterceptor).addPathPatterns("/**").excludePathPatterns("/health-check");
        registry.addInterceptor(getUserInterceptor).addPathPatterns("/**").excludePathPatterns("/health-check");
        registry.addInterceptor(apiLogInterceptor).addPathPatterns("/**").excludePathPatterns("/health-check");
        registry.addInterceptor(auditInterceptor).addPathPatterns("/**").excludePathPatterns("/health-check");
    }

    /**
     * 自定义多语言解释器
     *
     * @return LocaleResolver 多语言解释器
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new I18nLocaleResolver();
    }

    /**
     * 消除@JsonProperty注解，返回响应字段问题
     *
     * @param converters 转换者
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(0, fastConverter);
    }

    /**
     * 实体类字段校验国际化引入
     *
     * @return Validator 校验器
     */
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();

        // 国际化配置
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding(I18nConfig.messagesEncoding());
        messageSource.setBasenames(new String[]{I18nConfig.messagesBasename()});
        validator.setValidationMessageSource(messageSource);

        // 设置快速失败,遇到第一个校验异常就返回
        validator.getValidationPropertyMap().put(BaseHibernateValidatorConfiguration.FAIL_FAST,
                Boolean.TRUE.toString());
        return validator;
    }
}
