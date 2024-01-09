/*
 * 文 件 名:  ImportHistoryRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/12
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.repository;

import com.huawei.smartcampus.datatool.entity.ImportHistoryEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 导入历史表操作类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/12]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Repository
public interface ImportHistoryRepository extends JpaRepository<ImportHistoryEntity, String> {
    ImportHistoryEntity findImportHistoryEntityById(String id);

    Optional<ImportHistoryEntity> findById(String id);

    Page<ImportHistoryEntity> findAll(Specification<ImportHistoryEntity> specification, Pageable pageable);
}
