/*
 * 文 件 名:  BatchTaskWaitingTimeoutAlarmCondition.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.condition;

import com.huawei.smartcampus.datatool.monitor.common.properties.AlarmConfig;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmBasicInfo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmDataOperator;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmHelper;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata.TaskWaitingTimeoutVo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.AlarmRule;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.AlarmAction;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.OperateBatchTaskWaitingTimeoutDBRecoverAction;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.OperateBatchTaskWaitingTimeoutDBTriggerAction;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 批量作业等待运行告警条件
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class BatchTaskWaitingTimeoutAlarmCondition implements AlarmCondition {
    private static final long TASK_MAX_WAITING_TIME = AlarmConfig.dataBatchTaskWaitingTimeoutThreshold();
    private final AlarmHelper alarmHelper = SpringContextHelper.getBean(AlarmHelper.class);
    private final AlarmDataOperator alarmDataOperator = SpringContextHelper.getBean(AlarmDataOperator.class);

    @Override
    public Set<Alarm> evaluate(AlarmRule alarmRule) {
        // 获取当前实例等待超时告警列表A
        List<AlarmBasicInfo> newAlarmBasicInfoList = queryWaitingTimeoutTask();
        // 获取alarm_data表中已存在的等待超时告警列表B
        List<AlarmBasicInfo> currentAlarmBasicInfoList = alarmHelper.queryCurrentAlarmByAlarmId(alarmRule.alarmId(),
                null);
        List<AlarmAction> alarmTriggerActions = new ArrayList<>();
        List<AlarmAction> alarmRecoverActions = new ArrayList<>();
        alarmTriggerActions.add(new OperateBatchTaskWaitingTimeoutDBTriggerAction());
        alarmRecoverActions.add(new OperateBatchTaskWaitingTimeoutDBRecoverAction());
        return alarmHelper.fillAlarmList(alarmRule, newAlarmBasicInfoList, currentAlarmBasicInfoList,
                alarmTriggerActions, alarmRecoverActions);
    }

    private List<AlarmBasicInfo> queryWaitingTimeoutTask() {
        List<TaskWaitingTimeoutVo> taskWaitingTimeoutVoList = alarmDataOperator.getTaskWaitingTimeoutQueryResult();
        List<AlarmBasicInfo> alarmBasicInfoList = new ArrayList<>();
        List<String> jobIds = new ArrayList<>();
        for (TaskWaitingTimeoutVo taskWaitingTimeoutVo : taskWaitingTimeoutVoList) {
            Date tiStartDate = taskWaitingTimeoutVo.getTaskStartDate();
            Date drStartDate = taskWaitingTimeoutVo.getDagRunStartDate();
            // 作业运行等待时间
            long delay = (tiStartDate.getTime() - drStartDate.getTime()) / 1000;
            if (delay > TASK_MAX_WAITING_TIME && !jobIds.contains(taskWaitingTimeoutVo.getJobId())) {
                jobIds.add(taskWaitingTimeoutVo.getJobId());
                alarmBasicInfoList.add(new AlarmBasicInfo(taskWaitingTimeoutVo.getJobId(),
                        taskWaitingTimeoutVo.getName(), Long.toString(delay)));
            }
        }
        return alarmBasicInfoList;
    }
}