/*
 * 文 件 名:  AlarmStatisticsCustomRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/26
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository;

import com.huawei.smartcampus.datatool.entity.DtAlarmBatchTaskExecutionTimeoutEntity;
import com.huawei.smartcampus.datatool.entity.DtAlarmBatchTaskFailEntity;
import com.huawei.smartcampus.datatool.entity.DtAlarmBatchTaskWaitingTimeoutEntity;
import com.huawei.smartcampus.datatool.entity.DtAlarmStreamJobBackpressureEntity;
import com.huawei.smartcampus.datatool.entity.DtAlarmStreamJobFailEntity;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmStatisticsOperator;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.batch.TaskExecutionTimeoutStat;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.batch.TaskFailStat;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.batch.TaskWaitingTimeoutStat;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.stream.StreamJobBackpressureStat;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.stream.StreamJobFailStat;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

/**
 * 告警统计数据自定义仓库
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/26]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Component
public class AlarmStatisticsCustomRepository implements AlarmStatisticsOperator {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TaskExecutionTimeoutStat> getAlarmTaskExecutionTimeoutStatistics(Timestamp startDate,
            Timestamp endDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskExecutionTimeoutStat> criteriaQuery = criteriaBuilder
                .createQuery(TaskExecutionTimeoutStat.class);
        Root<DtAlarmBatchTaskExecutionTimeoutEntity> root = criteriaQuery
                .from(DtAlarmBatchTaskExecutionTimeoutEntity.class);
        Expression<Long> countNumber = criteriaBuilder.count(root.get("jobId"));
        Expression<Double> avgTime = criteriaBuilder.avg(root.get("executeDuration"));
        Expression<Double> maxTime = criteriaBuilder.greatest(root.<Double>get("executeDuration"));
        criteriaQuery
                .multiselect(root.get("jobId").alias("jobId"), root.get("jobName").alias("jobName"),
                        countNumber.alias("executionTimeoutCount"), avgTime.alias("avgExecutionTime"),
                        maxTime.alias("maxExecutionTime"))
                .where(criteriaBuilder.between(root.get("createDate"), startDate, endDate))
                .groupBy(root.get("jobId"), root.get("jobName")).orderBy(criteriaBuilder.desc(countNumber),
                        criteriaBuilder.desc(avgTime), criteriaBuilder.desc(maxTime));
        TypedQuery<TaskExecutionTimeoutStat> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setMaxResults(10);
        return typedQuery.getResultList();
    }

    @Override
    public List<TaskFailStat> getAlarmTaskFailStatistics(Timestamp startDate, Timestamp endDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskFailStat> criteriaQuery = criteriaBuilder.createQuery(TaskFailStat.class);
        Root<DtAlarmBatchTaskFailEntity> root = criteriaQuery.from(DtAlarmBatchTaskFailEntity.class);
        Expression<Long> countNumber = criteriaBuilder.count(root.get("jobId"));
        Expression<String> maxDate = criteriaBuilder.greatest(root.<String>get("jobStartDate"));
        criteriaQuery
                .multiselect(root.get("jobId").alias("jobId"), root.get("jobName").alias("jobName"),
                        countNumber.alias("taskFailCount"), maxDate.alias("maxCreateDate"))
                .where(criteriaBuilder.between(root.get("createDate"), startDate, endDate))
                .groupBy(root.get("jobId"), root.get("jobName"))
                .orderBy(criteriaBuilder.desc(countNumber), criteriaBuilder.desc(maxDate));
        TypedQuery<TaskFailStat> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setMaxResults(10);
        return typedQuery.getResultList();
    }

    @Override
    public List<TaskWaitingTimeoutStat> getAlarmTaskWaitingTimeoutStatistics(Timestamp startDate, Timestamp endDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskWaitingTimeoutStat> criteriaQuery = criteriaBuilder.createQuery(TaskWaitingTimeoutStat.class);
        Root<DtAlarmBatchTaskWaitingTimeoutEntity> root = criteriaQuery
                .from(DtAlarmBatchTaskWaitingTimeoutEntity.class);
        Expression<Long> countNumber = criteriaBuilder.count(root.get("jobId"));
        Expression<Double> avgTime = criteriaBuilder.avg(root.get("delay"));
        Expression<Double> maxTime = criteriaBuilder.greatest(root.<Double>get("delay"));
        criteriaQuery
                .multiselect(root.get("jobId").alias("jobId"), root.get("jobName").alias("jobName"),
                        countNumber.alias("waitingTimeoutCount"), avgTime.alias("avgWaitingTime"),
                        maxTime.alias("maxWaitingTime"))
                .where(criteriaBuilder.between(root.get("createDate"), startDate, endDate))
                .groupBy(root.get("jobId"), root.get("jobName")).orderBy(criteriaBuilder.desc(countNumber),
                        criteriaBuilder.desc(avgTime), criteriaBuilder.desc(maxTime));
        TypedQuery<TaskWaitingTimeoutStat> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setMaxResults(10);
        return typedQuery.getResultList();
    }

    @Override
    public List<StreamJobFailStat> getAlarmStreamJobFailStatistics(Timestamp startDate, Timestamp endDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StreamJobFailStat> criteriaQuery = criteriaBuilder.createQuery(StreamJobFailStat.class);
        Root<DtAlarmStreamJobFailEntity> root = criteriaQuery.from(DtAlarmStreamJobFailEntity.class);
        Expression<Long> countNumber = criteriaBuilder.count(root.get("jobId"));
        Expression<Timestamp> maxDate = criteriaBuilder.greatest(root.<Timestamp>get("createDate"));
        criteriaQuery
                .multiselect(root.get("jobId").alias("jobId"), root.get("jobName").alias("jobName"),
                        countNumber.alias("streamJobFailCount"), maxDate.alias("maxCreateDate"))
                .where(criteriaBuilder.between(root.get("createDate"), startDate, endDate))
                .groupBy(root.get("jobId"), root.get("jobName"))
                .orderBy(criteriaBuilder.desc(countNumber), criteriaBuilder.desc(maxDate));
        TypedQuery<StreamJobFailStat> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setMaxResults(10);
        return typedQuery.getResultList();
    }

    @Override
    public List<StreamJobBackpressureStat> getAlarmStreamJobBackpressureStatistics(Timestamp startDate,
            Timestamp endDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StreamJobBackpressureStat> criteriaQuery = criteriaBuilder
                .createQuery(StreamJobBackpressureStat.class);
        Root<DtAlarmStreamJobBackpressureEntity> root = criteriaQuery.from(DtAlarmStreamJobBackpressureEntity.class);
        Expression<Long> countNumber = criteriaBuilder.count(root.get("jobId"));
        Expression<Double> avgTime = criteriaBuilder.avg(root.<Double>get("recoverTime"));
        // recoverTime为null不会计入avg中，但是count会计算
        criteriaQuery
                .multiselect(root.get("jobId").alias("jobId"), root.get("jobName").alias("jobName"),
                        countNumber.alias("streamJobBackpressureCount"), avgTime.alias("avgRecoverTime"))
                .where(criteriaBuilder.between(root.get("createDate"), startDate, endDate))
                .groupBy(root.get("jobId"), root.get("jobName"))
                .orderBy(criteriaBuilder.desc(countNumber), criteriaBuilder.desc(avgTime));
        TypedQuery<StreamJobBackpressureStat> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setMaxResults(10);
        return typedQuery.getResultList();
    }
}