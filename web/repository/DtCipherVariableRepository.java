/*
 * 文 件 名:  CipherVariableRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2021/2/9
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.repository;

import com.huawei.smartcampus.datatool.entity.DtCipherVariableEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 密码箱操作类
 *
 * @author z00569896
 * @version [SmartCampus V100R001C00, 2021/2/9]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Repository
public interface DtCipherVariableRepository extends JpaRepository<DtCipherVariableEntity, Integer> {
    /**
     * 查询密码箱
     *
     * @param keys 密码箱key
     * @return 密码箱列表
     */
    List<DtCipherVariableEntity> findCipherVariableEntitiesByKeyIn(List<String> keys);

    /**
     * 查询密码箱
     *
     * @param ids 密码箱id
     * @return 密码箱列表
     */
    List<DtCipherVariableEntity> findCipherVariableEntitiesByIdIn(List<String> ids);

    /**
     * 根据key查询密码
     *
     * @param key 密码箱key
     * @return {@link DtCipherVariableEntity}
     */
    DtCipherVariableEntity findCipherVariableEntityByKey(String key);
}
