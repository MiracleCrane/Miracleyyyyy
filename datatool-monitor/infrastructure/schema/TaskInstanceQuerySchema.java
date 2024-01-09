/*
 * 文 件 名:  TaskInstanceQuerySchema.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.schema;

import com.huawei.smartcampus.datatool.entity.DtBatchJobEntity;
import com.huawei.smartcampus.datatool.entity.TaskInstanceEntity;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * task_instance查询
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/23]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public final class TaskInstanceQuerySchema {
    private TaskInstanceQuerySchema() {
    }

    public static Specification<TaskInstanceEntity> getTaskInstanceQueuedTimeoutSpecification() {
        return (Root<TaskInstanceEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteria) -> {
            // join task_instance表和batch_job表
            Join<TaskInstanceEntity, DtBatchJobEntity> join = root.join("dtBatchJobEntity", JoinType.LEFT);
            join.on(criteria.equal(join.get("id"), root.get("dagId")));
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(criteria.equal(root.get("state"), "queued"));
            predicateList.add(criteria.isNotNull(root.get("queuedDttm")));
            Predicate pre = criteria.and(predicateList.toArray(new Predicate[0]));
            criteriaQuery.where(pre);
            return pre;
        };
    }
}