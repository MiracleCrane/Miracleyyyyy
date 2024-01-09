/*
 * 文 件 名:  DataWareHouse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw;

import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnInfo;
import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnector;
import com.huawei.smartcampus.datatool.monitor.domain.dw.stat.DWStatistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据仓库, DW即DataWareHouse的简写
 * 数据仓库领域模型的顶层模型
 * DW模型本身应该是无状态的，是支持单例使用的
 * 当成单例使用时，DW的方法是线程安全的
 * 数据仓库跟数据库的connection连接池会维持在程序内存中，不会随着DW的销毁而销毁，避免每次使用都创建连接而消耗性能
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/17]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DataWareHouse {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataWareHouse.class);

    private DWConnector dwConnector;

    private DWStatistics dwStatistics;

    public DataWareHouse(DWStatistics dwStatistics, DWConnector dwConnector) {
        this.dwConnector = dwConnector;
        this.dwStatistics = dwStatistics;
    }

    public DWStatistics getStatistics() {
        return dwStatistics;
    }

    public DWConnInfo getConnInfo() {
        return dwConnector.getDwConnInfo();
    }
}