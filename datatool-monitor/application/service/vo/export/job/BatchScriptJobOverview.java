/*
 * 文 件 名:  BatchScriptJobOverview.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job;

/**
 * 批处理脚本作业概览
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class BatchScriptJobOverview {
    private JobSummary jobSummary;
    private JobAmountClassification jobAmountClassification;
    private JobStateAmountClassification jobState;

    public JobSummary getJobSummary() {
        return jobSummary;
    }

    public void setJobSummary(JobSummary jobSummary) {
        this.jobSummary = jobSummary;
    }

    public JobAmountClassification getJobAmountClassification() {
        return jobAmountClassification;
    }

    public void setJobAmountClassification(JobAmountClassification jobAmountClassification) {
        this.jobAmountClassification = jobAmountClassification;
    }

    public JobStateAmountClassification getJobState() {
        return jobState;
    }

    public void setJobState(JobStateAmountClassification jobState) {
        this.jobState = jobState;
    }
}