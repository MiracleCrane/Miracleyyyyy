/*
 * 文 件 名:  Zabbix.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.alarmcenter.zabbix;

import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.AlarmCenterGateway;
import com.huawei.smartcampus.datatool.pojo.AlarmInfo;
import com.huawei.smartcampus.datatool.utils.ZabbixUtil;
import org.springframework.stereotype.Component;

/**
 * zabbix，告警中心的一种实现
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/2]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Component
public class ZabbixAlarmCenter implements AlarmCenterGateway {
    @Override
    public void trigger(Alarm alarm) {
        ZabbixAlarmMessageFormatter alarmMessageFormatter = ZabbixAlarmMessageFormatterFactory.create(alarm.getRule().type());
        AlarmInfo alarmInfo = new AlarmInfo();
        alarmInfo.setAlarmStatus("alarm");
        alarmInfo.setAlarmDesc(alarmMessageFormatter.formAlarmDescription(alarm, "trigger"));
        alarmInfo.setAlarmId(alarmMessageFormatter.formAlarmId(alarm));
        alarmInfo.setManualClear(alarm.getAlarmMsg().isEnableManualRecover());
        alarmInfo.setAlarmLevel(alarm.getAlarmMsg().getAlarmLevel());
        ZabbixUtil.alarmOrResume(alarmInfo);
    }

    @Override
    public void recover(Alarm alarm) {
        ZabbixAlarmMessageFormatter alarmMessageFormatter = ZabbixAlarmMessageFormatterFactory.create(alarm.getRule().type());
        AlarmInfo alarmInfo = new AlarmInfo();
        alarmInfo.setAlarmStatus("ok");
        alarmInfo.setAlarmDesc(alarmMessageFormatter.formAlarmDescription(alarm, "resume"));
        alarmInfo.setAlarmId(alarmMessageFormatter.formAlarmId(alarm));
        alarmInfo.setManualClear(alarm.getAlarmMsg().isEnableManualRecover());
        alarmInfo.setAlarmLevel(alarm.getAlarmMsg().getAlarmLevel());
        ZabbixUtil.alarmOrResume(alarmInfo);
    }
}