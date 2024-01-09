/*
 * 文 件 名:  BatchJobGateway.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.gateway;

import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.BatchJobDetail;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata.BatchJobAlarmThreshold;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.batchjob.BatchJobInstanceStateCountVo;
import com.huawei.smartcampus.datatool.vo.BatchRunningJobInstanceCountVo;

import java.sql.Timestamp;
import java.util.List;

/**
 * 批处理数据抽象接口
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public interface BatchJobGateway {
    /**
     * 查询作业实例状态数量分布
     *
     * @param startDate 查询起始时间
     * @param endDate 查询终止时间
     * @return 作业实例状态数量分布列表
     */
    List<BatchJobInstanceStateCountVo> queryBatchJobInstanceStateCount(Timestamp startDate, Timestamp endDate);

    /**
     * 查询作业实例等待状态数量
     *
     * @param startDate 查询起始时间
     * @param endDate 查询终止时间
     * @return 业实例等待状态数量
     */
    List<Long> queryBatchJobInstanceStateWaitingCount(Timestamp startDate, Timestamp endDate);

    /**
     * 查询作业实例运行数量分布
     *
     * @param interval 时间间隔
     * @param startDate 查询起始时间
     * @param endDate 查询终止时间
     * @return 作业实例运行数量分布列表
     */
    List<BatchRunningJobInstanceCountVo> queryBatchRunningJobInstanceCount(Integer interval, Timestamp startDate, Timestamp endDate);

    /**
     * 查询运行中的作业id
     *
     * @return 作业id列表
     */
    List<String> queryBatchRunningJobId();

    /**
     * 查询批处理作业告警阈值
     *
     * @return 查询结果
     */
    List<BatchJobAlarmThreshold> queryBatchJobAlarmThreshold();

    /**
     * 获取作业目录列表
     *
     * @param id 作业id
     * @return 作业目录列表
     */
    List<String> getJobFullDir(String id);

    /**
     * 获取批处理作业明细
     *
     * @return 批处理作业明细
     */
    List<BatchJobDetail> getBatchJobDetail();
}