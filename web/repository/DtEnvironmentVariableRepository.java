/*
 * 文 件 名:  EnvironmentVariableRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.repository;

import com.huawei.smartcampus.datatool.entity.DtEnvironmentVariableEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 环境变量操作表
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/15]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Repository
public interface DtEnvironmentVariableRepository extends JpaRepository<DtEnvironmentVariableEntity, String> {
    DtEnvironmentVariableEntity findDtEnvironmentVariableEntityByKey(String key);

    List<DtEnvironmentVariableEntity> findDtEnvironmentVariableEntitiesByIdIn(List<String> ids);

    /**
     * 根据key值查找环境变量
     *
     * @param keys key值数组
     * @return 环境变量列表
     */
    List<DtEnvironmentVariableEntity> findDtEnvironmentVariableEntitiesByKeyIn(List<String> keys);
}
