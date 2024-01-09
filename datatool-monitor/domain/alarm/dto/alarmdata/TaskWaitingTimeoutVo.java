/*
 * 文 件 名:  TaskWaitingTimeoutVo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata;

import java.util.Date;

/**
 * 作业等待超时
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/10]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class TaskWaitingTimeoutVo {
    private String jobId;
    private String name;
    private Date taskStartDate;
    private Date dagRunStartDate;

    /**
     * 无参构造
     */
    public TaskWaitingTimeoutVo() {
    }

    public TaskWaitingTimeoutVo(String jobId, String name, Date taskStartDate, Date dagRunStartDate) {
        this.jobId = jobId;
        this.name = name;
        this.taskStartDate = taskStartDate;
        this.dagRunStartDate = dagRunStartDate;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(Date taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public Date getDagRunStartDate() {
        return dagRunStartDate;
    }

    public void setDagRunStartDate(Date dagRunStartDate) {
        this.dagRunStartDate = dagRunStartDate;
    }
}