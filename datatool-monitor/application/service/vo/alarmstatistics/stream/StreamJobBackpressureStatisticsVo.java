/*
 * 文 件 名:  StreamJobBackpressureStatisticsVo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/26
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmstatistics.stream;

/**
 * 流处理作业反压告警统计数据对象
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/26]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class StreamJobBackpressureStatisticsVo {
    private String jobId;
    private String jobName;
    private long backpressureCount;
    private Long averageRecoverTime;

    /**
     * 无参构造
     */
    public StreamJobBackpressureStatisticsVo() {
    }

    public StreamJobBackpressureStatisticsVo(String jobId, String jobName, long backpressureCount,
            Long averageRecoverTime) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.backpressureCount = backpressureCount;
        this.averageRecoverTime = averageRecoverTime;
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

    public long getBackpressureCount() {
        return backpressureCount;
    }

    public void setBackpressureCount(long backpressureCount) {
        this.backpressureCount = backpressureCount;
    }

    public Long getAverageRecoverTime() {
        return averageRecoverTime;
    }

    public void setAverageRecoverTime(Long averageRecoverTime) {
        this.averageRecoverTime = averageRecoverTime;
    }
}