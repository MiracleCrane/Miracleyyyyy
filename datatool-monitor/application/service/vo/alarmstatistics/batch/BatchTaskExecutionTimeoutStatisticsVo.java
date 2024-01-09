/*
 * 文 件 名:  BatchTaskExecutionTimeoutStatisticsVo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/25
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmstatistics.batch;

/**
 * 作业实例执行超时统计数据对象
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/25]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class BatchTaskExecutionTimeoutStatisticsVo {
    private String jobId;
    private String jobName;
    private long executionTimeoutCount;
    private long averageExecutionTime;
    private long maxExecutionTime;

    /**
     * 无参构造
     */
    public BatchTaskExecutionTimeoutStatisticsVo() {
    }

    public BatchTaskExecutionTimeoutStatisticsVo(String jobId, String jobName, long executionTimeoutCount,
            long averageExecutionTime, long maxExecutionTime) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.executionTimeoutCount = executionTimeoutCount;
        this.averageExecutionTime = averageExecutionTime;
        this.maxExecutionTime = maxExecutionTime;
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

    public long getExecutionTimeoutCount() {
        return executionTimeoutCount;
    }

    public void setExecutionTimeoutCount(long executionTimeoutCount) {
        this.executionTimeoutCount = executionTimeoutCount;
    }

    public long getAverageExecutionTime() {
        return averageExecutionTime;
    }

    public void setAverageExecutionTime(long averageExecutionTime) {
        this.averageExecutionTime = averageExecutionTime;
    }

    public long getMaxExecutionTime() {
        return maxExecutionTime;
    }

    public void setMaxExecutionTime(long maxExecutionTime) {
        this.maxExecutionTime = maxExecutionTime;
    }
}