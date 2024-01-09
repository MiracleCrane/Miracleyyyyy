/*
 * 文 件 名:  AlarmFactory.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmType;

/**
 * 告警规则工厂类
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/2]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class AlarmRuleFactory {
    public static AlarmRule create(AlarmType type) {
        switch (type) {
            case BATCH_TASK_FAIL:
                return new BatchTaskFailAlarmRule();
            case BATCH_TASK_EXECUTION_TIMEOUT:
                return new BatchTaskExecutionTimeoutAlarmRule();
            case BATCH_TASK_WAITING_TIMEOUT:
                return new BatchTaskWaitingTimeoutAlarmRule();
            case BATCH_TASK_QUEUED_TIMEOUT:
                return new BatchTaskQueuedTimeoutAlarmRule();
            case STREAM_JOB_FAIL:
                return new StreamJobFailAlarmRule();
            case STREAM_JOB_BACKPRESSURE:
                return new StreamJobBackpressureAlarmRule();
            default:
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ALARM_TYPE_NOT_SUPPORT, type.name());
        }
    }
}