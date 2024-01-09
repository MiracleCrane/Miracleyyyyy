/*
 * 文 件 名:  DtBatchJobRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/21
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.repository;

import com.huawei.smartcampus.datatool.entity.DtBatchJobEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作业操作类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/21]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Repository
public interface DtBatchJobRepository extends JpaRepository<DtBatchJobEntity, String> {
    List<DtBatchJobEntity> findDtBatchJobEntitiesByIdIn(List<String> ids);

    DtBatchJobEntity findDtBatchJobEntityByName(String name);

    List<DtBatchJobEntity> findByState(Boolean state);

    long countByState(boolean state);
}
