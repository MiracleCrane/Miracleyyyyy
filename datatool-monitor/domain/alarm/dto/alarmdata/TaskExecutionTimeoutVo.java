/*
 * 文 件 名:  TaskExecutionTimeoutVo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/24
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata;

/**
 * 作业执行超时
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/24]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class TaskExecutionTimeoutVo {
    private String jobId;
    private String jobName;
    private double duration;

    /**
     * 无参构造
     */
    public TaskExecutionTimeoutVo() {
    }

    public TaskExecutionTimeoutVo(String jobId, String jobName, double duration) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.duration = duration;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}