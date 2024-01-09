/*
 * 文 件 名:  DWFactory.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.factory;

import com.huawei.smartcampus.datatool.monitor.domain.dw.DataWareHouse;
import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnector;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.RealDataSourceProvider;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.SQLExecutorProvider;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.SingleDWDataSourceHolder;
import com.huawei.smartcampus.datatool.monitor.domain.dw.stat.DWStatistics;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.SysConfigGateWay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 数仓创建的工厂类
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
@Component
public class DWFactory {
    private SysConfigGateWay sysConfigGateway;

    private SQLExecutorProvider sqlExecutorProvider;

    private RealDataSourceProvider realDataSourceProvider;

    @Autowired
    public DWFactory(SysConfigGateWay sysConfigGateway) {
        this.sysConfigGateway = sysConfigGateway;
    }

    public DWFactory() {
    }

    public DataWareHouse create() {
        DWConnector dwConnector = new DWConnector(sysConfigGateway, SingleDWDataSourceHolder.getInstance());
        if (sqlExecutorProvider != null) {
            dwConnector.setSqlExecutorProvider(sqlExecutorProvider);
        }
        if (realDataSourceProvider != null) {
            dwConnector.setRealDataSourceProvider(realDataSourceProvider);
        }
        DWStatistics dwStatistics = new DWStatistics(dwConnector);
        return new DataWareHouse(dwStatistics, dwConnector);
    }

    public void setSysConfigGateway(SysConfigGateWay sysConfigGateway) {
        this.sysConfigGateway = sysConfigGateway;
    }

    public void setSqlExecutorProvider(SQLExecutorProvider sqlExecutorProvider) {
        this.sqlExecutorProvider = sqlExecutorProvider;
    }

    public void setRealDataSourceProvider(RealDataSourceProvider realDataSourceProvider) {
        this.realDataSourceProvider = realDataSourceProvider;
    }
}