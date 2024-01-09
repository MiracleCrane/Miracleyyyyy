/*
 * 文 件 名:  Cron.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/21
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.vo.job;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 作业调度周期
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/21]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class Cron {
    private String expression;
    private String periodDay;
    private String periodEndTime;
    private Integer periodInterval;
    private String periodStartTime;
    private String schedulePeriod;
    private Date startTime;
    private Date endTime;

    public Cron(String expression, String periodDay, String periodEndTime, Integer periodInterval, String periodStartTime,
            String schedulePeriod, Date startTime, Date endTime) {
        this.expression = expression;
        this.periodDay = periodDay;
        this.periodEndTime = periodEndTime;
        this.periodInterval = periodInterval;
        this.periodStartTime = periodStartTime;
        this.schedulePeriod = schedulePeriod;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @JSONField(name = "period_day")
    public String getPeriodDay() {
        return periodDay;
    }

    public void setPeriodDay(String periodDay) {
        this.periodDay = periodDay;
    }

    @JSONField(name = "period_endtime")
    public String getPeriodEndTime() {
        return periodEndTime;
    }

    public void setPeriodEndTime(String periodEndTime) {
        this.periodEndTime = periodEndTime;
    }

    @JSONField(name = "period_starttime")
    public String getPeriodStartTime() {
        return periodStartTime;
    }

    public void setPeriodStartTime(String periodStartTime) {
        this.periodStartTime = periodStartTime;
    }

    @JSONField(name = "schedule_period")
    public String getSchedulePeriod() {
        return schedulePeriod;
    }

    public void setSchedulePeriod(String schedulePeriod) {
        this.schedulePeriod = schedulePeriod;
    }

    @JSONField(name = "period_interval")
    public Integer getPeriodInterval() {
        return periodInterval;
    }

    public void setPeriodInterval(Integer periodInterval) {
        this.periodInterval = periodInterval;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}