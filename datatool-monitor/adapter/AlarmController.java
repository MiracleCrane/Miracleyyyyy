/*
 * 文 件 名:  AlarmController.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.adapter;

import com.huawei.smartcampus.datatool.model.BaseResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.AlarmService;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.CurrentDateRequest;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmdata.AlarmDataDetailQueryRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 告警外部接口
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/15]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@RestController
@RequestMapping(value = "/v1/alarms")
public class AlarmController {
    @Autowired
    private AlarmService alarmService;

    @GetMapping(value = "/current/count")
    public BaseResponse queryCurrentAlarmCount() {
        return alarmService.queryCurrentAlarmCount();
    }

    @GetMapping(value = "/current/detail")
    public BaseResponse queryCurrentAlarmDetail(@Valid AlarmDataDetailQueryRequest alarmDataDetailQueryRequest) {
        return alarmService.queryCurrentAlarmDetail(alarmDataDetailQueryRequest);
    }

    @GetMapping(value = "/batch-jobs/instances/execution-timeout/statistics")
    public BaseResponse queryBatchTaskExecutionTimeoutStatistics(@Valid CurrentDateRequest currentDateRequest) {
        return alarmService.queryBatchTaskExecutionTimeoutStatistics(currentDateRequest);
    }

    @GetMapping(value = "/batch-jobs/instances/fail/statistics")
    public BaseResponse queryBatchTaskFailStatistics(@Valid CurrentDateRequest currentDateRequest) {
        return alarmService.queryBatchTaskFailStatistics(currentDateRequest);
    }

    @GetMapping(value = "/batch-jobs/instances/waiting-timeout/statistics")
    public BaseResponse queryBatchTaskWaitingTimeoutStatistics(@Valid CurrentDateRequest currentDateRequest) {
        return alarmService.queryBatchTaskWaitingTimeoutStatistics(currentDateRequest);
    }

    @GetMapping(value = "/stream-jobs/fail/statistics")
    public BaseResponse queryStreamJobFailStatistics(@Valid CurrentDateRequest currentDateRequest) {
        return alarmService.queryStreamJobFailStatistics(currentDateRequest);
    }

    @GetMapping(value = "/stream-jobs/backpressure/statistics")
    public BaseResponse queryStreamJobBackpressureStatistics(@Valid CurrentDateRequest currentDateRequest) {
        return alarmService.queryStreamJobBackpressureStatistics(currentDateRequest);
    }
}