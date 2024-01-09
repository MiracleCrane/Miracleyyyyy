/*
 * 文 件 名:  LayerAggregation.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/27
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export.table;

import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.GroupAmount;

/**
 * 分层汇总
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/27]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class LayerAggregation {
    private GroupAmount dm;
    private GroupAmount dwr;
    private GroupAmount dwi;
    private GroupAmount total;

    public GroupAmount getDm() {
        return dm;
    }

    public void setDm(GroupAmount dm) {
        this.dm = dm;
    }

    public GroupAmount getDwr() {
        return dwr;
    }

    public void setDwr(GroupAmount dwr) {
        this.dwr = dwr;
    }

    public GroupAmount getDwi() {
        return dwi;
    }

    public void setDwi(GroupAmount dwi) {
        this.dwi = dwi;
    }

    public GroupAmount getTotal() {
        return total;
    }

    public void setTotal(GroupAmount total) {
        this.total = total;
    }
}