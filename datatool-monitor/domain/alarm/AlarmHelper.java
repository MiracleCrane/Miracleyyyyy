/*
 * 文 件 名:  AlarmUtil.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/25
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm;

import com.huawei.smartcampus.datatool.entity.AlarmDataEntity;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.AlarmRule;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.AlarmAction;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.OperateAlarmDataDBRecoverAction;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.OperateAlarmDataDBTriggerAction;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.ReportAlarmCenterRecoverAction;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.ReportAlarmCenterTriggerAction;
import com.huawei.smartcampus.datatool.monitor.infrastructure.schema.AlarmDataQuerySchema;
import com.huawei.smartcampus.datatool.repository.AlarmDataRepository;
import com.huawei.smartcampus.datatool.utils.ZabbixUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 告警帮助类
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/25]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Component
public class AlarmHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger("Alarm");

    @Autowired
    private AlarmDataRepository alarmDataRepository;

    /**
     * 获取告警列表
     *
     * @param alarmRule 告警规则
     * @param newAlarmBasicInfoList 新增告警信息列表
     * @param currentAlarmBasicInfoList 当前告警信息列表
     * @param alarmTriggerActionList 告警触发动作列表
     * @param alarmRecoverActionList 告警恢复动作列表
     * @return 处理后的告警列表
     */
    public Set<Alarm> fillAlarmList(AlarmRule alarmRule, List<AlarmBasicInfo> newAlarmBasicInfoList,
            List<AlarmBasicInfo> currentAlarmBasicInfoList, List<AlarmAction> alarmTriggerActionList,
            List<AlarmAction> alarmRecoverActionList) {
        // 新触发告警为A-B
        List<AlarmBasicInfo> triggerAlarmBasicInfoList = getFilteredAlarmData(newAlarmBasicInfoList,
                currentAlarmBasicInfoList);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("trigger alarm: {}", triggerAlarmBasicInfoList);
        }
        // 待恢复告警为B-A
        List<AlarmBasicInfo> recoverAlarmBasicInfoList = getFilteredAlarmData(currentAlarmBasicInfoList,
                newAlarmBasicInfoList);
        Set<Alarm> alarms = new HashSet<>();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("recover alarm: {}", recoverAlarmBasicInfoList);
        }
        // 将触发告警加入告警列表
        for (AlarmBasicInfo alarmBasicInfo : triggerAlarmBasicInfoList) {
            addAlarm(alarmRule, alarmBasicInfo, alarms, true, alarmTriggerActionList);
        }
        // 将恢复告警加入告警列表
        for (AlarmBasicInfo alarmBasicInfo : recoverAlarmBasicInfoList) {
            addAlarm(alarmRule, alarmBasicInfo, alarms, false, alarmRecoverActionList);
        }
        return alarms;
    }

    /**
     * 通过告警id和开始时间查询当前告警
     *
     * @param alarmId 告警id
     * @param startDate 开始时间
     * @return 告警信息列表
     */
    public List<AlarmBasicInfo> queryCurrentAlarmByAlarmId(String alarmId, Date startDate) {
        Specification<AlarmDataEntity> specification = AlarmDataQuerySchema.getCurrentAlarmDataByAlarmId(alarmId,
                startDate);
        List<AlarmDataEntity> alarmDataEntityList = alarmDataRepository.findAll(specification);
        List<AlarmBasicInfo> alarmBasicInfoList = new ArrayList<>();
        for (AlarmDataEntity alarmDataEntity : alarmDataEntityList) {
            String extraInfo = null;
            // 如果是实例失败告警，额外信息就传入jobStartDate
            String[] splitArray = alarmDataEntity.getId().split("_");
            if (AlarmType.BATCH_TASK_FAIL.alarmId().equals(alarmId) && splitArray.length > 1) {
                extraInfo = splitArray[1];
            }
            AlarmBasicInfo alarmBasicInfo = new AlarmBasicInfo(alarmDataEntity.getId(), alarmDataEntity.getName(),
                    extraInfo);
            if (!alarmBasicInfoList.contains(alarmBasicInfo)) {
                alarmBasicInfoList.add(alarmBasicInfo);
            }
        }
        return alarmBasicInfoList;
    }

    private List<AlarmBasicInfo> getFilteredAlarmData(List<AlarmBasicInfo> sourceAlarmBasicInfoList,
            List<AlarmBasicInfo> targetAlarmBasicInfoList) {
        List<AlarmBasicInfo> filteredAlarmBasicInfo = new ArrayList<>();
        for (AlarmBasicInfo sourceAlarmBasicInfo : sourceAlarmBasicInfoList) {
            if (targetAlarmBasicInfoList.stream()
                    .noneMatch(item -> sourceAlarmBasicInfo.getId().equals(item.getId()))) {
                filteredAlarmBasicInfo.add(sourceAlarmBasicInfo);
            }
        }
        return filteredAlarmBasicInfo;
    }

    private void addAlarm(AlarmRule alarmRule, AlarmBasicInfo alarmBasicInfo, Set<Alarm> alarmList, boolean isTrigger,
            List<AlarmAction> customAlarmActionList) {
        AlarmMsg alarmMsg = new AlarmMsg();
        alarmMsg.setId(alarmBasicInfo.getId());
        alarmMsg.setName(alarmBasicInfo.getName());
        alarmMsg.setAlarmId(alarmRule.alarmId());
        String extraInfo = alarmBasicInfo.getExtraInfo();
        // 新触发的反压告警如果是反压高，告警等级是High，其余都是Information
        String alarmLevel = AlarmLevel.HIGH.value().toLowerCase(Locale.ENGLISH).equals(extraInfo)
                ? AlarmLevel.HIGH.value()
                : AlarmLevel.INFORMATION.value();
        alarmMsg.setAlarmLevel(alarmLevel);
        // 如果是作业实例失败，则支持手动恢复
        boolean enableManualRecover = AlarmType.BATCH_TASK_FAIL.equals(alarmRule.type());
        Date occurDate = new Date();
        if (enableManualRecover && extraInfo != null) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                occurDate = simpleDateFormat.parse(extraInfo);
            } catch (ParseException e) {
                LOGGER.error("parse occurDate failed, parse date is: {}", extraInfo);
            }
        }
        alarmMsg.setEnableManualRecover(enableManualRecover);
        alarmMsg.setHostname(ZabbixUtil.getHostname());
        alarmMsg.setAlarmAttr(extraInfo);
        // 写数据库动作
        AlarmAction operateDBAction = isTrigger
                ? new OperateAlarmDataDBTriggerAction()
                : new OperateAlarmDataDBRecoverAction();
        // 默认发送到zabbix告警平台
        AlarmAction reportAlarmCenterAction = isTrigger
                ? new ReportAlarmCenterTriggerAction()
                : new ReportAlarmCenterRecoverAction();
        AlarmBuilder alarmBuilder = AlarmBuilder.newBuilder().alarmRule(alarmRule).alarmMsg(alarmMsg)
                .occurDate(occurDate).appendAction(operateDBAction);
        // 插入自定义告警动作
        if (customAlarmActionList != null && !customAlarmActionList.isEmpty()) {
            for (AlarmAction alarmAction : customAlarmActionList) {
                alarmBuilder.appendAction(alarmAction);
            }
        }
        Alarm alarm = alarmBuilder.appendAction(reportAlarmCenterAction).builder();
        alarmList.add(alarm);
    }
}