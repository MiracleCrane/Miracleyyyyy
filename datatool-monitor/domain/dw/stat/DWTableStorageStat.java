/*
 * 文 件 名:  DWTableStorageStat.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.stat;

import com.huawei.smartcampus.datatool.monitor.domain.dw.DWTableSize;
import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnector;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.DWGateway;

import java.util.List;

/**
 * 统计数据库的表存储大小
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/19]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWTableStorageStat implements DWStatisticsable<DWTableStorage> {
    private DWGateway dwGateway;

    @Override
    public DWTableStorage calculate(DWConnector dwConnector) {
        List<DWTableSize> dwTableSize = dwGateway.getDWTableSize(dwConnector.getDWConnection());
        return new DWTableStorage(dwTableSize);
    }

    @Override
    public void setDWGateway(DWGateway dwGateway) {
        this.dwGateway = dwGateway;
    }
}