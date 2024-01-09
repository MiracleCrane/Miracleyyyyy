/*
 * 文 件 名:  TriggerSaveAlarmDataDBAction.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/11
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action;

import com.huawei.smartcampus.datatool.entity.AlarmDataEntity;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.repository.AlarmDataRepository;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 告警触发操作数据库动作
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/11]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class OperateAlarmDataDBTriggerAction implements AlarmTriggerAction {
    private static final Logger LOGGER = LoggerFactory.getLogger("Alarm");

    private final AlarmDataRepository alarmDataRepository = SpringContextHelper.getBean(AlarmDataRepository.class);

    @Override
    public void execute(Alarm alarm) {
        // 保存数据库
        AlarmDataEntity alarmDataEntity = new AlarmDataEntity();
        alarmDataEntity.setAlarmId(alarm.getAlarmMsg().getAlarmId());
        alarmDataEntity.setCreateDate(alarm.getOccurDate());
        alarmDataEntity.setHostname(alarm.getAlarmMsg().getHostname());
        alarmDataEntity.setId(alarm.getAlarmMsg().getId());
        alarmDataEntity.setName(alarm.getAlarmMsg().getName());
        alarmDataRepository.save(alarmDataEntity);
        LOGGER.info("save alarm id {}, job name {} to db success!", alarm.getAlarmMsg().getAlarmId(),
                alarm.getAlarmMsg().getName());
    }
}