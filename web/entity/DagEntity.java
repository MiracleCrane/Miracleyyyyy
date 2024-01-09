/*
 * 文 件 名:  DagEntity.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * dag
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/23]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Entity
@Table(name = "dag")
public class DagEntity {
    private String dagId;
    private Boolean isPaused;
    private Boolean isSubdag;
    private Boolean isActive;
    private Timestamp lastParsedTime;
    private Timestamp lastPickled;
    private Timestamp lastExpired;
    private Boolean schedulerLock;
    private Long pickleId;
    private String fileloc;
    private String processorSubdir;
    private String owners;
    private String description;
    private String defaultView;
    private String scheduleInterval;
    private String timetableDescription;
    private String rootDagId;
    private Integer maxActiveTasks;
    private Integer maxActiveRuns;
    private Timestamp nextDagrun;
    private Timestamp nextDagrunDataIntervalStart;
    private Timestamp nextDagrunDataIntervalEnd;
    private Timestamp nextDagrunCreateAfter;
    private Boolean hasTaskConcurrencyLimits;
    private Boolean hasImportErrors;

    @Id
    @Column(name = "dag_id")
    public String getDagId() {
        return dagId;
    }

    public void setDagId(String dagId) {
        this.dagId = dagId;
    }

    @Column(name = "is_paused")
    public Boolean getIsPaused() {
        return isPaused;
    }

    public void setIsPaused(Boolean isPaused) {
        this.isPaused = isPaused;
    }

    @Column(name = "is_subdag")
    public Boolean getIsSubdag() {
        return isSubdag;
    }

    public void setIsSubdag(Boolean isSubdag) {
        this.isSubdag = isSubdag;
    }

    @Column(name = "is_active")
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Column(name = "last_parsed_time")
    public Timestamp getLastParsedTime() {
        return lastParsedTime;
    }

    public void setLastParsedTime(Timestamp lastParsedTime) {
        this.lastParsedTime = lastParsedTime;
    }

    @Column(name = "last_pickled")
    public Timestamp getLastPickled() {
        return lastPickled;
    }

    public void setLastPickled(Timestamp lastPickled) {
        this.lastPickled = lastPickled;
    }

    @Column(name = "last_expired")
    public Timestamp getLastExpired() {
        return lastExpired;
    }

    public void setLastExpired(Timestamp lastExpired) {
        this.lastExpired = lastExpired;
    }

    @Column(name = "scheduler_lock")
    public Boolean getSchedulerLock() {
        return schedulerLock;
    }

    public void setSchedulerLock(Boolean schedulerLock) {
        this.schedulerLock = schedulerLock;
    }

    @Column(name = "pickle_id")
    public Long getPickleId() {
        return pickleId;
    }

    public void setPickleId(Long pickleId) {
        this.pickleId = pickleId;
    }

    @Column(name = "fileloc")
    public String getFileloc() {
        return fileloc;
    }

    public void setFileloc(String fileloc) {
        this.fileloc = fileloc;
    }

    @Column(name = "owners")
    public String getOwners() {
        return owners;
    }

    public void setOwners(String owners) {
        this.owners = owners;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "default_view")
    public String getDefaultView() {
        return defaultView;
    }

    public void setDefaultView(String defaultView) {
        this.defaultView = defaultView;
    }

    @Column(name = "schedule_interval")
    public String getScheduleInterval() {
        return scheduleInterval;
    }

    public void setScheduleInterval(String scheduleInterval) {
        this.scheduleInterval = scheduleInterval;
    }

    @Column(name = "root_dag_id")
    public String getRootDagId() {
        return rootDagId;
    }

    public void setRootDagId(String rootDagId) {
        this.rootDagId = rootDagId;
    }

    @Column(name = "next_dagrun")
    public Timestamp getNextDagrun() {
        return nextDagrun;
    }

    public void setNextDagrun(Timestamp nextDagrun) {
        this.nextDagrun = nextDagrun;
    }

    @Column(name = "next_dagrun_create_after")
    public Timestamp getNextDagrunCreateAfter() {
        return nextDagrunCreateAfter;
    }

    public void setNextDagrunCreateAfter(Timestamp nextDagrunCreateAfter) {
        this.nextDagrunCreateAfter = nextDagrunCreateAfter;
    }

    @Column(name = "has_task_concurrency_limits")
    public Boolean getHasTaskConcurrencyLimits() {
        return hasTaskConcurrencyLimits;
    }

    public void setHasTaskConcurrencyLimits(Boolean hasTaskConcurrencyLimits) {
        this.hasTaskConcurrencyLimits = hasTaskConcurrencyLimits;
    }

    @Column(name = "processor_subdir")
    public String getProcessorSubdir() {
        return processorSubdir;
    }

    public void setProcessorSubdir(String processorSubdir) {
        this.processorSubdir = processorSubdir;
    }

    @Column(name = "timetable_description")
    public String getTimetableDescription() {
        return timetableDescription;
    }

    public void setTimetableDescription(String timetableDescription) {
        this.timetableDescription = timetableDescription;
    }

    @Column(name = "max_active_tasks")
    public Integer getMaxActiveTasks() {
        return maxActiveTasks;
    }

    public void setMaxActiveTasks(Integer maxActiveTasks) {
        this.maxActiveTasks = maxActiveTasks;
    }

    @Column(name = "max_active_runs")
    public Integer getMaxActiveRuns() {
        return maxActiveRuns;
    }

    public void setMaxActiveRuns(Integer maxActiveRuns) {
        this.maxActiveRuns = maxActiveRuns;
    }

    @Column(name = "next_dagrun_data_interval_start")
    public Timestamp getNextDagrunDataIntervalStart() {
        return nextDagrunDataIntervalStart;
    }

    public void setNextDagrunDataIntervalStart(Timestamp nextDagrunDataIntervalStart) {
        this.nextDagrunDataIntervalStart = nextDagrunDataIntervalStart;
    }

    @Column(name = "next_dagrun_data_interval_end")
    public Timestamp getNextDagrunDataIntervalEnd() {
        return nextDagrunDataIntervalEnd;
    }

    public void setNextDagrunDataIntervalEnd(Timestamp nextDagrunDataIntervalEnd) {
        this.nextDagrunDataIntervalEnd = nextDagrunDataIntervalEnd;
    }

    @Column(name = "has_import_errors")
    public Boolean getHasImportErrors() {
        return hasImportErrors;
    }

    public void setHasImportErrors(Boolean hasImportErrors) {
        this.hasImportErrors = hasImportErrors;
    }

    @Override
    public String toString() {
        return "DagEntity{" + "dagId='" + dagId + '\'' + ", description='" + description + '\'' + '}';
    }
}