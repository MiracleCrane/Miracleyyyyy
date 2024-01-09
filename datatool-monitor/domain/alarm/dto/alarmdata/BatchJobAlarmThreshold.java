/*
 * 文 件 名:  BatchJobAlarmThreshold.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/24
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata;

/**
 * 批处理作业告警阈值
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/24]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class BatchJobAlarmThreshold {
    private String jobId;
    private Long alarmThreshold;

    /**
     * 无参构造
     */
    public BatchJobAlarmThreshold() {
    }

    public BatchJobAlarmThreshold(String jobId, Long alarmThreshold) {
        this.jobId = jobId;
        this.alarmThreshold = alarmThreshold;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Long getAlarmThreshold() {
        return alarmThreshold;
    }

    public void setAlarmThreshold(Long alarmThreshold) {
        this.alarmThreshold = alarmThreshold;
    }
}