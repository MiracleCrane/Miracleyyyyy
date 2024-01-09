/*
 * 文 件 名:  DtAlarmStreamJobFailRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/25
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.repository;

import com.huawei.smartcampus.datatool.entity.DtAlarmStreamJobFailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 流处理作业失败告警仓库
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/25]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Repository
public interface DtAlarmStreamJobFailRepository extends JpaRepository<DtAlarmStreamJobFailEntity, String>,
        JpaSpecificationExecutor<DtAlarmStreamJobFailEntity> {
    /**
     * 通过jobId、jobName和endDate为null查询
     *
     * @param jobId 作业id
     * @param jobName 作业名称
     * @return 查询结果
     */
    List<DtAlarmStreamJobFailEntity> findByJobIdAndJobNameAndEndDateIsNull(String jobId, String jobName);
}