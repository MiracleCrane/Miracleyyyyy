/*
 * 文 件 名:  ZabbixDataBatchAlarmMessageFormatter.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/6/6
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.alarmcenter.zabbix;

import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;

import java.text.MessageFormat;

/**
 * zabbix平台批处理作业告警信息格式化工具
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/6/6]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class ZabbixDataBatchAlarmMessageFormatter implements ZabbixAlarmMessageFormatter {
    private static final String DATA_BATCH_ALARM_MSG_TEMPLATE = "{0}_{1} alarm, {2} jobs:({3},{4},{5})";

    @Override
    public String formAlarmId(Alarm alarm) {
        String alarmId = alarm.getAlarmMsg().getAlarmId();
        String jobId = alarm.getAlarmMsg().getId();
        // 批处理作业是alarmId_jobId
        return alarmId + "_" + jobId;
    }

    @Override
    public String formAlarmDescription(Alarm alarm, String actionType) {
        StringBuilder alarmDesc = new StringBuilder();
        String jobName = alarm.getAlarmMsg().getName();
        String alarmType = alarm.getRule().type().toString();
        // 批处理告警拼接告警描述信息
        String alarmTypeId = alarm.getAlarmMsg().getAlarmId();
        String jobId = alarm.getAlarmMsg().getId();
        String hostname = alarm.getAlarmMsg().getHostname();
        alarmDesc.append(alarmTypeId).append("_");
        String batchAlarmMsg = MessageFormat.format(DATA_BATCH_ALARM_MSG_TEMPLATE, actionType, alarmType, alarmType,
                jobId, jobName, hostname);
        alarmDesc.append(batchAlarmMsg);
        return alarmDesc.toString();
    }
}