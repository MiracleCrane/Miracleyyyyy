/*
 * 文 件 名:  StreamJobRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository;

import com.huawei.smartcampus.datatool.entity.StreamJobEntity;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job.StreamJobDetail;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.streamjob.StreamJobInfo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.streamjob.StreamJobTaskInfo;
import com.huawei.smartcampus.datatool.monitor.common.properties.JobApplicationConfig;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.StreamJobGateway;
import com.huawei.smartcampus.datatool.monitor.domain.overview.GroupStatisticsEnum;
import com.huawei.smartcampus.datatool.monitor.domain.overview.JobStatisticsType;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.factory.CustomAssetStrategyFactory;
import com.huawei.smartcampus.datatool.properties.QuantitativeConfig;
import com.huawei.smartcampus.datatool.repository.StreamJobRepository;
import com.huawei.smartcampus.datatool.utils.StringUtils;
import com.huawei.smartcampus.datatool.utils.http.DataToolResponseHandler;
import com.huawei.smartcampus.datatool.utils.http.HttpClientPool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 流处理数据仓库
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Component
public class StreamJobCustomRepository implements StreamJobGateway {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamJobCustomRepository.class);

    @Autowired
    StreamJobRepository streamJobRepository;

    @Override
    public List<StreamJobInfo> queryStreamJobInfo(String status) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageIndex", 1);
        // 直接取最大数量查出全部作业
        jsonObject.put("pageSize", QuantitativeConfig.getMaxStreamJobs());
        if (!StringUtils.isEmpty(status)) {
            jsonObject.put("status", status);
        }
        try {
            URIBuilder uriBuilder = new URIBuilder(JobApplicationConfig.streamJobQueryUrl());
            Map<String, Object> paramsMap = jsonObject.getInnerMap();
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            String resultStr = HttpClientPool.getHttpClient().execute(httpGet, new DataToolResponseHandler());
            JSONObject resultObj = JSON.parseObject(resultStr).getJSONObject("result");
            if (resultObj == null) {
                return Collections.emptyList();
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("query running stream job result: {}", resultObj);
            }
            JSONArray jobs = resultObj.getJSONArray("jobs");
            List<StreamJobInfo> streamJobInfos = new ArrayList<>();
            for (int i = 0; i < jobs.size(); i++) {
                JSONObject job = jobs.getJSONObject(i);
                streamJobInfos
                        .add(new StreamJobInfo(job.getString("id"), job.getString("name"), job.getString("status")));
            }
            return streamJobInfos;
        } catch (HttpResponseException e) {
            // 可能存在敏感信息，不直接打印异常
            LOGGER.error("query stream job response failed!");
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_QUERY_STREAM_JOB_FAIL);
        } catch (Exception e) {
            LOGGER.error("query stream job failed!", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_QUERY_STREAM_JOB_FAIL);
        }
    }

    @Override
    public List<StreamJobTaskInfo> queryStreamTaskInfoByJobId(String jobId) {
        HttpGet httpGet = new HttpGet(JobApplicationConfig.streamJobDetailQueryUrl().replace("{jobId}", jobId));
        try {
            String resultStr = HttpClientPool.getHttpClient().execute(httpGet, new DataToolResponseHandler());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("query stream job detail success: {}", JSON.parseObject(resultStr));
            }
            JSONObject detailResultList = JSON.parseObject(resultStr).getJSONObject("result");
            if (detailResultList == null) {
                return Collections.emptyList();
            }
            JSONArray executionTaskList = detailResultList.getJSONArray("tasks");
            List<StreamJobTaskInfo> streamJobTaskInfos = new ArrayList<>();
            for (int i = 0; i < executionTaskList.size(); i++) {
                JSONObject task = executionTaskList.getJSONObject(i);
                streamJobTaskInfos.add(new StreamJobTaskInfo(task.getString("taskId"), task.getString("name"),
                        task.getString("backpressure")));
            }
            return streamJobTaskInfos;
        } catch (HttpResponseException e) {
            // 可能存在敏感信息，不直接打印异常
            LOGGER.error("query stream job detail response failed!");
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_QUERY_STREAM_JOB_DETAIL_FAIL);
        } catch (Exception e) {
            LOGGER.error("query stream job detail failed!", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_QUERY_STREAM_JOB_DETAIL_FAIL);
        }
    }

    @Override
    public List<StreamJobDetail> getStreamJobDetail() {
        List<StreamJobDetail> streamJobDetails = new ArrayList<>();
        List<StreamJobEntity> streamJobEntities = streamJobRepository.findAll();
        for (StreamJobEntity streamJobEntity : streamJobEntities) {
            StreamJobDetail streamJobDetail = new StreamJobDetail();
            String jobName = streamJobEntity.getName();
            String[] jobNameArray = jobName.split("_");
            // 根据流处理命名规范，获取作业的领域
            if (jobNameArray.length > 1) {
                streamJobDetail.setDomain(jobNameArray[1]);
            }
            // 获取作业名
            streamJobDetail.setName(jobName);
            // 获取来源
            streamJobDetail.setOrigin(CustomAssetStrategyFactory.createStrategy(JobStatisticsType.STREAM_JOB).isCustom(streamJobEntity.getId())
                    ? GroupStatisticsEnum.CUSTOM.value()
                    : GroupStatisticsEnum.BASELINE.value());
            streamJobDetails.add(streamJobDetail);
        }
        return streamJobDetails;
    }
}