/*
 * 文 件 名:  AlarmDataDetailInfo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmdata;

/**
 * 告警数据详情信息
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/16]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class AlarmDataDetailInfo {
    private String jobType;
    private String alarmType;
    private String createDate;
    private String jobId;
    private String jobName;

    /**
     * 无参构造
     */
    public AlarmDataDetailInfo() {
    }

    public AlarmDataDetailInfo(String jobType, String alarmType, String createDate, String jobId, String jobName) {
        this.jobType = jobType;
        this.alarmType = alarmType;
        this.createDate = createDate;
        this.jobId = jobId;
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    @Override
    public String toString() {
        return "AlarmDataDetailInfo{" + "jobId: " + jobId + ", jobName: " + jobName + ", jobType: " + jobType
                + ", alarmType: " + alarmType + ", createDate: " + createDate + "}";
    }
}