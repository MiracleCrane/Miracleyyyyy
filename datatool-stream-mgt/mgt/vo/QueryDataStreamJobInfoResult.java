/*
 * 文 件 名:  QueryDataStreamJobInfoResult.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  z00850154
 * 修改时间： 2023/8/21
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo;

import java.util.Date;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author z00850154
 * @version [Core&Link 23.1, 2023/8/21]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */

public class QueryDataStreamJobInfoResult {
    // 作业id
    private String id;

    // 作业名
    private String name;

    // 描述
    private String description;

    // flink sql 内容
    private String flinkSql;

    // flink作业最大并行数[ 1, 2, 3, 4 ]
    private short parallelism;

    // 重试次数[ 0, 5, 10, 20, 50, 100 ]
    private short retryTimes;

    // 是否开启checkpoint
    private boolean enableChk;

    // CheckPoint的模式，0代表AT_LEAST_ONCE，1代表EXACTLY_ONCE
    private short chkMode;

    // Checkpoint触发间隔
    private int chkInterval;

    // 两次检查点之间的最小时间间隔
    private int chkMinPause;

    // 检查点的超时时间，如果超时则放弃
    private int chkTimeout;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;

    public QueryDataStreamJobInfoResult() {
    }

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

    public boolean isEnableChk() {
        return enableChk;
    }

    public void setEnableChk(boolean enableChk) {
        this.enableChk = enableChk;
    }

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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
