/*
 * 文 件 名:  AllStreamJobStrategy.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/17
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.strategy;

import com.huawei.smartcampus.datatool.enums.JobStatus;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.streamjob.StreamJobInfo;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.StreamJobCustomRepository;
import com.huawei.smartcampus.datatool.repository.StreamJobRepository;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import java.util.List;

/**
 * 全量流处理作业策略
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class AllStreamJobStrategy implements AllStatisticsStrategy {
    private final StreamJobRepository streamJobRepository = SpringContextHelper.getBean(StreamJobRepository.class);
    private final StreamJobCustomRepository streamJobCustomRepository = SpringContextHelper
            .getBean(StreamJobCustomRepository.class);

    @Override
    public int countNum() {
        return (int) streamJobRepository.count();
    }

    @Override
    public int countStateNum(boolean state) {
        List<StreamJobInfo> allJobs = streamJobCustomRepository.queryStreamJobInfo(null);
        int count = 0;
        for (StreamJobInfo streamJobInfo : allJobs) {
            if (JobStatus.valueOf(streamJobInfo.getStatus()).judgeRun() == state) {
                count = count + 1;
            }
        }
        return count;
    }
}