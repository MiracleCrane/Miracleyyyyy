/*
 * 文 件 名:  AlarmType.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm;

/**
 * 告警类型
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/1]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public enum AlarmType {
    BATCH_TASK_FAIL("106004", "dataBatchTaskFail"),
    BATCH_TASK_EXECUTION_TIMEOUT("106031", "dataBatchTaskExecutionTimeout"),
    BATCH_TASK_WAITING_TIMEOUT("106006", "dataBatchTaskWaitingTimeout"),
    BATCH_TASK_QUEUED_TIMEOUT("106003", "dataBatchQueuedTimeout"),
    STREAM_JOB_FAIL("106018", "dataStreamJobFail"),
    STREAM_JOB_BACKPRESSURE("106032", "dataStreamJobBackpressure");

    private final String alarmId;
    private final String alarmName;

    AlarmType(String alarmId, String alarmName) {
        this.alarmId = alarmId;
        this.alarmName = alarmName;
    }

    public String alarmId() {
        return alarmId;
    }

    public String alarmName() {
        return alarmName;
    }

    public static String getAlarmNameByAlarmId(String alarmId) {
        if (alarmId == null) {
            return "";
        }
        for (AlarmType alarmType : AlarmType.values()) {
            if (alarmType.alarmId.equals(alarmId)) {
                return alarmType.alarmName;
            }
        }
        return "";
    }

    public static AlarmType getAlarmTypeByAlarmId(String alarmId) {
        if (alarmId == null) {
            return null;
        }
        for (AlarmType alarmType : AlarmType.values()) {
            if (alarmType.alarmId.equals(alarmId)) {
                return alarmType;
            }
        }
        return null;
    }

    public static String getAlarmIdByAlarmName(String alarmName) {
        if (alarmName == null) {
            return null;
        }
        for (AlarmType alarmType : AlarmType.values()) {
            if (alarmType.alarmName.equals(alarmName)) {
                return alarmType.alarmId;
            }
        }
        return null;
    }
}