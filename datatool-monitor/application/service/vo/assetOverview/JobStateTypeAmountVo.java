/*
 * 文 件 名:  JobStateTypeAmountVo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/17
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview;

/**
 * 作业状态类型数量对象
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class JobStateTypeAmountVo {
    private JobStateAmountVo batchJob;
    private JobStateAmountVo streamJob;

    public JobStateTypeAmountVo(JobStateAmountVo batchJob, JobStateAmountVo streamJob) {
        this.batchJob = batchJob;
        this.streamJob = streamJob;
    }

    public JobStateAmountVo getBatchJob() {
        return batchJob;
    }

    public void setBatchJob(JobStateAmountVo batchJob) {
        this.batchJob = batchJob;
    }

    public JobStateAmountVo getStreamJob() {
        return streamJob;
    }

    public void setStreamJob(JobStateAmountVo streamJob) {
        this.streamJob = streamJob;
    }
}