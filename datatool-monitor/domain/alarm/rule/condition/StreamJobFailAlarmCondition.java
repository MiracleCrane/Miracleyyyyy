/*
 * 文 件 名:  StreamJobFailAlarmCondition.java
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
import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmBasicInfo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmHelper;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.AlarmRule;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.AlarmAction;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.OperateStreamJobFailDBRecoverAction;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.OperateStreamJobFailDBTriggerAction;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.StreamJobGateway;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 流处理作业异常失败告警条件
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class StreamJobFailAlarmCondition implements AlarmCondition {
    private final AlarmHelper alarmHelper = SpringContextHelper.getBean(AlarmHelper.class);
    private final StreamJobGateway streamJobGateway = SpringContextHelper.getBean(StreamJobGateway.class);
    @Override
    public Set<Alarm> evaluate(AlarmRule alarmRule) {
        // 获取当前流处理作业失败告警列表A
        List<AlarmBasicInfo> newAlarmBasicInfoList = queryFailedStreamJob();
        // 获取alarm_data表中已存在的流处理作业失败告警列表B
        List<AlarmBasicInfo> currentAlarmBasicInfoList = alarmHelper.queryCurrentAlarmByAlarmId(alarmRule.alarmId(),
                null);
        List<AlarmAction> alarmTriggerActions = new ArrayList<>();
        List<AlarmAction> alarmRecoverActions = new ArrayList<>();
        alarmTriggerActions.add(new OperateStreamJobFailDBTriggerAction());
        alarmRecoverActions.add(new OperateStreamJobFailDBRecoverAction());
        return alarmHelper.fillAlarmList(alarmRule, newAlarmBasicInfoList, currentAlarmBasicInfoList,
                alarmTriggerActions, alarmRecoverActions);
    }

    private List<AlarmBasicInfo> queryFailedStreamJob() {
        // 查询所有stream job
        List<StreamJobInfo> result = streamJobGateway.queryStreamJobInfo(null);
        if (result == null || result.isEmpty()) {
            return Collections.emptyList();
        }
        List<AlarmBasicInfo> alarmBasicInfoList = new ArrayList<>();
        for (StreamJobInfo streamJobInfo : result) {
            String status = streamJobInfo.getStatus();
            // 如果状态处于FAILED或者FAILING，则认为是失败作业
            if (JobStatus.FAILED.name().equals(status) || JobStatus.FAILING.name().equals(status)) {
                alarmBasicInfoList.add(new AlarmBasicInfo(streamJobInfo.getId(), streamJobInfo.getName(), null));
            }
        }
        return alarmBasicInfoList;
    }
}