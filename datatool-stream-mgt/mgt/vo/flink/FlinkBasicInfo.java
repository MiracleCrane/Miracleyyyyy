/*
 * 文 件 名:  FlinkBasicInfo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo.flink;

import com.huawei.smartcampus.datatool.enums.JobStatus;

import java.util.Date;

/**
 * flink基础信息
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */

public class FlinkBasicInfo {
    private JobStatus status;
    private Date startTime;
    private Date endTime;
    private Long duration;

    /**
     * 无参构造
     */
    public FlinkBasicInfo() {
    }

    public FlinkBasicInfo(JobStatus status, Date startTime, Date endTime, Long duration) {
        this.status = status;
        this.startTime = startTime != null ? new Date(startTime.getTime()) : null;
        this.endTime = endTime != null ? new Date(endTime.getTime()) : null;
        this.duration = duration;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public Date getStartTime() {
        return this.startTime != null ? new Date(this.startTime.getTime()) : null;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime != null ? new Date(startTime.getTime()) : null;
    }

    public Date getEndTime() {
        return this.endTime != null ? new Date(this.endTime.getTime()) : null;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime != null ? new Date(endTime.getTime()) : null;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
