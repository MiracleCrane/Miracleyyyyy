/*
 * 文 件 名:  AlarmDef.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule;

import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmType;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.condition.AlarmCondition;

/**
 * 告警规则
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/1]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public interface AlarmRule {

    /**
     * 告警ID
     *
     * @return 告警ID
     */
    String alarmId();

    /**
     * 告警类型
     *
     * @return 告警类型枚举值
     */
    AlarmType type();

    /**
     * 告警的条件
     *
     * @return 告警的条件
     */
    AlarmCondition condition();
}