/*
 * 文 件 名:  JobStateAmountResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/17
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview;

/**
 * 作业状态数量响应
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class JobStateAmountResponse {
    private JobStateAmountVo summary;
    private JobStateTypeAmountVo all;
    private JobStateTypeAmountVo baseline;
    private JobStateTypeAmountVo custom;

    public JobStateAmountVo getSummary() {
        return summary;
    }

    public void setSummary(JobStateAmountVo summary) {
        this.summary = summary;
    }

    public JobStateTypeAmountVo getAll() {
        return all;
    }

    public void setAll(JobStateTypeAmountVo all) {
        this.all = all;
    }

    public JobStateTypeAmountVo getBaseline() {
        return baseline;
    }

    public void setBaseline(JobStateTypeAmountVo baseline) {
        this.baseline = baseline;
    }

    public JobStateTypeAmountVo getCustom() {
        return custom;
    }

    public void setCustom(JobStateTypeAmountVo custom) {
        this.custom = custom;
    }
}