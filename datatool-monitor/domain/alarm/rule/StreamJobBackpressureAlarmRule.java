/*
 * 文 件 名:  StreamJobBackpressureAlarmRule.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule;

import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmType;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.condition.AlarmCondition;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.condition.StreamJobBackpressureAlarmCondition;

/**
 * 实时作业反压告警规则
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class StreamJobBackpressureAlarmRule implements AlarmRule {
    private AlarmType alarmType = AlarmType.STREAM_JOB_BACKPRESSURE;

    private AlarmCondition condition = new StreamJobBackpressureAlarmCondition();

    @Override
    public String alarmId() {
        return alarmType.alarmId();
    }

    @Override
    public AlarmType type() {
        return alarmType;
    }

    @Override
    public AlarmCondition condition() {
        return condition;
    }
}