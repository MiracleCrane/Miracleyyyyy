/*
 * 文 件 名:  DWStorageStat.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.stat;

import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnector;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.DWConnection;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.DWGateway;

/**
 * 统计数据仓库整体存储空间信息
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/19]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWStorageStat implements DWStatisticsable<DWStorage> {
    private DWGateway dwGateway;

    @Override
    public DWStorage calculate(DWConnector dwConnector) {
        DWConnection dwConnection = dwConnector.getDWConnection();
        Long dbMaximumSize = dwGateway.getDWMaximumSize(dwConnection);
        Long dbUsedSize = dwGateway.getDWUsedSize(dwConnection);

        DWStorage info = new DWStorage();
        info.setMaximumSize(dbMaximumSize);
        info.setUsedSize(dbUsedSize);
        return info;
    }

    @Override
    public void setDWGateway(DWGateway dwGateway) {
        this.dwGateway = dwGateway;
    }
}