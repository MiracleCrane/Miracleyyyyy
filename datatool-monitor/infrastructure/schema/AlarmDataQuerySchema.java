/*
 * 文 件 名:  AlarmDataQuerySchema.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/25
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.schema;

import com.huawei.smartcampus.datatool.entity.AlarmDataEntity;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmType;
import com.huawei.smartcampus.datatool.utils.CommonUtil;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * alarm_data查询
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/25]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public final class AlarmDataQuerySchema {
    private AlarmDataQuerySchema() {
    }

    /**
     * 获取告警id获取当前存在的告警
     *
     * @param alarmId 告警id
     * @param startTime 告警开始时间
     * @return 告警数据
     */
    public static Specification<AlarmDataEntity> getCurrentAlarmDataByAlarmId(String alarmId, Date startTime) {
        return (Root<AlarmDataEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteria) -> {
            criteriaQuery.distinct(true);
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteria.equal(root.get("alarmId"), alarmId));
            if (startTime != null) {
                predicateList.add(criteria.greaterThan(root.get("createDate"), startTime));
            }
            Predicate predicate = criteria.and(predicateList.toArray(new Predicate[0]));
            criteriaQuery.where(predicate);
            return predicate;
        };
    }

    /**
     * 根据告警id和name获取当前存在的告警查询条件
     *
     * @param alarmId 告警id
     * @param name 名称
     * @return 查询条件
     */
    public static Specification<AlarmDataEntity> getCurrentAlarmQuerySpecification(String alarmId, String name) {
        return (Root<AlarmDataEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteria) -> {
            criteriaQuery.distinct(true);
            List<Predicate> predicateList = new ArrayList<>();
            if (!StringUtils.isEmpty(alarmId)) {
                predicateList.add(criteria.equal(root.get("alarmId"), alarmId));
            }
            if (!StringUtils.isEmpty(name)) {
                Path<String> namePath = root.get("name");
                // 实例失败告警需要将字段值去掉时间戳后再模糊查询
                List<Predicate> predicateTaskFailAndList = new ArrayList<>();
                Expression<String> taskFailJobName = criteria.substring(namePath, criteria.locate(namePath, namePath),
                        criteria.diff(criteria.length(namePath), 15));
                // 判断实例失败告警条件在先，防止substring中长度为负数sql报错
                predicateTaskFailAndList.add(criteria.equal(root.get("alarmId"), AlarmType.BATCH_TASK_FAIL.alarmId()));
                predicateTaskFailAndList.add(criteria.like(criteria.lower(taskFailJobName),
                        "%" + CommonUtil.parameterEscape(name.toLowerCase(Locale.ROOT)) + "%"));
                Predicate predicateTaskFail = criteria.and(predicateTaskFailAndList.toArray(new Predicate[0]));
                // 非实例失败告警直接根据当前字段值模糊查询
                List<Predicate> predicateNotTaskFailAndList = new ArrayList<>();
                predicateNotTaskFailAndList.add(criteria.like(criteria.lower(namePath),
                        "%" + CommonUtil.parameterEscape(name.toLowerCase(Locale.ROOT)) + "%"));
                predicateNotTaskFailAndList
                        .add(criteria.notEqual(root.get("alarmId"), AlarmType.BATCH_TASK_FAIL.alarmId()));
                Predicate predicateNotTaskFail = criteria.and(predicateNotTaskFailAndList.toArray(new Predicate[0]));
                List<Predicate> predicateOrList = new ArrayList<>();
                predicateOrList.add(predicateNotTaskFail);
                predicateOrList.add(predicateTaskFail);
                Predicate predicateOr = criteria.or(predicateOrList.toArray(new Predicate[0]));
                predicateList.add(predicateOr);
            }
            Predicate predicate = criteria.and(predicateList.toArray(new Predicate[0]));
            criteriaQuery.where(predicate);
            return predicate;
        };
    }
}