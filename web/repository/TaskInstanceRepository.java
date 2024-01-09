/*
 * 文 件 名:  TaskInstanceRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.repository;

import com.huawei.smartcampus.datatool.entity.TaskInstance;
import com.huawei.smartcampus.datatool.entity.TaskInstanceEntity;
import com.huawei.smartcampus.datatool.vo.BatchRunningJobInstanceCountVo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * task instance repository
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/23]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Repository
public interface TaskInstanceRepository
        extends JpaRepository<TaskInstanceEntity, TaskInstance>, JpaSpecificationExecutor<TaskInstanceEntity> {
    @Query(nativeQuery = true, value = "select DATE_FORMAT(dr.start_date, concat('%Y-%m-%d %H:', floor(substring"
            + "(DATE_FORMAT(dr.start_date, '%Y-%m-%d %H:%i:%s'), 15, 2)/ ?1 ) * ?1)) as timeInterval, "
            + "count(dr.start_date) as count from task_instance as ti left join dag_run as dr on ti.dag_id"
            + " = dr.dag_id and ti.run_id = dr.run_id left join dt_batch_job_node as bjn on bjn.id = ti.task_id where"
            + " dr.start_date >= ?2 and dr.start_date <= ?3 and bjn.id is not null group by timeInterval"
            + " order by timeInterval asc")
    List<BatchRunningJobInstanceCountVo> findRunningJobInstanceCountByStartDateAndInterval(int interval,
            Timestamp startDate, Timestamp endDate);
}