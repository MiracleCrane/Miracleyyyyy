/*
 * 文 件 名:  Alarm.java
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
import java.util.HashSet;
import java.util.Set;

/**
 * 告警的实例
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/1]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class Alarm {
    private AlarmRule rule;

    private Date occurDate;

    private AlarmMsg alarmMsg;

    private Set<AlarmAction> actions = new HashSet<>();

    public Set<AlarmAction> getActions() {
        return actions;
    }

    public void setActions(Set<AlarmAction> actions) {
        this.actions = actions;
    }

    public AlarmRule getRule() {
        return rule;
    }

    public void setRule(AlarmRule rule) {
        this.rule = rule;
    }

    public Date getOccurDate() {
        return occurDate;
    }

    public void setOccurDate(Date occurDate) {
        this.occurDate = occurDate;
    }

    public AlarmMsg getAlarmMsg() {
        return alarmMsg;
    }

    public void setAlarmMsg(AlarmMsg alarmMsg) {
        this.alarmMsg = alarmMsg;
    }
}