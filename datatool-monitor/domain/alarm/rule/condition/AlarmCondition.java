/*
 * 文 件 名:  AlarmCondition.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.condition;

import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.AlarmRule;

import java.util.Set;

/**
 * 告警规则的条件
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/1]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public interface AlarmCondition {

    /**
     * 根据条件判断
     *
     * @param alarmRule 告警规则
     * @return 告警列表
     */
    Set<Alarm> evaluate(AlarmRule alarmRule);
}