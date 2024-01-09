/*
 * 文 件 名:  ZabbixAlarmMessageFormatterFactory.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/6/6
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.alarmcenter.zabbix;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmType;

/**
 * zabbix平台告警信息格式化工具工厂类
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/6/6]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class ZabbixAlarmMessageFormatterFactory {
    public static ZabbixAlarmMessageFormatter create(AlarmType alarmType) {
        switch (alarmType) {
            case BATCH_TASK_FAIL:
            case BATCH_TASK_EXECUTION_TIMEOUT:
            case BATCH_TASK_WAITING_TIMEOUT:
            case BATCH_TASK_QUEUED_TIMEOUT:
                return new ZabbixDataBatchAlarmMessageFormatter();
            case STREAM_JOB_FAIL:
            case STREAM_JOB_BACKPRESSURE:
                return new ZabbixDataStreamAlarmMessageFormatter();
            default:
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ALARM_TYPE_NOT_SUPPORT, alarmType.name());
        }
    }
}