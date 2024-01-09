/*
 * 文 件 名:  AlarmTaskStartup.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.common.properties.AlarmConfig;
import com.huawei.smartcampus.datatool.monitor.common.properties.AlarmProperty;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmType;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.AlarmRule;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.AlarmRuleFactory;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.task.AlarmInspectionTask;
import com.huawei.smartcampus.datatool.monitor.domain.inspection.service.InspectionTaskExecutor;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 告警巡检任务的启动类
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/2]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Component
@DependsOn("springContextHelper")
public class AlarmInspectionTaskStarter {
    /**
     * 在系统启动的时候，调用该方法，拉起所有告警任务
     */
    @PostConstruct
    public void onStartup() {
        // 遍历支持的alarmType，获取enable和interval
        if (!AlarmConfig.dataAllAlarmEnable()) {
            return;
        }
        for (AlarmType alarmType : AlarmType.values()) {
            AlarmProperty alarmProperty = getAlarmProperty(alarmType);
            AlarmRule alarmRule = AlarmRuleFactory.create(alarmType);
            AlarmInspectionTask task = new AlarmInspectionTask(alarmProperty.isAlarmEnable(),
                    alarmProperty.getAlarmInterval(), alarmRule);
            InspectionTaskExecutor.instance().schedule(task);
        }
    }

    private AlarmProperty getAlarmProperty(AlarmType alarmType) {
        switch (alarmType) {
            case BATCH_TASK_FAIL:
                return new AlarmProperty(AlarmConfig.dataBatchTaskFailEnable(),
                        AlarmConfig.dataBatchTaskFailInterval());
            case BATCH_TASK_EXECUTION_TIMEOUT:
                return new AlarmProperty(AlarmConfig.dataBatchTaskExecutionTimeoutEnable(),
                        AlarmConfig.dataBatchTaskExecutionTimeoutInterval());
            case BATCH_TASK_WAITING_TIMEOUT:
                return new AlarmProperty(AlarmConfig.dataBatchTaskWaitingTimeoutEnable(),
                        AlarmConfig.dataBatchTaskWaitingTimeoutInterval());
            case BATCH_TASK_QUEUED_TIMEOUT:
                return new AlarmProperty(AlarmConfig.dataBatchTaskQueuedTimeoutEnable(),
                        AlarmConfig.dataBatchTaskQueuedTimeoutInterval());
            case STREAM_JOB_FAIL:
                return new AlarmProperty(AlarmConfig.dataStreamJobFailEnable(),
                        AlarmConfig.dataStreamJobFailInterval());
            case STREAM_JOB_BACKPRESSURE:
                return new AlarmProperty(AlarmConfig.dataStreamJobBackpressureEnable(),
                        AlarmConfig.dataStreamJobBackpressureInterval());
            default:
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ALARM_TYPE_NOT_SUPPORT, alarmType.name());
        }
    }
}