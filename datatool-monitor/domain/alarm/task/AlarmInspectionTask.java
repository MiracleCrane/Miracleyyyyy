/*
 * 文 件 名:  AlarmTask.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.task;

import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.AlarmRule;
import com.huawei.smartcampus.datatool.monitor.domain.inspection.task.InspectionTask;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * 告警巡检任务
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/1]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class AlarmInspectionTask implements InspectionTask {
    private static final Logger LOGGER = LoggerFactory.getLogger("Alarm");
    private final AlarmActionExecutor alarmActionExecutor = SpringContextHelper.getBean(AlarmActionExecutor.class);
    private boolean enable;

    private int interval = 0;

    private AlarmRule rule;

    public AlarmInspectionTask(boolean enable, int interval, AlarmRule alarmRule) {
        this.enable = enable;
        this.interval = interval;
        this.rule = alarmRule;
    }

    @Override
    public void run() {
        Set<Alarm> alarms = rule.condition().evaluate(rule);

        for (Alarm alarm : alarms) {
            try {
                alarmActionExecutor.execute(alarm);
            } catch (Exception e) {
                LOGGER.error("execute alarm action failed!", e);
            }
        }
    }

    @Override
    public String id() {
        return rule.alarmId();
    }

    @Override
    public String name() {
        return rule.type().name();
    }

    @Override
    public boolean enable() {
        return enable;
    }

    @Override
    public int interval() {
        return interval;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setRule(AlarmRule rule) {
        this.rule = rule;
    }
}