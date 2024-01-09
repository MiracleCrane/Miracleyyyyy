/*
 * 文 件 名:  OperateStreamJobFailDBRecoverAction.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/27
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action;

import com.huawei.smartcampus.datatool.entity.DtAlarmStreamJobFailEntity;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.repository.DtAlarmStreamJobFailRepository;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * 告警恢复操作流作业失败数据库动作
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/27]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class OperateStreamJobFailDBRecoverAction implements AlarmRecoverAction {
    private static final Logger LOGGER = LoggerFactory.getLogger("Alarm");

    private final DtAlarmStreamJobFailRepository dtAlarmStreamJobFailRepository = SpringContextHelper
            .getBean(DtAlarmStreamJobFailRepository.class);

    @Override
    public void execute(Alarm alarm) {
        String jobId = alarm.getAlarmMsg().getId();
        String jobName = alarm.getAlarmMsg().getName();
        // 更新数据库
        List<DtAlarmStreamJobFailEntity> dtAlarmStreamJobFailEntityList = dtAlarmStreamJobFailRepository
                .findByJobIdAndJobNameAndEndDateIsNull(jobId, jobName);
        if (dtAlarmStreamJobFailEntityList == null || dtAlarmStreamJobFailEntityList.isEmpty()) {
            LOGGER.error("query job id {}, job name {} from dt_alarm_stream_job_fail is empty!", jobId, jobName);
            return;
        }
        DtAlarmStreamJobFailEntity dtAlarmStreamJobFailEntity = dtAlarmStreamJobFailEntityList.get(0);
        dtAlarmStreamJobFailEntity.setEndDate(Timestamp.from(Instant.now()));
        dtAlarmStreamJobFailRepository.save(dtAlarmStreamJobFailEntity);
        LOGGER.info("update job id {}, job name {} to dt_alarm_stream_job_fail success!", jobId, jobName);
    }
}