/*
 * 文 件 名:  StreamJobRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.repository;

import com.huawei.smartcampus.datatool.entity.StreamJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 流处理作业仓库
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Repository
public interface StreamJobRepository
        extends JpaRepository<StreamJobEntity, String>, JpaSpecificationExecutor<StreamJobEntity> {
    /**
     * 批量删除作业
     *
     * @param ids id数组
     */
    void deleteStreamJobEntitiesByIdIn(List<String> ids);

    /**
     * 根据作业id查询作业列表
     *
     * @param ids id数组
     * @return 作业列表
     */
    List<StreamJobEntity> findStreamJobEntitiesByIdIn(List<String> ids);

    /**
     * 根据名称查找作业
     *
     * @param name 作业名
     * @return 作业
     */
    StreamJobEntity findByName(String name);

    /**
     * 根据名称查找作业
     *
     * @param name 作业名
     * @return 作业
     */
    StreamJobEntity findStreamJobEntityByName(String name);

    /**
     * 根据作业状态查找作业
     *
     * @param state 作业状态
     * @return 作业列表
     */
    List<StreamJobEntity> findByState(String state);

    /**
     * 查询是否有创建失败的保存点
     *
     * @return 所有创建保存点失败的对象
     */
    List<StreamJobEntity> findStreamJobEntitiesByRequestIdIsNotNullAndSavepointPathIsNotNull();

    /**
     * 根据flink_id查询stream_job
     *
     * @param flinkId flink_id
     * @return stream_job对象
     */
    StreamJobEntity findStreamJobEntityByFlinkId(String flinkId);
}
