/*
 * 文 件 名:  AlarmDataGateway.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm;

import com.huawei.smartcampus.datatool.entity.AlarmDataEntity;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata.AlarmDataCountVo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata.TaskExecutionTimeoutVo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata.TaskWaitingTimeoutVo;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 告警数据表抽象接口
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/16]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public interface AlarmDataOperator {
    /**
     * 获取当前告警数量分布
     *
     * @return 当前告警数量分布列表
     */
    List<AlarmDataCountVo> queryCurrentAlarmCount();

    /**
     * 分页查询当前告警详情
     *
     * @param pageSize 每页数量
     * @param pageIndex 当前页数
     * @param alarmId 告警id
     * @param name 名称
     * @return 当前告警详情列表
     */
    Page<AlarmDataEntity> queryCurrentAlarmDetail(int pageSize, int pageIndex, String alarmId, String name);

    /**
     * 获取查询等待运行作业结果
     *
     * @return 查询结果
     */
    List<TaskWaitingTimeoutVo> getTaskWaitingTimeoutQueryResult();

    /**
     * 获取查询作业执行超时结果
     *
     * @param jobId 作业id
     * @param threshold 告警阈值
     * @return 查询结果
     */
    List<TaskExecutionTimeoutVo> getTaskExecutionTimeoutQueryResult(String jobId, double threshold);
}