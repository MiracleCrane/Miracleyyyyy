/*
 * 文 件 名:  StreamJobBackpressureAlarmCondition.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.condition;

import com.huawei.smartcampus.datatool.enums.JobStatus;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.streamjob.StreamJobInfo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.streamjob.StreamJobTaskInfo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmBasicInfo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmHelper;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.AlarmRule;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.AlarmAction;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.OperateStreamJobBackpressureDBRecoverAction;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.OperateStreamJobBackpressureDBTriggerAction;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.StreamJobGateway;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 流处理作业反压告警条件
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class StreamJobBackpressureAlarmCondition implements AlarmCondition {
    private final AlarmHelper alarmHelper = SpringContextHelper.getBean(AlarmHelper.class);
    private final StreamJobGateway streamJobGateway = SpringContextHelper.getBean(StreamJobGateway.class);
    @Override
    public Set<Alarm> evaluate(AlarmRule alarmRule) {
        // 获取当前流处理作业反压告警列表A
        List<AlarmBasicInfo> newAlarmBasicInfoList = queryStreamJobBackpressure();
        // 获取alarm_data表中已存在的流处理作业反压告警
        List<AlarmBasicInfo> currentAlarmBasicInfoList = alarmHelper.queryCurrentAlarmByAlarmId(alarmRule.alarmId(),
                null);
        List<AlarmAction> alarmTriggerActions = new ArrayList<>();
        List<AlarmAction> alarmRecoverActions = new ArrayList<>();
        alarmTriggerActions.add(new OperateStreamJobBackpressureDBTriggerAction());
        alarmRecoverActions.add(new OperateStreamJobBackpressureDBRecoverAction());
        return alarmHelper.fillAlarmList(alarmRule, newAlarmBasicInfoList, currentAlarmBasicInfoList,
                alarmTriggerActions, alarmRecoverActions);
    }

    private List<AlarmBasicInfo> queryStreamJobBackpressure() {
        // 查询所有running状态的stream job
        List<StreamJobInfo> result = streamJobGateway.queryStreamJobInfo(JobStatus.RUNNING.name());
        if (result == null || result.isEmpty()) {
            return Collections.emptyList();
        }
        List<AlarmBasicInfo> alarmBasicInfoList = new ArrayList<>();
        for (StreamJobInfo streamJobInfo : result) {
            String jobId = streamJobInfo.getId();
            List<StreamJobTaskInfo> detailResultList = streamJobGateway.queryStreamTaskInfoByJobId(jobId);
            if (detailResultList == null) {
                continue;
            }
            for (StreamJobTaskInfo streamJobTaskInfo : detailResultList) {
                // 只检测源任务的backpressure状态
                if (!streamJobTaskInfo.getName().startsWith("Source")) {
                    continue;
                }
                String backpressureLevel = streamJobTaskInfo.getBackpressure();
                if ("high".equals(backpressureLevel) || "low".equals(backpressureLevel)) {
                    AlarmBasicInfo alarmBasicInfo = new AlarmBasicInfo();
                    alarmBasicInfo.setId(jobId);
                    alarmBasicInfo.setName(streamJobInfo.getName());
                    alarmBasicInfo.setExtraInfo(backpressureLevel);
                    alarmBasicInfoList.add(alarmBasicInfo);
                }
            }
        }
        return alarmBasicInfoList;
    }
}