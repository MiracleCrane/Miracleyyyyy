/*
 * 文 件 名:  OverViewResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview;

/**
 * 资产概览响应
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/11/2]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class OverviewResponse<T1, T2> {
    private T1 summary;
    private T2 all;
    private T2 baseline;
    private T2 custom;

    public T1 getSummary() {
        return summary;
    }

    public T2 getAll() {
        return all;
    }

    public T2 getBaseline() {
        return baseline;
    }

    public T2 getCustom() {
        return custom;
    }

    public OverviewResponse<T1, T2> setSummary(T1 summary) {
        this.summary = summary;
        return this;
    }

    public OverviewResponse<T1, T2> setBaseline(T2 baseline) {
        this.baseline = baseline;
        return this;
    }

    public OverviewResponse<T1, T2> setCustom(T2 custom) {
        this.custom = custom;
        return this;
    }

    public OverviewResponse<T1, T2> setAll(T2 all) {
        this.all = all;
        return this;
    }
}