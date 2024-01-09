/*
 * 文 件 名:  AlarmService.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service;

import com.huawei.smartcampus.datatool.model.BaseResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.CurrentDateRequest;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmdata.AlarmDataDetailQueryRequest;

/**
 * 告警服务
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/15]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public interface AlarmService {
    /**
     * 查询当前告警数量分布
     *
     * @return 查询结果
     */
    BaseResponse queryCurrentAlarmCount();

    /**
     * 查询当前告警详情
     *
     * @param alarmDataDetailQueryRequest 查询告警详情入参
     * @return 查询结果
     */
    BaseResponse queryCurrentAlarmDetail(AlarmDataDetailQueryRequest alarmDataDetailQueryRequest);

    /**
     * 查询近30日批处理作业实例执行超时告警统计数据
     *
     * @param currentDateRequest 当前时间请求
     * @return 查询结果
     */
    BaseResponse queryBatchTaskExecutionTimeoutStatistics(CurrentDateRequest currentDateRequest);

    /**
     * 查询近30日批处理作业实例失败告警统计数据
     *
     * @param currentDateRequest 当前时间请求
     * @return 查询结果
     */
    BaseResponse queryBatchTaskFailStatistics(CurrentDateRequest currentDateRequest);

    /**
     * 查询近30日批处理作业实例等待超时告警统计数据
     *
     * @param currentDateRequest 当前时间请求
     * @return 查询结果
     */
    BaseResponse queryBatchTaskWaitingTimeoutStatistics(CurrentDateRequest currentDateRequest);

    /**
     * 查询近30日流处理作业失败告警统计数据
     *
     * @param currentDateRequest 当前时间请求
     * @return 查询结果
     */
    BaseResponse queryStreamJobFailStatistics(CurrentDateRequest currentDateRequest);

    /**
     * 查询近30日流处理作业反压告警统计数据
     *
     * @param currentDateRequest 当前时间请求
     * @return 查询结果
     */
    BaseResponse queryStreamJobBackpressureStatistics(CurrentDateRequest currentDateRequest);
}