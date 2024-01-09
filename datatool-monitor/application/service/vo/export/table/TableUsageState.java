/*
 * 文 件 名:  TableUsageState.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/27
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export.table;

import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.UsageAmount;

/**
 * 表使用状态
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/27]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class TableUsageState {
    private UsageAmount dm;
    private UsageAmount dwr;
    private UsageAmount dwi;
    private UsageAmount total;

    public UsageAmount getDm() {
        return dm;
    }

    public void setDm(UsageAmount dm) {
        this.dm = dm;
    }

    public UsageAmount getDwr() {
        return dwr;
    }

    public void setDwr(UsageAmount dwr) {
        this.dwr = dwr;
    }

    public UsageAmount getDwi() {
        return dwi;
    }

    public void setDwi(UsageAmount dwi) {
        this.dwi = dwi;
    }

    public UsageAmount getTotal() {
        return total;
    }

    public void setTotal(UsageAmount total) {
        this.total = total;
    }
}