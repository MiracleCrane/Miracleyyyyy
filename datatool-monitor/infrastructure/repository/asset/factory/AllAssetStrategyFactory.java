/*
 * 文 件 名:  AllAssetStrategyFactory.java
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
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.strategy.AllBatchJobStrategy;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.strategy.AllBatchScriptStrategy;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.strategy.AllStatisticsStrategy;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.strategy.AllStreamJobStrategy;

/**
 * 全量资产策略工厂
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class AllAssetStrategyFactory {
    public static AllStatisticsStrategy createStrategy(JobStatisticsType jobStatisticsType) {
        switch (jobStatisticsType) {
            case BATCH_JOB:
                return new AllBatchJobStrategy();
            case STREAM_JOB:
                return new AllStreamJobStrategy();
            case BATCH_SCRIPT:
                return new AllBatchScriptStrategy();
            default:
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_JOB_TYPE_NOT_SUPPORT,
                        jobStatisticsType.jobStatisticsType());
        }
    }
}