/*
 * 文 件 名:  BatchJobService.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/17
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service;

import com.huawei.smartcampus.datatool.model.BaseResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.CurrentDateRequest;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.batchjob.BatchRunningJobInstanceCountRequest;

/**
 * 批处理作业服务
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public interface BatchJobService {
    /**
     * 查询今日作业实例状态数量
     *
     * @param currentDateRequest 当前时间请求
     * @return 查询结果
     */
    BaseResponse queryTodayJobInstanceStateCount(CurrentDateRequest currentDateRequest);

    /**
     * 查询近24小时作业实例运行数量
     *
     * @param batchRunningJobInstanceCountRequest 查询请求
     * @return 查询结果
     */
    BaseResponse queryDayRunningJobInstanceCount(
            BatchRunningJobInstanceCountRequest batchRunningJobInstanceCountRequest);
}