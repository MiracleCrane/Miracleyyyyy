/*
 * 文 件 名:  AlarmTaskFailStatistics.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/26
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.batch;

/**
 * 作业实例失败告警统计数据
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/26]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class TaskFailStat {
    private String jobId;
    private String jobName;
    private long taskFailCount;
    private String maxCreateDate;

    /**
     * 无参构造
     */
    public TaskFailStat() {
    }

    public TaskFailStat(String jobId, String jobName, long taskFailCount, String maxCreateDate) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.taskFailCount = taskFailCount;
        this.maxCreateDate = maxCreateDate;
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

    public long getTaskFailCount() {
        return taskFailCount;
    }

    public void setTaskFailCount(long taskFailCount) {
        this.taskFailCount = taskFailCount;
    }

    public String getMaxCreateDate() {
        return maxCreateDate;
    }

    public void setMaxCreateDate(String maxCreateDate) {
        this.maxCreateDate = maxCreateDate;
    }
}