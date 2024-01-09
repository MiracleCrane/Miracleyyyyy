/*
 * 文 件 名:  OperateAlarmDataDBRecoverAction.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/13
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.repository.AlarmDataRepository;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 告警恢复操作数据库动作
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/13]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class OperateAlarmDataDBRecoverAction implements AlarmRecoverAction {
    private static final Logger LOGGER = LoggerFactory.getLogger("Alarm");

    private final AlarmDataRepository alarmDataRepository = SpringContextHelper.getBean(AlarmDataRepository.class);

    @Override
    public void execute(Alarm alarm) {
        String id = alarm.getAlarmMsg().getId();
        String alarmId = alarm.getAlarmMsg().getAlarmId();
        // 删除alarm_data表数据
        int count = alarmDataRepository.deleteAlarmDataEntityByIdAndAlarmId(id, alarmId);
        // 删除失败，抛出异常
        if (count == 0) {
            LOGGER.error("delete alarm data records failed! id: {}, alarm id: {}", id, alarmId);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_DELETE_DB_RECORDS_FAIL);
        }
        LOGGER.info("delete alarm id {}, job name {} success!", alarmId, alarm.getAlarmMsg().getName());
    }
}