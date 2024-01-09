/*
 * 文 件 名:  DagRunRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.repository;

import com.huawei.smartcampus.datatool.entity.DagRunEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/23]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Repository
public interface DagRunRepository extends JpaRepository<DagRunEntity, Integer>, JpaSpecificationExecutor<DagRunEntity> {
}