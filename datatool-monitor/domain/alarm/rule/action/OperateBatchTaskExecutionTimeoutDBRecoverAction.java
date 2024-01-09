/*
 * 文 件 名:  OperateBatchTaskExecutionTimeoutDBRecoverAction.java
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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * 告警恢复操作作业实例执行超时数据库动作
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/27]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class OperateBatchTaskExecutionTimeoutDBRecoverAction implements AlarmRecoverAction {
    private static final Logger LOGGER = LoggerFactory.getLogger("Alarm");

    private final DtAlarmBatchTaskExecutionTimeoutRepository dtAlarmBatchTaskExecutionTimeoutRepository = SpringContextHelper
            .getBean(DtAlarmBatchTaskExecutionTimeoutRepository.class);

    @Override
    public void execute(Alarm alarm) {
        String jobId = alarm.getAlarmMsg().getId();
        String jobName = alarm.getAlarmMsg().getName();
        // 更新数据库
        List<DtAlarmBatchTaskExecutionTimeoutEntity> dtAlarmBatchTaskExecutionTimeoutEntityList = dtAlarmBatchTaskExecutionTimeoutRepository
                .findByJobIdAndJobNameAndEndDateIsNull(jobId, jobName);
        if (dtAlarmBatchTaskExecutionTimeoutEntityList == null
                || dtAlarmBatchTaskExecutionTimeoutEntityList.isEmpty()) {
            LOGGER.error("query job id {}, job name {} from dt_alarm_batch_task_execution_timeout is empty!", jobId,
                    jobName);
            return;
        }
        DtAlarmBatchTaskExecutionTimeoutEntity dtAlarmBatchTaskExecutionTimeoutEntity = dtAlarmBatchTaskExecutionTimeoutEntityList
                .get(0);
        dtAlarmBatchTaskExecutionTimeoutEntity.setEndDate(Timestamp.from(Instant.now()));
        dtAlarmBatchTaskExecutionTimeoutRepository.save(dtAlarmBatchTaskExecutionTimeoutEntity);
        LOGGER.info("update job id {}, job name {} to dt_alarm_batch_task_execution_timeout success!", jobId, jobName);
    }
}