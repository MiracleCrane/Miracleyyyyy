/*
 * 文 件 名:  OperateBatchTaskFailDBRecoverAction.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/27
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action;

import com.huawei.smartcampus.datatool.entity.DtAlarmBatchTaskFailEntity;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.repository.DtAlarmBatchTaskFailRepository;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * 告警触发操作作业实例失败数据库动作
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/27]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class OperateBatchTaskFailDBRecoverAction implements AlarmRecoverAction {
    private static final Logger LOGGER = LoggerFactory.getLogger("Alarm");

    private final DtAlarmBatchTaskFailRepository dtAlarmBatchTaskFailRepository = SpringContextHelper
            .getBean(DtAlarmBatchTaskFailRepository.class);

    @Override
    public void execute(Alarm alarm) {
        String jobName = alarm.getAlarmMsg().getName();
        String jobId = alarm.getAlarmMsg().getId();
        // 去掉时间戳
        String[] jobNameArray = jobId.split("_");
        int arrayLength = jobNameArray.length;
        int charNum = jobNameArray[arrayLength - 1].length();
        String jobIdStr = jobId.substring(0, jobId.length() - charNum - 1);
        String jobNameStr = jobName.substring(0, jobName.length() - charNum - 1);
        String jobStartDate = alarm.getAlarmMsg().getAlarmAttr();
        // 更新数据库
        List<DtAlarmBatchTaskFailEntity> dtAlarmBatchTaskFailEntityList = dtAlarmBatchTaskFailRepository
                .findByJobIdAndJobNameAndJobStartDateAndEndDateIsNull(jobIdStr, jobNameStr, jobStartDate);
        if (dtAlarmBatchTaskFailEntityList == null || dtAlarmBatchTaskFailEntityList.isEmpty()) {
            LOGGER.error("query job id {}, job name {}, job start date {} from dt_alarm_batch_task_fail is empty!",
                    jobIdStr, jobNameStr, jobStartDate);
            return;
        }
        DtAlarmBatchTaskFailEntity dtAlarmBatchTaskFailEntity = dtAlarmBatchTaskFailEntityList.get(0);
        dtAlarmBatchTaskFailEntity.setEndDate(Timestamp.from(Instant.now()));
        dtAlarmBatchTaskFailRepository.save(dtAlarmBatchTaskFailEntity);
        LOGGER.info("update job id {}, job name {}, job start date {} to dt_alarm_batch_task_fail success!", jobIdStr,
                jobNameStr, jobStartDate);
    }
}