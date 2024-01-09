/*
 * 文 件 名:  CreateStreamJobReq.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  z00850154
 * 修改时间： 2023/8/25
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo;

import com.huawei.smartcampus.datatool.validation.constraints.ChooseOneShort;
import com.huawei.smartcampus.datatool.validation.groups.DataToolGroup;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

/**
 * <用于保存/更新的作业信息>
 * <创建/更新jobs模型>
 *
 * @author z00850154
 * @version [Core&Link 23.1, 2023/8/25]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class StreamJobReq {
    // 作业名
    // 控制校验器顺序
    @Pattern(regexp = "^[\\w\\-]{1,57}$", message = "{DATATOOL_ILLEGAL_STREAM_NAME}", groups = DataToolGroup.Second.class)
    @NotBlank(message = "{DATATOOL_STREAM_JOB_NAME_EMPTY}", groups = Default.class)
    private String name;

    // 描述
    @Length(max = 512, message = "{DATATOOL_STREAM_JOB_DESCRIPTION_TOO_LONG}")
    private String description;

    // flink sql 内容
    @NotNull(message = "{DATATOOL_STREAM_JOB_FLINKSQL_MISSING}")
    private String flinkSql;

    // flink作业最大并行数
    @ChooseOneShort(value = {1, 2, 3, 4}, message = "{DATATOOL_STREAM_JOB_PARALLELISM_INVALID}")
    private short parallelism;

    // 重试次数 [ 0, 5, 10, 20, 50, 100 ]
    @ChooseOneShort(value = {0, 5, 10, 20, 50, 100}, message = "{DATATOOL_STREAM_JOB_RETRY_TIMES_INVALID}")
    private short retryTimes;

    // 是否开启checkpoint
    private boolean enableChk;

    // CheckPoint的模式，0代表AT_LEAST_ONCE，1代表EXACTLY_ONCE
    @ChooseOneShort(value = {0, 1}, message = "{DATATOOL_STREAM_JOB_CHKMODE_INVALID}")
    private short chkMode;

    // Checkpoint触发间隔，单位ms
    @Range(min = 0, max = 2_140_000_000, message = "{DATATOOL_STREAM_JOB_CHKINTERVAL_INVALID}")
    private int chkInterval;

    // 两次检查点之间的最小时间间隔，前端传的单位是ms
    @Range(min = 0, max = 2_140_000_000, message = "{DATATOOL_STREAM_JOB_CHKMINPAUSE_INVALID}")
    private int chkMinPause;

    // 检查点的超时时间，如果超时则放弃，单位ms
    @Range(min = 0, max = 2_140_000_000, message = "{DATATOOL_STREAM_JOB_CHKTIMEOUT_INVALID}")
    private int chkTimeout;

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

    @Override
    public String toString() {
        return "CreateStreamJobReq{" + "name='" + name + '\'' + '}';
    }
}
