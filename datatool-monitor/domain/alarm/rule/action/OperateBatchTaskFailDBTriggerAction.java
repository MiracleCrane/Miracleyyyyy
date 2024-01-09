/*
 * 文 件 名:  OperateBatchTaskFailDBTriggerAction.java
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

/**
 * 告警恢复操作作业实例失败数据库动作
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/27]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class OperateBatchTaskFailDBTriggerAction implements AlarmTriggerAction {
    private static final Logger LOGGER = LoggerFactory.getLogger("Alarm");

    private final DtAlarmBatchTaskFailRepository dtAlarmBatchTaskFailRepository = SpringContextHelper
            .getBean(DtAlarmBatchTaskFailRepository.class);

    @Override
    public void execute(Alarm alarm) {
        // 保存数据库
        DtAlarmBatchTaskFailEntity dtAlarmBatchTaskFailEntity = new DtAlarmBatchTaskFailEntity();
        String jobId = alarm.getAlarmMsg().getId();
        String jobName = alarm.getAlarmMsg().getName();
        // 去掉时间戳
        String[] jobNameArray = jobId.split("_");
        int charNum = jobNameArray[jobNameArray.length - 1].length();
        String jobNameStr = jobName.substring(0, jobName.length() - charNum - 1);
        String jobIdStr = jobId.substring(0, jobId.length() - charNum - 1);
        dtAlarmBatchTaskFailEntity.setJobId(jobIdStr);
        dtAlarmBatchTaskFailEntity.setJobName(jobNameStr);
        dtAlarmBatchTaskFailEntity.setJobStartDate(alarm.getAlarmMsg().getAlarmAttr());
        dtAlarmBatchTaskFailRepository.save(dtAlarmBatchTaskFailEntity);
        LOGGER.info("save job id {}, job name {}, start_date {} to dt_alarm_batch_task_fail success!", jobIdStr,
                jobNameStr, alarm.getAlarmMsg().getAlarmAttr());
    }
}