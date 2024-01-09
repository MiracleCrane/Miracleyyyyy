/*
 * 文 件 名:  BatchTaskWaitingTimeoutStatisticsVo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/26
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmstatistics.batch;

/**
 * 作业实例等待超时告警统计数据对象
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/26]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class BatchTaskWaitingTimeoutStatisticsVo {
    private String jobId;
    private String jobName;
    private long waitingTimeoutCount;
    private long averageWaitingTime;
    private long maxWaitingTime;

    /**
     * 无参构造
     */
    public BatchTaskWaitingTimeoutStatisticsVo() {
    }

    public BatchTaskWaitingTimeoutStatisticsVo(String jobId, String jobName, long waitingTimeoutCount,
            long averageWaitingTime, long maxWaitingTime) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.waitingTimeoutCount = waitingTimeoutCount;
        this.averageWaitingTime = averageWaitingTime;
        this.maxWaitingTime = maxWaitingTime;
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

    public long getWaitingTimeoutCount() {
        return waitingTimeoutCount;
    }

    public void setWaitingTimeoutCount(long waitingTimeoutCount) {
        this.waitingTimeoutCount = waitingTimeoutCount;
    }

    public long getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public void setAverageWaitingTime(long averageWaitingTime) {
        this.averageWaitingTime = averageWaitingTime;
    }

    public long getMaxWaitingTime() {
        return maxWaitingTime;
    }

    public void setMaxWaitingTime(long maxWaitingTime) {
        this.maxWaitingTime = maxWaitingTime;
    }
}