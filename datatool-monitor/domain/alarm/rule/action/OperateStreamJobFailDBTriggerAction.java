/*
 * 文 件 名:  OperateStreamJobFailDBTriggerAction.java
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

/**
 * 告警触发操作流作业失败数据库动作
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/27]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class OperateStreamJobFailDBTriggerAction implements AlarmTriggerAction {
    private static final Logger LOGGER = LoggerFactory.getLogger("Alarm");

    private final DtAlarmStreamJobFailRepository dtAlarmStreamJobFailRepository = SpringContextHelper.getBean(DtAlarmStreamJobFailRepository.class);

    @Override
    public void execute(Alarm alarm) {
        // 保存数据库
        DtAlarmStreamJobFailEntity dtAlarmStreamJobFailEntity = new DtAlarmStreamJobFailEntity();
        dtAlarmStreamJobFailEntity.setJobId(alarm.getAlarmMsg().getId());
        dtAlarmStreamJobFailEntity.setJobName(alarm.getAlarmMsg().getName());
        dtAlarmStreamJobFailRepository.save(dtAlarmStreamJobFailEntity);
        LOGGER.info("save job id {}, job name {} to dt_alarm_stream_job_fail success!", alarm.getAlarmMsg().getId(),
                alarm.getAlarmMsg().getName());
    }
}