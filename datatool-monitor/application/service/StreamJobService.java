/*
 * 文 件 名:  StreamJobService.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/17
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service;

import com.huawei.smartcampus.datatool.model.BaseResponse;

/**
 * 流处理作业服务
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public interface StreamJobService {
    /**
     * 查询实时作业状态数量分布
     *
     * @return 查询结果
     */
    BaseResponse queryStreamJobStateCount();
}