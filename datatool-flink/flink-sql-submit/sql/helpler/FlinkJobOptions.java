/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

package com.huawei.dataservice.sql.helpler;

import org.apache.flink.streaming.api.CheckpointingMode;

/**
 * Flink作业启动选型实体类
 *
 * @author w00318695
 * @version [SmartCampus V100R001C00, 2021/3/30]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class FlinkJobOptions {
    /**
     * Flink作业名称
     */
    private String jobName;

    /**
     * 作业尝试重启次数
     */
    private int restartAttempts;

    /**
     * 作业重启间隔,单位秒
     */
    private long restartDelayInterval;

    /**
     * 是否开启checkpoint
     */
    private boolean enableCheckPoint;

    /**
     * Checkpoint触发间隔，单位毫秒
     */
    private long checkpointInterval;

    /**
     * Checkpoint模式：至少一次、精确一次
     */
    private CheckpointingMode checkpointingMode;

    /**
     * 两次CheckPoint之间的最小时间间隔，单位毫秒
     */
    private long minPauseBetweenCheckpoints;

    /**
     * CheckPoint的超时时间，单位毫秒
     */
    private long checkpointTimeout;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getRestartAttempts() {
        return restartAttempts;
    }

    public void setRestartAttempts(int restartAttempts) {
        this.restartAttempts = restartAttempts;
    }

    public long getRestartDelayInterval() {
        return restartDelayInterval;
    }

    public void setRestartDelayInterval(long restartDelayInterval) {
        this.restartDelayInterval = restartDelayInterval;
    }

    public boolean isEnableCheckPoint() {
        return enableCheckPoint;
    }

    public void setEnableCheckPoint(boolean enableCheckPoint) {
        this.enableCheckPoint = enableCheckPoint;
    }

    public long getCheckpointInterval() {
        return checkpointInterval;
    }

    public void setCheckpointInterval(long checkpointInterval) {
        this.checkpointInterval = checkpointInterval;
    }

    public CheckpointingMode getCheckpointingMode() {
        return checkpointingMode;
    }

    public void setCheckpointingMode(CheckpointingMode checkpointingMode) {
        this.checkpointingMode = checkpointingMode;
    }

    public long getMinPauseBetweenCheckpoints() {
        return minPauseBetweenCheckpoints;
    }

    public void setMinPauseBetweenCheckpoints(long minPauseBetweenCheckpoints) {
        this.minPauseBetweenCheckpoints = minPauseBetweenCheckpoints;
    }

    public long getCheckpointTimeout() {
        return checkpointTimeout;
    }

    public void setCheckpointTimeout(long checkpointTimeout) {
        this.checkpointTimeout = checkpointTimeout;
    }
}
