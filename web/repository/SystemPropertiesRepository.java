/*
 * 文 件 名:  SystemPropertiesRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  w00318695
 * 修改时间： 2022/6/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.repository;

import com.huawei.smartcampus.datatool.entity.SystemPropertyEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统变量jpa
 * <功能详细描述>
 *
 * @author w00318695
 * @version [SmartCampus V100R001C00, 2022/6/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Repository
public interface SystemPropertiesRepository extends JpaRepository<SystemPropertyEntity, String> {
    /**
     * 根据系统变量名列表获取系统变量
     *
     * @param names 变量名列表
     * @return 属性列表
     */
    List<SystemPropertyEntity> findSystemPropertyEntityByNameIn(List<String> names);
}
