/*
 * 文 件 名:  TaskInstance.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/21
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * task instance entity
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/21]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Entity
@IdClass(TaskInstance.class)
@Table(name = "task_instance")
public class TaskInstanceEntity implements Serializable {
    private static final long serialVersionUID = -5648432034370601169L;
    private String taskId;
    private String dagId;
    private String runId;
    private Integer mapIndex;
    private Timestamp startDate;
    private Timestamp endDate;
    private Double duration;
    private String state;
    private Integer tryNumber;
    private String hostname;
    private String unixname;
    private Integer jobId;
    private String pool;
    private String queue;
    private Integer priorityWeight;
    private String operator;
    private Timestamp queuedDttm;
    private Integer pid;
    private Integer maxTries;
    private byte[] executorConfig;
    private Integer poolSlots;
    private Integer queuedByJobId;
    private String externalExecutorId;
    private Timestamp updatedAt;
    private Integer triggerId;
    private Timestamp triggerTimeout;
    private String nextMethod;
    private String nextKwargs;
    private DagEntity dagEntity;
    private DagRunEntity dagRunEntity;
    private DtBatchJobNodeEntity dtBatchJobNodeEntity;
    private DtBatchJobEntity dtBatchJobEntity;

    @ManyToOne(targetEntity = DagEntity.class)
    @JoinColumn(name = "dag_id", referencedColumnName = "dag_id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    @Fetch(FetchMode.JOIN)
    public DagEntity getDagEntity() {
        return dagEntity;
    }

    @ManyToOne(targetEntity = DagRunEntity.class)
    @JoinColumns(value = {
            @JoinColumn(name = "dag_id", referencedColumnName = "dag_id", insertable = false, updatable = false),
            @JoinColumn(name = "run_id", referencedColumnName = "run_id", insertable = false, updatable = false)})
    @NotFound(action = NotFoundAction.IGNORE)
    @Fetch(FetchMode.JOIN)
    public DagRunEntity getDagRunEntity() {
        return dagRunEntity;
    }

    @ManyToOne(targetEntity = DtBatchJobNodeEntity.class)
    @JoinColumn(name = "task_id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    @Fetch(FetchMode.JOIN)
    public DtBatchJobNodeEntity getDtBatchJobNodeEntity() {
        return dtBatchJobNodeEntity;
    }

    @ManyToOne(targetEntity = DtBatchJobEntity.class)
    @JoinColumn(name = "dag_id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    @Fetch(FetchMode.JOIN)
    public DtBatchJobEntity getDtBatchJobEntity() {
        return dtBatchJobEntity;
    }

    public void setDagEntity(DagEntity dagEntity) {
        this.dagEntity = dagEntity;
    }

    public void setDagRunEntity(DagRunEntity dagRunEntity) {
        this.dagRunEntity = dagRunEntity;
    }

    public void setDtBatchJobNodeEntity(DtBatchJobNodeEntity dtBatchJobNodeEntity) {
        this.dtBatchJobNodeEntity = dtBatchJobNodeEntity;
    }

    public void setDtBatchJobEntity(DtBatchJobEntity dtBatchJobEntity) {
        this.dtBatchJobEntity = dtBatchJobEntity;
    }

    @Id
    @Column(name = "task_id")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Id
    @Column(name = "dag_id")
    public String getDagId() {
        return dagId;
    }

    public void setDagId(String dagId) {
        this.dagId = dagId;
    }

    @Id
    @Column(name = "run_id")
    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    @Id
    @Column(name = "map_index")
    public Integer getMapIndex() {
        return mapIndex;
    }

    public void setMapIndex(Integer mapIndex) {
        this.mapIndex = mapIndex;
    }

    @Column(name = "start_date")
    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    @Column(name = "end_date")
    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Column(name = "duration")
    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    @Column(name = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "try_number")
    public Integer getTryNumber() {
        return tryNumber;
    }

    public void setTryNumber(Integer tryNumber) {
        this.tryNumber = tryNumber;
    }

    @Column(name = "hostname")
    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Column(name = "unixname")
    public String getUnixname() {
        return unixname;
    }

    public void setUnixname(String unixname) {
        this.unixname = unixname;
    }

    @Column(name = "job_id")
    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    @Column(name = "pool")
    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    @Column(name = "queue")
    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    @Column(name = "priority_weight")
    public Integer getPriorityWeight() {
        return priorityWeight;
    }

    public void setPriorityWeight(Integer priorityWeight) {
        this.priorityWeight = priorityWeight;
    }

    @Column(name = "operator")
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name = "queued_dttm")
    public Timestamp getQueuedDttm() {
        return queuedDttm;
    }

    public void setQueuedDttm(Timestamp queuedDttm) {
        this.queuedDttm = queuedDttm;
    }

    @Column(name = "pid")
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    @Column(name = "max_tries")
    public Integer getMaxTries() {
        return maxTries;
    }

    public void setMaxTries(Integer maxTries) {
        this.maxTries = maxTries;
    }

    @Column(name = "executor_config")
    public byte[] getExecutorConfig() {
        return executorConfig;
    }

    public void setExecutorConfig(byte[] executorConfig) {
        this.executorConfig = executorConfig;
    }

    @Column(name = "pool_slots")
    public Integer getPoolSlots() {
        return poolSlots;
    }

    public void setPoolSlots(Integer poolSlots) {
        this.poolSlots = poolSlots;
    }

    @Column(name = "queued_by_job_id")
    public Integer getQueuedByJobId() {
        return queuedByJobId;
    }

    public void setQueuedByJobId(Integer queuedByJobId) {
        this.queuedByJobId = queuedByJobId;
    }

    @Column(name = "external_executor_id")
    public String getExternalExecutorId() {
        return externalExecutorId;
    }

    public void setExternalExecutorId(String externalExecutorId) {
        this.externalExecutorId = externalExecutorId;
    }

    @Column(name = "updated_at")
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Column(name = "trigger_id")
    public Integer getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(Integer triggerId) {
        this.triggerId = triggerId;
    }

    @Column(name = "trigger_timeout")
    public Timestamp getTriggerTimeout() {
        return triggerTimeout;
    }

    public void setTriggerTimeout(Timestamp triggerTimeout) {
        this.triggerTimeout = triggerTimeout;
    }

    @Column(name = "next_method")
    public String getNextMethod() {
        return nextMethod;
    }

    public void setNextMethod(String nextMethod) {
        this.nextMethod = nextMethod;
    }

    @Column(name = "next_kwargs", columnDefinition = "json")
    public String getNextKwargs() {
        return nextKwargs;
    }

    public void setNextKwargs(String nextKwargs) {
        this.nextKwargs = nextKwargs;
    }

    @Override
    public String toString() {
        return "TaskInstanceEntity{" + "taskId='" + taskId + '\'' + ", dagId='" + dagId + '\'' + ", runId='" + runId
                + '\'' + ", tryNumber='" + tryNumber + '\'' + ", priorityWeight='" + priorityWeight + '\'' + '}';
    }
}