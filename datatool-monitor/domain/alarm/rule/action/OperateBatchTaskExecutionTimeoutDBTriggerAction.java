/*
 * 文 件 名:  OperateBatchTaskExecutionTimeoutDBTriggerAction.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/27
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action;

import com.huawei.smartcampus.datatool.entity.DtAlarmBatchTaskExecutionTimeoutEntity;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.repository.DtAlarmBatchTaskExecutionTimeoutRepository;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 告警触发操作作业实例执行超时数据库动作
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/27]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class OperateBatchTaskExecutionTimeoutDBTriggerAction implements AlarmTriggerAction {
    private static final Logger LOGGER = LoggerFactory.getLogger("Alarm");

    private final DtAlarmBatchTaskExecutionTimeoutRepository dtAlarmBatchTaskExecutionTimeoutRepository = SpringContextHelper
            .getBean(DtAlarmBatchTaskExecutionTimeoutRepository.class);

    @Override
    public void execute(Alarm alarm) {
        // 保存数据库
        DtAlarmBatchTaskExecutionTimeoutEntity dtAlarmBatchTaskExecutionTimeoutEntity = new DtAlarmBatchTaskExecutionTimeoutEntity();
        dtAlarmBatchTaskExecutionTimeoutEntity.setJobId(alarm.getAlarmMsg().getId());
        dtAlarmBatchTaskExecutionTimeoutEntity.setJobName(alarm.getAlarmMsg().getName());
        dtAlarmBatchTaskExecutionTimeoutEntity
                .setExecuteDuration(Double.parseDouble(alarm.getAlarmMsg().getAlarmAttr()));
        dtAlarmBatchTaskExecutionTimeoutRepository.save(dtAlarmBatchTaskExecutionTimeoutEntity);
        LOGGER.info("save job id {}, job name {} to dt_alarm_batch_task_execution_timeout success!",
                alarm.getAlarmMsg().getId(), alarm.getAlarmMsg().getName());
    }
}