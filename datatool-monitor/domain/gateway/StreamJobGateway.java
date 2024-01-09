/*
 * 文 件 名:  StreamJobGateway.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.gateway;

import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job.StreamJobDetail;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.streamjob.StreamJobInfo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.streamjob.StreamJobTaskInfo;

import java.util.List;

/**
 * 流处理数据抽象接口
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public interface StreamJobGateway {
    /**
     * 查询实时作业信息
     *
     * @param status 作业状态
     * @return 实时作业信息列表
     */
    List<StreamJobInfo> queryStreamJobInfo(String status);

    /**
     * 通过作业id查询实时作业任务信息
     *
     * @param jobId 作业id
     * @return 实时作业任务信息列表
     */
    List<StreamJobTaskInfo> queryStreamTaskInfoByJobId(String jobId);

    /**
     * 获取流处理作业明细
     *
     * @return 流处理作业明细
     */
    List<StreamJobDetail> getStreamJobDetail();
}