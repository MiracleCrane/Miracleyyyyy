/*
 * 文 件 名:  CustomAssetStrategyFactory.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/17
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.factory;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.domain.overview.JobStatisticsType;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.strategy.CustomBatchJobStrategy;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.strategy.CustomBatchScriptStrategy;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.strategy.CustomStatisticsStrategy;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.strategy.CustomStreamJobStrategy;

/**
 * 定制资产策略工厂
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class CustomAssetStrategyFactory {
    public static CustomStatisticsStrategy createStrategy(JobStatisticsType jobStatisticsType) {
        switch (jobStatisticsType) {
            case BATCH_JOB:
                return new CustomBatchJobStrategy();
            case STREAM_JOB:
                return new CustomStreamJobStrategy();
            case BATCH_SCRIPT:
                return new CustomBatchScriptStrategy();
            default:
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_JOB_TYPE_NOT_SUPPORT,
                        jobStatisticsType.jobStatisticsType());
        }
    }
}