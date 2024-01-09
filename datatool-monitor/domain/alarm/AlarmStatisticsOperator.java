/*
 * 文 件 名:  AlarmStatisticsOperator.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/26
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm;

import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.batch.TaskExecutionTimeoutStat;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.batch.TaskFailStat;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.batch.TaskWaitingTimeoutStat;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.stream.StreamJobBackpressureStat;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.stream.StreamJobFailStat;

import java.sql.Timestamp;
import java.util.List;

/**
 * 告警统计数据抽象接口
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/26]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public interface AlarmStatisticsOperator {
    /**
     * 获取查询批处理作业执行超时统计数据
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 查询结果
     */
    List<TaskExecutionTimeoutStat> getAlarmTaskExecutionTimeoutStatistics(Timestamp startDate, Timestamp endDate);

    /**
     * 获取查询批处理作业失败统计数据
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 查询结果
     */
    List<TaskFailStat> getAlarmTaskFailStatistics(Timestamp startDate, Timestamp endDate);

    /**
     * 获取查询批处理作业等待超时统计数据
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 查询结果
     */
    List<TaskWaitingTimeoutStat> getAlarmTaskWaitingTimeoutStatistics(Timestamp startDate, Timestamp endDate);

    /**
     * 获取查询流处理作业失败统计数据
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 查询结果
     */
    List<StreamJobFailStat> getAlarmStreamJobFailStatistics(Timestamp startDate, Timestamp endDate);

    /**
     * 获取查询流处理作业失败统计数据
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 查询结果
     */
    List<StreamJobBackpressureStat> getAlarmStreamJobBackpressureStatistics(Timestamp startDate, Timestamp endDate);
}