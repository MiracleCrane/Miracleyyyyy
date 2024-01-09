/*
 * 文 件 名:  AlarmStreamJobFailStatistics.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/26
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.stream;

import java.util.Date;

/**
 * 流作业失败告警统计数据
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/26]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class StreamJobFailStat {
    private String jobId;
    private String jobName;
    private long streamJobFailCount;
    private Date maxCreateDate;

    /**
     * 无参构造
     */
    public StreamJobFailStat() {
    }

    public StreamJobFailStat(String jobId, String jobName, long streamJobFailCount, Date maxCreateDate) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.streamJobFailCount = streamJobFailCount;
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

    public long getStreamJobFailCount() {
        return streamJobFailCount;
    }

    public void setStreamJobFailCount(long streamJobFailCount) {
        this.streamJobFailCount = streamJobFailCount;
    }

    public Date getMaxCreateDate() {
        return maxCreateDate;
    }

    public void setMaxCreateDate(Date maxCreateDate) {
        this.maxCreateDate = maxCreateDate;
    }
}