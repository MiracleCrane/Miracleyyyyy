/*
 * 文 件 名:  AssetOVGateWay.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.gateway;

import com.huawei.smartcampus.datatool.monitor.domain.overview.GroupStatisticsEnum;
import com.huawei.smartcampus.datatool.monitor.domain.overview.JobStatisticsType;

/**
 * 资产概览服务
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/19]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public interface AssetOVGateWay {
    int getMaxJobNum(JobStatisticsType jobType);

    int getJobNum(JobStatisticsType jobType, GroupStatisticsEnum group);

    int getJobStateNum(JobStatisticsType jobType, GroupStatisticsEnum group, boolean state);
}
