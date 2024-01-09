/*
 * 文 件 名:  BatchJobDetail.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export;

/**
 * 批处理作业明细
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class BatchJobDetail {
    private String dir;
    private String name;
    private String origin;
    private String bundledScript;
    private String scheduleType;
    private String periodUnit;
    private String periodInterval;
    private String scheduleTimePerDay;
    private Boolean selfDependent;
    private String dependJobs;
    private Boolean running;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getBundledScript() {
        return bundledScript;
    }

    public void setBundledScript(String bundledScript) {
        this.bundledScript = bundledScript;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(String periodUnit) {
        this.periodUnit = periodUnit;
    }

    public String getPeriodInterval() {
        return periodInterval;
    }

    public void setPeriodInterval(String periodInterval) {
        this.periodInterval = periodInterval;
    }

    public String getScheduleTimePerDay() {
        return scheduleTimePerDay;
    }

    public void setScheduleTimePerDay(String scheduleTimePerDay) {
        this.scheduleTimePerDay = scheduleTimePerDay;
    }

    public Boolean isSelfDependent() {
        return selfDependent;
    }

    public void setSelfDependent(Boolean selfDependent) {
        this.selfDependent = selfDependent;
    }

    public String getDependJobs() {
        return dependJobs;
    }

    public void setDependJobs(String dependJobs) {
        this.dependJobs = dependJobs;
    }

    public Boolean isRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }
}