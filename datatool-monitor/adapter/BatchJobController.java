/*
 * 文 件 名:  BatchController.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/17
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.adapter;

import com.huawei.smartcampus.datatool.model.BaseResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.BatchJobService;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.CurrentDateRequest;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.batchjob.BatchRunningJobInstanceCountRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 批处理外部接口
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@RestController
@RequestMapping(value = "/v1/batch-jobs")
public class BatchJobController {
    @Autowired
    private BatchJobService batchJobService;

    @GetMapping(value = "/instances/today/state/count")
    public BaseResponse queryTodayJobInstanceStateCount(@Valid CurrentDateRequest currentDateRequest) {
        return batchJobService.queryTodayJobInstanceStateCount(currentDateRequest);
    }

    @GetMapping(value = "/instances/day/running/count")
    public BaseResponse queryDayRunningJobInstanceCount(
            @Valid BatchRunningJobInstanceCountRequest batchRunningJobInstanceCountRequest) {
        return batchJobService.queryDayRunningJobInstanceCount(batchRunningJobInstanceCountRequest);
    }
}