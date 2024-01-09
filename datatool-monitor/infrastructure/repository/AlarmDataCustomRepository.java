/*
 * 文 件 名:  AlarmDataFactory.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository;

import com.huawei.smartcampus.datatool.entity.AlarmDataEntity;
import com.huawei.smartcampus.datatool.entity.DagRunEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobNodeEntity;
import com.huawei.smartcampus.datatool.entity.TaskInstanceEntity;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmDataOperator;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata.AlarmDataCountVo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata.TaskExecutionTimeoutVo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata.TaskWaitingTimeoutVo;
import com.huawei.smartcampus.datatool.monitor.infrastructure.schema.AlarmDataQuerySchema;
import com.huawei.smartcampus.datatool.repository.AlarmDataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 * 告警数据自定义仓库
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/16]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Component
public class AlarmDataCustomRepository implements AlarmDataOperator {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AlarmDataRepository alarmDataRepository;

    @Override
    public List<AlarmDataCountVo> queryCurrentAlarmCount() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AlarmDataCountVo> criteriaQuery = criteriaBuilder.createQuery(AlarmDataCountVo.class);
        Root<AlarmDataEntity> root = criteriaQuery.from(AlarmDataEntity.class);
        criteriaQuery.multiselect(root.get("alarmId").alias("alarmId"),
                criteriaBuilder.count(root.get("alarmId")).alias("count")).groupBy(root.get("alarmId"));
        TypedQuery<AlarmDataCountVo> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Override
    public Page<AlarmDataEntity> queryCurrentAlarmDetail(int pageSize, int pageIndex, String alarmId, String name) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createDate"));
        Sort sort = Sort.by(orders);
        Pageable pageable = PageRequest.of(pageIndex > 0 ? pageIndex - 1 : pageIndex, pageSize, sort);
        Specification<AlarmDataEntity> specification = AlarmDataQuerySchema.getCurrentAlarmQuerySpecification(alarmId,
                name);
        Page<AlarmDataEntity> alarmDataPage = alarmDataRepository.findAll(specification, pageable);
        int totalPage = alarmDataPage.getTotalPages();
        // 如果当前页数大于查询出的总页数，则取最后一页查询结果
        if (totalPage > 0 && totalPage < pageIndex) {
            pageable = PageRequest.of(totalPage - 1, pageSize, sort);
            alarmDataPage = alarmDataRepository.findAll(pageable);
        }
        return alarmDataPage;
    }

    @Override
    public List<TaskWaitingTimeoutVo> getTaskWaitingTimeoutQueryResult() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskWaitingTimeoutVo> criteriaQuery = criteriaBuilder.createQuery(TaskWaitingTimeoutVo.class);
        Root<TaskInstanceEntity> root = criteriaQuery.from(TaskInstanceEntity.class);
        // 关联task_instance表和dag_run表、dt_batch_job表、dt_batch_job_node表
        Join<TaskInstanceEntity, DtBatchJobEntity> joinDtBatchJob = root.join("dtBatchJobEntity", JoinType.LEFT);
        Join<TaskInstanceEntity, DagRunEntity> joinDagRun = root.join("dagRunEntity", JoinType.LEFT);
        Join<TaskInstanceEntity, DtBatchJobNodeEntity> joinDtBatchJobNode = root.join("dtBatchJobNodeEntity",
                JoinType.LEFT);
        Subquery<Timestamp> subquery = criteriaQuery.subquery(Timestamp.class);
        Root<TaskInstanceEntity> subRoot = subquery.from(TaskInstanceEntity.class);
        Join<TaskInstanceEntity, DtBatchJobNodeEntity> subJoinDtBatchJobNode = subRoot.join("dtBatchJobNodeEntity",
                JoinType.LEFT);
        List<Predicate> predicateSubAndList = new ArrayList<>();
        predicateSubAndList.add(criteriaBuilder.isNotNull(subRoot.get("startDate")));
        predicateSubAndList.add(criteriaBuilder.isNotNull(subJoinDtBatchJobNode.get("id")));
        Predicate subPredicate = criteriaBuilder.and(predicateSubAndList.toArray(new Predicate[0]));
        // 子查询查询出所有task_instance的最近执行时间
        subquery.select(criteriaBuilder.greatest(subRoot.<Timestamp>get("startDate"))).where(subPredicate)
                .groupBy(subRoot.get("dagId"));
        // 拼接where条件
        List<Predicate> predicateFinalList = new ArrayList<>();
        predicateFinalList.add(criteriaBuilder.isNotNull(joinDtBatchJobNode.get("id")));
        predicateFinalList.add(criteriaBuilder.isNotNull(root.get("startDate")));
        predicateFinalList.add(criteriaBuilder.isNotNull(joinDagRun.get("startDate")));
        predicateFinalList.add(criteriaBuilder.equal(joinDtBatchJob.get("state"), 1));
        predicateFinalList.add(root.get("startDate").in(subquery));
        Predicate finalPredicate = criteriaBuilder.and(predicateFinalList.toArray(new Predicate[0]));
        criteriaQuery.multiselect(joinDtBatchJob.get("id").alias("jobId"), joinDtBatchJob.get("name").alias("name"),
                root.get("startDate").alias("taskStartDate"), joinDagRun.get("startDate").alias("dagRunStartDate"))
                .where(finalPredicate).orderBy(criteriaBuilder.desc(root.get("startDate")));
        TypedQuery<TaskWaitingTimeoutVo> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Override
    public List<TaskExecutionTimeoutVo> getTaskExecutionTimeoutQueryResult(String jobId, double threshold) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskExecutionTimeoutVo> criteriaQuery = criteriaBuilder.createQuery(TaskExecutionTimeoutVo.class);
        // 创建子查询，查询dag_id的最近执行时间
        Subquery<Timestamp> subQuery = criteriaQuery.subquery(Timestamp.class);
        Root<TaskInstanceEntity> subRoot = subQuery.from(TaskInstanceEntity.class);
        Join<TaskInstanceEntity, DtBatchJobNodeEntity> subJoinDtBatchJobNode = subRoot.join("dtBatchJobNodeEntity",
                JoinType.LEFT);
        List<Predicate> subPredicateList = new ArrayList<>();
        subPredicateList.add(criteriaBuilder.equal(subRoot.get("dagId"), jobId));
        subPredicateList.add(criteriaBuilder.equal(subRoot.get("state"), "success"));
        subPredicateList.add(criteriaBuilder.isNotNull(subJoinDtBatchJobNode.get("id")));
        Predicate subPreAnd = criteriaBuilder.and(subPredicateList.toArray(new Predicate[0]));
        subQuery.select(criteriaBuilder.greatest(subRoot.<Timestamp>get("startDate"))).where(subPreAnd);
        // 创建主查询，根据duration和子查询结果判断，并join dt_batch_job表
        Root<TaskInstanceEntity> root = criteriaQuery.from(TaskInstanceEntity.class);
        Join<TaskInstanceEntity, DtBatchJobNodeEntity> joinDtBatchJobNode = root.join("dtBatchJobNodeEntity",
                JoinType.LEFT);
        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(criteriaBuilder.greaterThan(root.get("duration"), threshold));
        predicateList.add(criteriaBuilder.equal(root.get("dagId"), jobId));
        predicateList.add(criteriaBuilder.isNotNull(joinDtBatchJobNode.get("id")));
        predicateList.add(root.get("startDate").in(subQuery));
        Predicate predicateFinal = criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        Join<TaskInstanceEntity, DtBatchJobEntity> joinBatchJob = root.join("dtBatchJobEntity", JoinType.LEFT);
        criteriaQuery.multiselect(root.get("dagId").alias("jobId"), joinBatchJob.get("name").alias("jobName"),
                root.get("duration").alias("duration")).where(predicateFinal);
        TypedQuery<TaskExecutionTimeoutVo> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }
}