/*
 * 文 件 名:  AlarmBuilder.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm;

import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.AlarmRule;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.AlarmAction;

import java.util.Date;

/**
 * 告警实例构造器
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/1]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class AlarmBuilder {

    private Alarm alarm = new Alarm();

    private AlarmBuilder() {

    }

    public static AlarmBuilder newBuilder() {
        return new AlarmBuilder();
    }

    public AlarmBuilder occurDate(Date occurDate) {
        this.alarm.setOccurDate(occurDate);
        return this;
    }

    public AlarmBuilder alarmRule(AlarmRule alarmRule) {
        this.alarm.setRule(alarmRule);
        return this;
    }

    public AlarmBuilder appendAction(AlarmAction action) {
        this.alarm.getActions().add(action);
        return this;
    }

    public AlarmBuilder alarmMsg(AlarmMsg alarmMsg) {
        this.alarm.setAlarmMsg(alarmMsg);
        return this;
    }

    public Alarm builder() {
        return this.alarm;
    }
}