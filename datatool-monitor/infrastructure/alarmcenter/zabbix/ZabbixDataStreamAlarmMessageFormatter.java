/*
 * 文 件 名:  ZabbixDataStreamAlarmMessageFormatter.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/6/6
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.alarmcenter.zabbix;

import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmType;

import java.text.MessageFormat;

/**
 * zabbix平台流处理作业告警信息格式化工具
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/6/6]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class ZabbixDataStreamAlarmMessageFormatter implements ZabbixAlarmMessageFormatter {
    private static final String DATA_STREAM_ALARM_MSG_TEMPLATE = "The stream job {0} {1} {2}";

    @Override
    public String formAlarmId(Alarm alarm) {
        String alarmId = alarm.getAlarmMsg().getAlarmId();
        String jobId = alarm.getAlarmMsg().getId();
        // 流处理作业zabbixAlarmId是alarmId_jobId
        return alarmId + "_" + jobId;
    }

    @Override
    public String formAlarmDescription(Alarm alarm, String actionType) {
        StringBuilder alarmDesc = new StringBuilder();
        String jobName = alarm.getAlarmMsg().getName();
        // 流处理作业告警拼接告警描述信息
        String streamAlarmMsg = MessageFormat.format(DATA_STREAM_ALARM_MSG_TEMPLATE, jobName, actionType,
                alarm.getRule().type().name());
        alarmDesc.append(streamAlarmMsg);
        // 如果是反压告警则还需要添加反压情况：高/低
        String backpressureLevel = alarm.getAlarmMsg().getAlarmAttr();
        if (AlarmType.STREAM_JOB_BACKPRESSURE.equals(alarm.getRule().type()) && backpressureLevel != null) {
            alarmDesc.append("_").append(backpressureLevel);
        }
        return alarmDesc.toString();
    }
}