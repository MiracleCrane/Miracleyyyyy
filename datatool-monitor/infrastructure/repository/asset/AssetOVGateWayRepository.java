/*
 * 文 件 名:  AssetOVGateWayRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/10/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.common.properties.JobApplicationConfig;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.AssetOVGateWay;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.AssetStatisticsStrategy;
import com.huawei.smartcampus.datatool.monitor.domain.overview.GroupStatisticsEnum;
import com.huawei.smartcampus.datatool.monitor.domain.overview.JobStatisticsType;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.factory.AllAssetStrategyFactory;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.factory.BaselineAssetStrategyFactory;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.factory.CustomAssetStrategyFactory;
import com.huawei.smartcampus.datatool.utils.CommonUtil;
import com.huawei.smartcampus.datatool.utils.http.DataToolResponseHandler;
import com.huawei.smartcampus.datatool.utils.http.HttpClientPool;

import com.alibaba.fastjson.JSONObject;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 资产概览数仓
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/23]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Component
public class AssetOVGateWayRepository implements AssetOVGateWay {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetOVGateWayRepository.class);

    @Override
    public int getMaxJobNum(JobStatisticsType jobType) {
        String url;
        String resName;
        switch (jobType) {
            case BATCH_JOB:
                url = JobApplicationConfig.maxBatchJobsQueryUrl();
                resName = "maxJobs";
                break;
            case STREAM_JOB:
                url = JobApplicationConfig.maxStreamJobsQueryUrl();
                resName = "maxJobs";
                break;
            case BATCH_SCRIPT:
                url = JobApplicationConfig.maxBatchScriptsQueryUrl();
                resName = "maxScripts";
                break;
            default:
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_JOB_TYPE_NOT_SUPPORT,
                        jobType.jobStatisticsType());
        }
        JSONObject resp;
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("query max number of {} by url: {}.", jobType, url);
            }
            HttpGet httpGet = new HttpGet(CommonUtil.encodeForURL(url));
            String responseStr = HttpClientPool.getHttpClient().execute(httpGet, new DataToolResponseHandler());
            resp = JSONObject.parseObject(responseStr);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("query max number of {} response: {}.", jobType, resp);
            }
            return resp.getJSONObject("result").getIntValue(resName);
        } catch (Exception e) {
            LOGGER.error("Failed to query max number of {}", jobType, e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_QUERY_MAX_JOB_NUMBER_FAIL);
        }
    }

    @Override
    public int getJobNum(JobStatisticsType jobType, GroupStatisticsEnum group) {
        return getAssetStatisticsStrategy(jobType, group).countNum();
    }

    @Override
    public int getJobStateNum(JobStatisticsType jobType, GroupStatisticsEnum group, boolean state) {
        return getAssetStatisticsStrategy(jobType, group).countStateNum(state);
    }

    private AssetStatisticsStrategy getAssetStatisticsStrategy(JobStatisticsType jobType, GroupStatisticsEnum group) {
        switch (group) {
            case ALL:
                return AllAssetStrategyFactory.createStrategy(jobType);
            case BASELINE:
                return BaselineAssetStrategyFactory.createStrategy(jobType);
            case CUSTOM:
                return CustomAssetStrategyFactory.createStrategy(jobType);
            default:
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ASSET_GROUP_TYPE_NOT_SUPPORT, group.value());
        }
    }
}