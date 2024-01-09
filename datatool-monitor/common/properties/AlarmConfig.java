/*
 * 文 件 名:  AlarmConfig.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.common.properties;

import com.huawei.smartcampus.datatool.properties.ApplicationProperties;

/**
 * alarm config
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class AlarmConfig {
    private static final ApplicationProperties PROPERTIES = ApplicationProperties.instance();

    private static final String DATA_ALL_ALARM_ENABLE = "data.all.alarm.enable";

    private static final String DATA_BATCH_TASK_EXECUTION_TIMEOUT_ENABLE = "data.batch.task.execution.timeout.alarm.enable";
    private static final String DATA_BATCH_TASK_EXECUTION_TIMEOUT_INTERVAL = "data.batch.task.execution.timeout.alarm.interval";
    private static final String DATA_BATCH_TASK_EXECUTION_TIMEOUT_THRESHOLD = "data.batch.task.execution.timeout.alarm.threshold";

    private static final String DATA_BATCH_TASK_WAITING_TIMEOUT_ENABLE = "data.batch.task.waiting.timeout.alarm.enable";
    private static final String DATA_BATCH_TASK_WAITING_TIMEOUT_INTERVAL = "data.batch.task.waiting.timeout.alarm.interval";
    private static final String DATA_BATCH_TASK_WAITING_TIMEOUT_THRESHOLD = "data.batch.task.waiting.timeout.alarm.threshold";

    private static final String DATA_BATCH_TASK_QUEUED_TIMEOUT_ENABLE = "data.batch.task.queued.timeout.alarm.enable";
    private static final String DATA_BATCH_TASK_QUEUED_TIMEOUT_INTERVAL = "data.batch.task.queued.timeout.alarm.interval";
    private static final String DATA_BATCH_TASK_QUEUED_TIMEOUT_THRESHOLD = "data.batch.task.queued.timeout.alarm.threshold";

    private static final String DATA_BATCH_TASK_FAIL_ENABLE = "data.batch.task.fail.alarm.enable";
    private static final String DATA_BATCH_TASK_FAIL_INTERVAL = "data.batch.task.fail.alarm.interval";

    private static final String DATA_STREAM_JOB_FAIL_ENABLE = "data.stream.job.fail.enable";
    private static final String DATA_STREAM_JOB_FAIL_INTERVAL = "data.stream.job.fail.interval";

    private static final String DATA_STREAM_JOB_BACKPRESSURE_ENABLE = "data.stream.job.backpressure.enable";
    private static final String DATA_STREAM_JOB_BACKPRESSURE_INTERVAL = "data.stream.job.backpressure.interval";

    public static boolean dataAllAlarmEnable() {
        return PROPERTIES.getBoolean(DATA_ALL_ALARM_ENABLE);
    }

    public static boolean dataBatchTaskExecutionTimeoutEnable() {
        return PROPERTIES.getBoolean(DATA_BATCH_TASK_EXECUTION_TIMEOUT_ENABLE);
    }

    public static int dataBatchTaskExecutionTimeoutInterval() {
        return PROPERTIES.getInt(DATA_BATCH_TASK_EXECUTION_TIMEOUT_INTERVAL);
    }

    public static int dataBatchTaskExecutionTimeoutThreshold() {
        return PROPERTIES.getInt(DATA_BATCH_TASK_EXECUTION_TIMEOUT_THRESHOLD);
    }

    public static boolean dataBatchTaskWaitingTimeoutEnable() {
        return PROPERTIES.getBoolean(DATA_BATCH_TASK_WAITING_TIMEOUT_ENABLE);
    }

    public static int dataBatchTaskWaitingTimeoutInterval() {
        return PROPERTIES.getInt(DATA_BATCH_TASK_WAITING_TIMEOUT_INTERVAL);
    }

    public static int dataBatchTaskWaitingTimeoutThreshold() {
        return PROPERTIES.getInt(DATA_BATCH_TASK_WAITING_TIMEOUT_THRESHOLD);
    }

    public static boolean dataBatchTaskQueuedTimeoutEnable() {
        return PROPERTIES.getBoolean(DATA_BATCH_TASK_QUEUED_TIMEOUT_ENABLE);
    }

    public static int dataBatchTaskQueuedTimeoutInterval() {
        return PROPERTIES.getInt(DATA_BATCH_TASK_QUEUED_TIMEOUT_INTERVAL);
    }

    public static int dataBatchTaskQueuedTimeoutThreshold() {
        return PROPERTIES.getInt(DATA_BATCH_TASK_QUEUED_TIMEOUT_THRESHOLD);
    }

    public static boolean dataBatchTaskFailEnable() {
        return PROPERTIES.getBoolean(DATA_BATCH_TASK_FAIL_ENABLE);
    }

    public static int dataBatchTaskFailInterval() {
        return PROPERTIES.getInt(DATA_BATCH_TASK_FAIL_INTERVAL);
    }

    public static boolean dataStreamJobFailEnable() {
        return PROPERTIES.getBoolean(DATA_STREAM_JOB_FAIL_ENABLE);
    }

    public static int dataStreamJobFailInterval() {
        return PROPERTIES.getInt(DATA_STREAM_JOB_FAIL_INTERVAL);
    }

    public static boolean dataStreamJobBackpressureEnable() {
        return PROPERTIES.getBoolean(DATA_STREAM_JOB_BACKPRESSURE_ENABLE);
    }

    public static int dataStreamJobBackpressureInterval() {
        return PROPERTIES.getInt(DATA_STREAM_JOB_BACKPRESSURE_INTERVAL);
    }
}