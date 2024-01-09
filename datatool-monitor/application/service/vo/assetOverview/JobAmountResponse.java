/*
 * 文 件 名:  JobAmountResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview;

/**
 * 作业数量响应
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/19]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class JobAmountResponse {
    private JobAmountVo all;
    private JobAmountVo baseline;
    private JobAmountVo custom;
    private int summary;
    private int maximumBatchJobNum;
    private int maximumStreamJobNum;
    private int maximumBatchScriptNum;

    public JobAmountVo getAll() {
        return all;
    }

    public void setAll(JobAmountVo all) {
        this.all = all;
    }

    public JobAmountVo getBaseline() {
        return baseline;
    }

    public void setBaseline(JobAmountVo baseline) {
        this.baseline = baseline;
    }

    public JobAmountVo getCustom() {
        return custom;
    }

    public void setCustom(JobAmountVo custom) {
        this.custom = custom;
    }

    public int getSummary() {
        return summary;
    }

    public void setSummary(int summary) {
        this.summary = summary;
    }

    public int getMaximumBatchJobNum() {
        return maximumBatchJobNum;
    }

    public void setMaximumBatchJobNum(int maximumBatchJobNum) {
        this.maximumBatchJobNum = maximumBatchJobNum;
    }

    public int getMaximumStreamJobNum() {
        return maximumStreamJobNum;
    }

    public void setMaximumStreamJobNum(int maximumStreamJobNum) {
        this.maximumStreamJobNum = maximumStreamJobNum;
    }

    public int getMaximumBatchScriptNum() {
        return maximumBatchScriptNum;
    }

    public void setMaximumBatchScriptNum(int maximumBatchScriptNum) {
        this.maximumBatchScriptNum = maximumBatchScriptNum;
    }
}