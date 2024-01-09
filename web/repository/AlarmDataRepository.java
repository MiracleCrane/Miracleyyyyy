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

import com.huawei.smartcampus.datatool.entity.AlarmData;
import com.huawei.smartcampus.datatool.entity.AlarmDataEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 告警数据仓库
 *
 * @author cwx630741
 * @version [SmartCampus V100R001C00, 2021/10/20]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Transactional
public interface AlarmDataRepository
        extends JpaRepository<AlarmDataEntity, AlarmData>, JpaSpecificationExecutor<AlarmDataEntity> {
    /**
     * 恢复告警时，删除告警数据信息
     *
     * @param id id,作业（api）id
     * @param alarmId alarmId，告警id
     * @return 删除行数
     */
    int deleteAlarmDataEntityByIdAndAlarmId(String id, String alarmId);

    /**
     * 根据alarmId查询已存在的告警信息
     *
     * @param alarmId 告警id
     * @return 某类告警信息
     */
    List<AlarmDataEntity> findAlarmDataEntitiesByAlarmId(String alarmId);

    /**
     * 根据主键信息查询
     *
     * @param id 作业id
     * @param alarmId 告警id
     * @return 告警信息
     */
    List<AlarmDataEntity> findAlarmDataEntitiesByIdAndAlarmId(String id, String alarmId);

    /**
     * 根据alarmId和createDate删除
     *
     * @param alarmId 告警id
     * @param createDate 创建时间
     * @return 删除行数
     */
    int deleteAlarmDataEntitiesByAlarmIdEqualsAndCreateDateLessThan(String alarmId, Date createDate);

    /**
     * 根据alarmId和createDate查询
     *
     * @param alarmId 告警id
     * @param createDate 创建时间
     * @return 查询记录
     */
    List<AlarmDataEntity> findByAlarmIdAndCreateDateLessThan(String alarmId, Date createDate);
}
