/*
 * 文 件 名:  StreamJobEntity.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 流处理作业表
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Entity
@Table(name = "dt_stream_job")
@EntityListeners(AuditingEntityListener.class)
public class StreamJobEntity {
    private String id;
    private String name;
    private String description;
    private String flinkSql;
    private short parallelism;
    private short retryTimes;
    private String state;
    private boolean enableChk;
    private short chkMode;
    private int chkInterval;
    private int chkMinPause;
    private int chkTimeout;
    private String savepointPath;
    private String requestId;
    private String flinkId;
    private String createdBy;
    private Date createdDate;
    private Date lastModifiedDate;
    private String lastModifiedBy;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @CreatedBy
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getFlinkSql() {
        return flinkSql;
    }

    public void setFlinkSql(String flinkSql) {
        this.flinkSql = flinkSql;
    }

    public short getParallelism() {
        return parallelism;
    }

    public void setParallelism(short parallelism) {
        this.parallelism = parallelism;
    }

    public short getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(short retryTimes) {
        this.retryTimes = retryTimes;
    }

    @Column(name = "created_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    @CreatedDate
    public Date getCreatedDate() {
        return this.createdDate != null ? new Date(this.createdDate.getTime()) : null;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate != null ? new Date(createdDate.getTime()) : null;
    }

    @Column(name = "last_modified_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @LastModifiedDate
    public Date getLastModifiedDate() {
        return this.lastModifiedDate != null ? new Date(this.lastModifiedDate.getTime()) : null;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate != null ? new Date(lastModifiedDate.getTime()) : null;
    }

    @LastModifiedBy
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Column(name = "enable_chk")
    public boolean isEnableChk() {
        return enableChk;
    }

    public void setEnableChk(boolean enableChk) {
        this.enableChk = enableChk;
    }

    @Column(name = "chk_mode")
    public short getChkMode() {
        return chkMode;
    }

    public void setChkMode(short chkMode) {
        this.chkMode = chkMode;
    }

    public int getChkInterval() {
        return chkInterval;
    }

    public void setChkInterval(int chkInterval) {
        this.chkInterval = chkInterval;
    }

    public int getChkMinPause() {
        return chkMinPause;
    }

    public void setChkMinPause(int chkMinPause) {
        this.chkMinPause = chkMinPause;
    }

    public int getChkTimeout() {
        return chkTimeout;
    }

    public void setChkTimeout(int chkTimeout) {
        this.chkTimeout = chkTimeout;
    }

    @Column(name = "save_point_location")
    public String getSavepointPath() {
        return savepointPath;
    }

    public void setSavepointPath(String savepointPath) {
        this.savepointPath = savepointPath;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getFlinkId() {
        return flinkId;
    }

    public void setFlinkId(String flinkId) {
        this.flinkId = flinkId;
    }

    @Override
    public String toString() {
        return "StreamJobEntity{" + "id='" + id + '\'' + ", name='" + name + '\'' + '}';
    }
}
