/*
 * 文 件 名:  SpringContextHelper.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/8
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring上下文帮助类
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/8]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Component
public class SpringContextHelper implements ApplicationContextAware {
    /**
     * Spring应用上下文环境
     */
    private static ApplicationContext applicationContext;

    /**
     * 重写并初始化上下文
     *
     * @param applicationContext 应用上下文
     * @throws BeansException bean异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 初始化applicationContext
        setApplicationContextInner(applicationContext);
    }

    /**
     * 通过类获取
     *
     * @param clazz 注入的类
     * @param <T> 返回类型
     * @return 返回这个bean
     * @throws BeansException bean异常
     */
    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return applicationContext.getBean(clazz);
    }

    /**
     * 通过名字获取
     *
     * @param name 名字
     * @param <T> 返回类型
     * @param clazz 类
     * @return 返回这个bean
     * @throws BeansException bean异常
     */
    public static <T> T getBean(String name, Class<T> clazz) throws BeansException {
        return applicationContext.getBean(name, clazz);
    }

    private static void setApplicationContextInner(ApplicationContext applicationContextInner) {
        SpringContextHelper.applicationContext = applicationContextInner;
    }
}