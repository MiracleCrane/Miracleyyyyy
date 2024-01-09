/*
 * 文 件 名:  AlarmDataRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  yWX895180
 * 修改时间： 2021/7/30
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.repository;

import com.huawei.smartcampus.datatool.entity.LocalAuditLogEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 本地审计日志
 *
 * @author cwx630741
 * @version [SmartCampus V100R001C00, 2021/10/20]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Transactional
public interface LocalAuditLogRepository extends JpaRepository<LocalAuditLogEntity, Long> {
    // jpql里不支持limit ，所以只能用原生sql了先
    @Query(value = "select * from dt_local_audit_log order by created_date asc limit ?1", nativeQuery = true)
    List<LocalAuditLogEntity> batchSelect(int count);
}
