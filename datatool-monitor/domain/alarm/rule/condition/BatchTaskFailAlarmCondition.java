/*
 * 文 件 名:  BatchTaskFailAlarmCondition.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.condition;

import com.huawei.smartcampus.datatool.entity.AlarmDataEntity;
import com.huawei.smartcampus.datatool.entity.DagRunEntity;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmBasicInfo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmHelper;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.AlarmRule;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.AlarmAction;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.OperateBatchTaskFailDBRecoverAction;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.OperateBatchTaskFailDBTriggerAction;
import com.huawei.smartcampus.datatool.monitor.infrastructure.schema.DagRunQuerySchema;
import com.huawei.smartcampus.datatool.repository.AlarmDataRepository;
import com.huawei.smartcampus.datatool.repository.DagRunRepository;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;
import com.huawei.smartcampus.datatool.utils.TimeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * 批量作业失败告警条件
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/2]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class BatchTaskFailAlarmCondition implements AlarmCondition {
    private static final Logger LOGGER = LoggerFactory.getLogger("Alarm");

    private static final Long TASK_FAIL_ALARM_DAY_RANGE = 7L;

    private final DagRunRepository dagRunRepository = SpringContextHelper.getBean(DagRunRepository.class);

    private final AlarmDataRepository alarmDataRepository = SpringContextHelper.getBean(AlarmDataRepository.class);

    private final AlarmHelper alarmHelper = SpringContextHelper.getBean(AlarmHelper.class);

    @Override
    public Set<Alarm> evaluate(AlarmRule alarmRule) {
        // 当前的UTC时间
        Instant startDate = Instant.now().minus(Duration.ofDays(TASK_FAIL_ALARM_DAY_RANGE));
        try {
            // 删除7天之外的数据，实际多保留一个小时以避免影响临界数据
            Instant actualStartDate = startDate.minus(Duration.ofHours(1));
            Date deleteStartDate = new Date(actualStartDate.toEpochMilli());
            List<AlarmDataEntity> alarmDataEntityList = alarmDataRepository
                    .findByAlarmIdAndCreateDateLessThan(alarmRule.alarmId(), deleteStartDate);
            if (!alarmDataEntityList.isEmpty()) {
                alarmDataRepository.deleteAlarmDataEntitiesByAlarmIdEqualsAndCreateDateLessThan(alarmRule.alarmId(),
                        deleteStartDate);
            }
        } catch (Exception e) {
            LOGGER.error("delete expired alarm failed", e);
        }
        Timestamp startDateTimestamp = Timestamp.from(startDate);
        // 获取当前实例失败告警列表A
        List<AlarmBasicInfo> newAlarmBasicInfoList = queryFailTaskInstance(startDateTimestamp);
        Date alarmStartDate = new Date(startDate.toEpochMilli());
        // 获取alarm_data表中已存在的实例失败告警列表B
        List<AlarmBasicInfo> currentAlarmBasicInfoList = alarmHelper.queryCurrentAlarmByAlarmId(alarmRule.alarmId(),
                alarmStartDate);
        List<AlarmAction> alarmTriggerActions = new ArrayList<>();
        List<AlarmAction> alarmRecoverActions = new ArrayList<>();
        alarmTriggerActions.add(new OperateBatchTaskFailDBTriggerAction());
        alarmRecoverActions.add(new OperateBatchTaskFailDBRecoverAction());
        return alarmHelper.fillAlarmList(alarmRule, newAlarmBasicInfoList, currentAlarmBasicInfoList,
                alarmTriggerActions, alarmRecoverActions);
    }

    private List<AlarmBasicInfo> queryFailTaskInstance(Timestamp startDate) {
        Specification<DagRunEntity> specification = DagRunQuerySchema.getTaskFailSpecification(startDate);
        List<DagRunEntity> dagRunEntities = dagRunRepository.findAll(specification);
        List<AlarmBasicInfo> alarmBasicInfoList = new ArrayList<>();
        for (DagRunEntity dagRunEntity : dagRunEntities) {
            // 转成当前时区的字符串
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TimeUtil.NO_HYPHEN_DATE_FORMAT);
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            String startDateStr = simpleDateFormat.format(dagRunEntity.getStartDate());
            // 作业实例失败时id为dag_id_start_date，name为description_start_date
            String id = dagRunEntity.getDagId() + "_" + startDateStr;
            String name = dagRunEntity.getDtBatchJobEntity().getName() + "_" + startDateStr;
            AlarmBasicInfo alarmBasicInfo = new AlarmBasicInfo(id, name, startDateStr);
            if (!alarmBasicInfoList.contains(alarmBasicInfo)) {
                alarmBasicInfoList.add(alarmBasicInfo);
            }
        }
        return alarmBasicInfoList;
    }
}