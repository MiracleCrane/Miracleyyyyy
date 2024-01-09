/*
 * 文 件 名:  DtSystemConfigurationRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/10/21
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.repository;

import com.huawei.smartcampus.datatool.entity.SystemConfigurationEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 系统配置
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/21]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Repository
public interface DtSystemConfigurationRepository
        extends JpaRepository<SystemConfigurationEntity, String>, JpaSpecificationExecutor<SystemConfigurationEntity> {
    List<SystemConfigurationEntity> findAllByName(String name);

    Optional<SystemConfigurationEntity> findByNameAndKey(String name, String key);
}