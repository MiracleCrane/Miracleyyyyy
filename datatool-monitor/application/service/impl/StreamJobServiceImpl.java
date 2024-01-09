/*
 * 文 件 名:  StreamJobServiceImpl.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/17
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.impl;

import com.huawei.smartcampus.datatool.enums.JobStatus;
import com.huawei.smartcampus.datatool.model.BaseResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.StreamJobService;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.streamjob.StreamJobInfo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.streamjob.StreamJobStatusCountQueryResponse;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.StreamJobGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流作业服务
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Service
public class StreamJobServiceImpl implements StreamJobService {
    @Autowired
    private StreamJobGateway streamJobGateway;

    @Override
    public BaseResponse queryStreamJobStateCount() {
        List<StreamJobInfo> result = streamJobGateway.queryStreamJobInfo(null);
        StreamJobStatusCountQueryResponse streamJobStatusCountQueryResponse = new StreamJobStatusCountQueryResponse(0,
                0, 0, 0, 0);
        for (StreamJobInfo streamJobInfo : result) {
            String status = streamJobInfo.getStatus();
            switch (JobStatus.valueOf(status)) {
                case STOPPED:
                    streamJobStatusCountQueryResponse.setStopped(streamJobStatusCountQueryResponse.getStopped() + 1);
                    break;
                case FAILED:
                    streamJobStatusCountQueryResponse.setFailed(streamJobStatusCountQueryResponse.getFailed() + 1);
                    break;
                case RUNNING:
                    streamJobStatusCountQueryResponse.setRunning(streamJobStatusCountQueryResponse.getRunning() + 1);
                    break;
                case EXCEPTION_STOPPED:
                    streamJobStatusCountQueryResponse
                            .setExceptionStopped(streamJobStatusCountQueryResponse.getExceptionStopped() + 1);
                    break;
                default:
                    streamJobStatusCountQueryResponse.setOthers(streamJobStatusCountQueryResponse.getOthers() + 1);
            }
        }
        return BaseResponse.newOk(streamJobStatusCountQueryResponse);
    }
}