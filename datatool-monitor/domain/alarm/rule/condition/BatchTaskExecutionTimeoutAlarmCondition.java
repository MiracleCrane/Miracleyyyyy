/*
 * 文 件 名:  BATCH_TASK_EXECUTION_TIMEOUT.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.condition;

import com.huawei.smartcampus.datatool.monitor.common.properties.AlarmConfig;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmBasicInfo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmDataOperator;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmHelper;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata.BatchJobAlarmThreshold;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata.TaskExecutionTimeoutVo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.AlarmRule;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.AlarmAction;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.OperateBatchTaskExecutionTimeoutDBRecoverAction;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.OperateBatchTaskExecutionTimeoutDBTriggerAction;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.BatchJobGateway;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 批量作业运行超时告警条件
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/2]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class BatchTaskExecutionTimeoutAlarmCondition implements AlarmCondition {
    private static final double EXECUTION_MAX_WAITING_TIME = AlarmConfig.dataBatchTaskExecutionTimeoutThreshold();
    private final BatchJobGateway batchJobGateway = SpringContextHelper.getBean(BatchJobGateway.class);
    private final AlarmDataOperator alarmDataOperator = SpringContextHelper.getBean(AlarmDataOperator.class);
    private final AlarmHelper alarmHelper = SpringContextHelper.getBean(AlarmHelper.class);

    @Override
    public Set<Alarm> evaluate(AlarmRule alarmRule) {
        List<String> jobIdList = batchJobGateway.queryBatchRunningJobId();
        List<BatchJobAlarmThreshold> batchJobAlarmThresholds = batchJobGateway.queryBatchJobAlarmThreshold();
        List<AlarmBasicInfo> taskExecutionTimeoutList = new ArrayList<>();
        for (String jobId : jobIdList) {
            if (StringUtils.isEmpty(jobId)) {
                continue;
            }
            double threshold = EXECUTION_MAX_WAITING_TIME;
            // 如果页面上设置了超时时间，则用页面上自定义的阈值
            Optional<BatchJobAlarmThreshold> batchJobAlarmThresholdOptional = batchJobAlarmThresholds.stream()
                    .filter(item -> jobId.equals(item.getJobId()) && item.getAlarmThreshold() != null).findFirst();
            if (batchJobAlarmThresholdOptional.isPresent()) {
                BatchJobAlarmThreshold batchJobAlarmThreshold = batchJobAlarmThresholdOptional.get();
                threshold = (double) batchJobAlarmThreshold.getAlarmThreshold();
            }
            // 添加当前作业实例执行超时告警列表A
            addExecutionTimeoutList(jobId, threshold, taskExecutionTimeoutList);
        }
        // 获取alarm_data表中已存在的作业实例执行超时告警列表B
        List<AlarmBasicInfo> currentAlarmBasicInfoList = alarmHelper.queryCurrentAlarmByAlarmId(alarmRule.alarmId(),
                null);
        List<AlarmAction> alarmTriggerActions = new ArrayList<>();
        List<AlarmAction> alarmRecoverActions = new ArrayList<>();
        alarmTriggerActions.add(new OperateBatchTaskExecutionTimeoutDBTriggerAction());
        alarmRecoverActions.add(new OperateBatchTaskExecutionTimeoutDBRecoverAction());
        return alarmHelper.fillAlarmList(alarmRule, taskExecutionTimeoutList, currentAlarmBasicInfoList,
                alarmTriggerActions, alarmRecoverActions);
    }

    private void addExecutionTimeoutList(String jobId, double threshold,
            List<AlarmBasicInfo> taskExecutionTimeoutList) {
        List<TaskExecutionTimeoutVo> taskExecutionTimeoutVoList = alarmDataOperator
                .getTaskExecutionTimeoutQueryResult(jobId, threshold);
        // 查出结果只会有0条或1条
        if (taskExecutionTimeoutVoList != null && !taskExecutionTimeoutVoList.isEmpty()) {
            TaskExecutionTimeoutVo taskExecutionTimeoutVo = taskExecutionTimeoutVoList.get(0);
            AlarmBasicInfo alarmBasicInfo = new AlarmBasicInfo(taskExecutionTimeoutVo.getJobId(),
                    taskExecutionTimeoutVo.getJobName(), String.valueOf(taskExecutionTimeoutVo.getDuration()));
            taskExecutionTimeoutList.add(alarmBasicInfo);
        }
    }
}