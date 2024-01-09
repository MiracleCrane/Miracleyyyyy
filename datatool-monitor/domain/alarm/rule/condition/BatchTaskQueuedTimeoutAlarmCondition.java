/*
 * 文 件 名:  BatchTaskQueuedTimeoutAlarmCondition.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.condition;

import com.huawei.smartcampus.datatool.entity.TaskInstanceEntity;
import com.huawei.smartcampus.datatool.monitor.common.properties.AlarmConfig;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmBasicInfo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmHelper;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.AlarmRule;
import com.huawei.smartcampus.datatool.monitor.infrastructure.schema.TaskInstanceQuerySchema;
import com.huawei.smartcampus.datatool.repository.TaskInstanceRepository;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 批量作业长时间队列告警条件
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class BatchTaskQueuedTimeoutAlarmCondition implements AlarmCondition {
    private static final long QUEUED_MAX_WAITING_TIME = AlarmConfig.dataBatchTaskQueuedTimeoutThreshold();
    private final TaskInstanceRepository taskInstanceRepository = SpringContextHelper
            .getBean(TaskInstanceRepository.class);

    private final AlarmHelper alarmHelper = SpringContextHelper.getBean(AlarmHelper.class);

    @Override
    public Set<Alarm> evaluate(AlarmRule alarmRule) {
        // 获取当前实例长时间队列告警列表A
        List<AlarmBasicInfo> newAlarmBasicInfoList = queryQueuedTimeoutTask();
        // 获取alarm_data表中已存在的实例长时间队列告警列表B
        List<AlarmBasicInfo> currentAlarmBasicInfoList = alarmHelper.queryCurrentAlarmByAlarmId(alarmRule.alarmId(),
                null);
        return alarmHelper.fillAlarmList(alarmRule, newAlarmBasicInfoList, currentAlarmBasicInfoList, null, null);
    }

    private List<AlarmBasicInfo> queryQueuedTimeoutTask() {
        Specification<TaskInstanceEntity> specification = TaskInstanceQuerySchema
                .getTaskInstanceQueuedTimeoutSpecification();
        List<TaskInstanceEntity> taskInstanceEntityList = taskInstanceRepository.findAll(specification);
        List<AlarmBasicInfo> alarmBasicInfoList = new ArrayList<>();
        for (TaskInstanceEntity taskInstanceEntity : taskInstanceEntityList) {
            Date actualQueuedTimeStamp = taskInstanceEntity.getQueuedDttm();
            if (actualQueuedTimeStamp == null) {
                continue;
            }
            Instant tolerantQueuedTime = Instant.now().minusSeconds(QUEUED_MAX_WAITING_TIME);
            Timestamp tolerantQueuedTimeStamp = Timestamp.from(tolerantQueuedTime);
            AlarmBasicInfo alarmBasicInfo = new AlarmBasicInfo();
            alarmBasicInfo.setId(taskInstanceEntity.getDagId());
            alarmBasicInfo.setName(taskInstanceEntity.getDtBatchJobEntity().getName());
            // 进队时间超过10min
            if (tolerantQueuedTimeStamp.after(actualQueuedTimeStamp) && !alarmBasicInfoList.contains(alarmBasicInfo)) {
                alarmBasicInfoList.add(alarmBasicInfo);
            }
        }
        return alarmBasicInfoList;
    }
}