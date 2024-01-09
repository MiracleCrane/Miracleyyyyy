/*
 * 文 件 名:  GuavaCacheConfig.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2021/2/9
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author z00569896
 * @version [SmartCampus V100R001C00, 2021/2/9]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Configuration
public class GuavaCacheConfig {
    /**
     * url缓存管理器
     *
     * @return url缓存构建器
     */
    @Bean(name = "urlInfo")
    public Cache<String, String> urlCacheManager() {
        return CacheBuilder.newBuilder().maximumSize(5000).build();
    }

    /**
     * 密码缓存管理器
     *
     * @return 密码缓存构建器
     */
    @Bean(name = "pwCache")
    public Cache<String, String> pwCacheManager() {
        return CacheBuilder.newBuilder().maximumSize(1000).build();
    }
}
