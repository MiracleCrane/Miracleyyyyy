/*
 * 文 件 名:  AppApplicationListener.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.listener;

import com.alibaba.druid.pool.DruidDataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class AppApplicationListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        ApplicationContext applicationContext = contextClosedEvent.getApplicationContext();
        DruidDataSource dataSource = (DruidDataSource) applicationContext.getBean("dataSource");
        dataSource.close();
    }
}
