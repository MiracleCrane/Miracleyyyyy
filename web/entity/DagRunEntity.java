/*
 * 文 件 名:  DagRunEntity.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/23
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * dag run entity
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/23]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Entity
@Table(name = "dag_run")
public class DagRunEntity implements Serializable {
    private static final long serialVersionUID = -3847991809863795497L;
    private Integer id;
    private String dagId;
    private Timestamp queuedAt;
    private Timestamp executionDate;
    private String state;
    private String runId;
    private Boolean externalTrigger;
    private Byte[] conf;
    private Timestamp endDate;
    private Timestamp startDate;
    private String runType;
    private Timestamp lastScheduleDecision;
    private String dagHash;
    private Integer creatingJobId;
    private Timestamp dataIntervalStart;
    private Timestamp dataIntervalEnd;
    private Integer logTemplateId;
    private Timestamp updatedAt;
    private DtBatchJobEntity dtBatchJobEntity;

    @ManyToOne(targetEntity = DtBatchJobEntity.class)
    @JoinColumn(name = "dag_id", referencedColumnName = "id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    @Fetch(FetchMode.JOIN)
    public DtBatchJobEntity getDtBatchJobEntity() {
        return dtBatchJobEntity;
    }

    public void setDtBatchJobEntity(DtBatchJobEntity dtBatchJobEntity) {
        this.dtBatchJobEntity = dtBatchJobEntity;
    }

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "dag_id")
    public String getDagId() {
        return dagId;
    }

    public void setDagId(String dagId) {
        this.dagId = dagId;
    }

    @Column(name = "queued_at")
    public Timestamp getQueuedAt() {
        return queuedAt;
    }

    public void setQueuedAt(Timestamp queuedAt) {
        this.queuedAt = queuedAt;
    }

    @Column(name = "execution_date")
    public Timestamp getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Timestamp executionDate) {
        this.executionDate = executionDate;
    }

    @Column(name = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "run_id")
    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    @Column(name = "external_trigger")
    public Boolean getExternalTrigger() {
        return externalTrigger;
    }

    public void setExternalTrigger(Boolean externalTrigger) {
        this.externalTrigger = externalTrigger;
    }

    @Column(name = "conf")
    public Byte[] getConf() {
        return conf;
    }

    public void setConf(Byte[] conf) {
        this.conf = conf;
    }

    @Column(name = "end_date")
    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Column(name = "start_date")
    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    @Column(name = "run_type")
    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    @Column(name = "last_scheduling_decision")
    public Timestamp getLastScheduleDecision() {
        return lastScheduleDecision;
    }

    public void setLastScheduleDecision(Timestamp lastScheduleDecision) {
        this.lastScheduleDecision = lastScheduleDecision;
    }

    @Column(name = "dag_hash")
    public String getDagHash() {
        return dagHash;
    }

    public void setDagHash(String dagHash) {
        this.dagHash = dagHash;
    }

    @Column(name = "creating_job_id")
    public Integer getCreatingJobId() {
        return creatingJobId;
    }

    public void setCreatingJobId(Integer creatingJobId) {
        this.creatingJobId = creatingJobId;
    }

    @Column(name = "data_interval_start")
    public Timestamp getDataIntervalStart() {
        return dataIntervalStart;
    }

    public void setDataIntervalStart(Timestamp dataIntervalStart) {
        this.dataIntervalStart = dataIntervalStart;
    }

    @Column(name = "data_interval_end")
    public Timestamp getDataIntervalEnd() {
        return dataIntervalEnd;
    }

    public void setDataIntervalEnd(Timestamp dataIntervalEnd) {
        this.dataIntervalEnd = dataIntervalEnd;
    }

    @Column(name = "log_template_id")
    public Integer getLogTemplateId() {
        return logTemplateId;
    }

    public void setLogTemplateId(Integer logTemplateId) {
        this.logTemplateId = logTemplateId;
    }

    @Column(name = "updated_at")
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "DagRunEntity{" + "id='" + id + '\'' + ", dagId='" + dagId + '\'' + ", runId='" + runId + '\'' + '}';
    }
}