/*
 * 文 件 名:  DWTableRowStat.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.stat;

import com.huawei.smartcampus.datatool.monitor.domain.dw.DWTableRow;
import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnector;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.DWGateway;

import java.util.List;

/**
 * 表的行数统计
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/20]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWTableRowStat implements DWStatisticsable<DWTableRows> {
    private DWGateway dwGateway;

    @Override
    public void setDWGateway(DWGateway dwGateway) {
        this.dwGateway = dwGateway;
    }

    @Override
    public DWTableRows calculate(DWConnector dwConnector) {
        List<DWTableRow> dwTableRows = dwGateway.getDWTableRows(dwConnector.getDWConnection());
        return new DWTableRows(dwTableRows);
    }
}