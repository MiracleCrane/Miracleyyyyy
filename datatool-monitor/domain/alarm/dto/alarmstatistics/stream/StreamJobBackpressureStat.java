/*
 * 文 件 名:  AlarmStreamJobBackpressureStatistics.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/26
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.stream;

/**
 * 流作业反压告警统计数据
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/26]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class StreamJobBackpressureStat {
    private String jobId;
    private String jobName;
    private long streamJobBackpressureCount;
    private Double avgRecoverTime;

    /**
     * 无参构造
     */
    public StreamJobBackpressureStat() {
    }

    public StreamJobBackpressureStat(String jobId, String jobName, long streamJobBackpressureCount,
                                     Double avgRecoverTime) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.streamJobBackpressureCount = streamJobBackpressureCount;
        this.avgRecoverTime = avgRecoverTime;
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

    public long getStreamJobBackpressureCount() {
        return streamJobBackpressureCount;
    }

    public void setStreamJobBackpressureCount(long streamJobBackpressureCount) {
        this.streamJobBackpressureCount = streamJobBackpressureCount;
    }

    public Double getAvgRecoverTime() {
        return avgRecoverTime;
    }

    public void setAvgRecoverTime(Double avgRecoverTime) {
        this.avgRecoverTime = avgRecoverTime;
    }
}