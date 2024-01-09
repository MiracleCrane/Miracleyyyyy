/*
 * 文 件 名:  BatchTaskFailAlarmRule.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule;

import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmType;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.condition.AlarmCondition;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.condition.BatchTaskFailAlarmCondition;

/**
 * 批量作业失败告警规则
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/2]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class BatchTaskFailAlarmRule implements AlarmRule {

    private final AlarmType alarmType = AlarmType.BATCH_TASK_FAIL;

    private AlarmCondition condition = new BatchTaskFailAlarmCondition();

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