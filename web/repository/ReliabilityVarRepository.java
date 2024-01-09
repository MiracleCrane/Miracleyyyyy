/*
 * 文 件 名:  ReliabilityVarRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.repository;

import com.huawei.smartcampus.datatool.entity.ReliabilityVariableEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public interface ReliabilityVarRepository extends JpaRepository<ReliabilityVariableEntity, Integer> {
    /**
     * 根据key查询变量
     *
     * @param varKey 变量key值
     * @return 变量
     */
    Optional<ReliabilityVariableEntity> findByVarKey(String varKey);
}
