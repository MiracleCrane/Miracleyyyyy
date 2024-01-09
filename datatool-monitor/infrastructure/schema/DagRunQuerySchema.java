/*
 * 文 件 名:  DagRunQuerySchema.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/9
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.schema;

import com.huawei.smartcampus.datatool.entity.DagRunEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobNodeEntity;
import com.huawei.smartcampus.datatool.entity.TaskInstanceEntity;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.batchjob.DagRunState;

import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 * dag_run查询
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/9]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public final class DagRunQuerySchema {
    private DagRunQuerySchema() {
    }

    /**
     * 获取作业实例失败查询条件
     *
     * @param startTime 作业实例开始时间
     * @return 查询条件
     */
    public static Specification<DagRunEntity> getTaskFailSpecification(Timestamp startTime) {
        return (Root<DagRunEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteria) -> {
            criteriaQuery.distinct(true);
            // join dag_run和dag表
            Join<DagRunEntity, DtBatchJobEntity> joinBatchJob = root.join("dtBatchJobEntity", JoinType.LEFT);
            joinBatchJob.on(criteria.equal(joinBatchJob.get("id"), root.get("dagId")));
            // 子查询task_instance表确定是批处理的task
            List<Predicate> subPredicateAddList = new ArrayList<>();
            Subquery<TaskInstanceEntity> subquery = criteriaQuery.subquery(TaskInstanceEntity.class);
            Root<TaskInstanceEntity> subRoot = subquery.from(TaskInstanceEntity.class);
            Join<TaskInstanceEntity, DtBatchJobNodeEntity> joinBatchJobNode = subRoot.join("dtBatchJobNodeEntity",
                    JoinType.LEFT);
            joinBatchJobNode.on(criteria.equal(joinBatchJobNode.get("id"), subRoot.get("taskId")));
            subPredicateAddList.add(criteria.equal(subRoot.get("dagId"), root.get("dagId")));
            subPredicateAddList.add(criteria.isNotNull(joinBatchJobNode.get("id")));
            Predicate subPreAnd = criteria.and(subPredicateAddList.toArray(new Predicate[0]));
            subquery.where(subPreAnd).getRestriction();
            subquery.select(subRoot);
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteria.equal(root.get("state"), DagRunState.FAILED.value()));
            predicateList.add(criteria.greaterThan(root.get("startDate"), startTime));
            predicateList.add(criteria.exists(subquery));
            Predicate finalPre = criteria.and(predicateList.toArray(new Predicate[0]));
            criteriaQuery.where(finalPre);
            return finalPre;
        };
    }
}