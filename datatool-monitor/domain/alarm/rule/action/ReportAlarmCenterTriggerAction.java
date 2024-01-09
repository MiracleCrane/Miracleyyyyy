/*
 * 文 件 名:  ReportAlarmCenterTriggerAction.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/13
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action;

import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.AlarmCenterGateway;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

/**
 * 上报告警中心触发动作
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/13]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class ReportAlarmCenterTriggerAction implements AlarmAction {
    private final AlarmCenterGateway alarmCenterGateway = SpringContextHelper.getBean(AlarmCenterGateway.class);

    @Override
    public void execute(Alarm alarm) {
        alarmCenterGateway.trigger(alarm);
    }
}