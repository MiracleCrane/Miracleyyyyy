/*
 * 文 件 名:  JobStateAmountClassification.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/12/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job;

/**
 * 作业状态数量汇总
 *
 * @author j00826364
 * @version [Core&Link 23.1, 2023/12/1]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class JobStateAmountClassification {
    private JobStateAmount batchJob;
    private JobStateAmount streamJob;
    private JobStateAmount total;

    public JobStateAmount getBatchJob() {
        return batchJob;
    }

    public void setBatchJob(JobStateAmount batchJob) {
        this.batchJob = batchJob;
    }

    public JobStateAmount getStreamJob() {
        return streamJob;
    }

    public void setStreamJob(JobStateAmount streamJob) {
        this.streamJob = streamJob;
    }

    public JobStateAmount getTotal() {
        return total;
    }

    public void setTotal(JobStateAmount total) {
        this.total = total;
    }
}