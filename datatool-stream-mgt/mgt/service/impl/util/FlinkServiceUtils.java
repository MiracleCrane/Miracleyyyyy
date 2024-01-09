/*
 * 文 件 名:  FlinkServiceUtils.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  w00318695
 * 修改时间： 2022/4/7
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.service.impl.util;

import com.huawei.hicampus.campuscommon.common.util.ClearSensitiveDataUtil;
import com.huawei.smartcampus.datatool.entity.StreamJobEntity;
import com.huawei.smartcampus.datatool.enums.JobStatus;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.i18n.I18nUtils;
import com.huawei.smartcampus.datatool.properties.QuantitativeConfig;
import com.huawei.smartcampus.datatool.repository.StreamJobRepository;
import com.huawei.smartcampus.datatool.stream.mgt.constan.FlinkJobConstant;
import com.huawei.smartcampus.datatool.stream.mgt.properties.FlinkConfig;
import com.huawei.smartcampus.datatool.stream.mgt.service.key.ReliablityKey;
import com.huawei.smartcampus.datatool.stream.mgt.utils.HttpClientService;
import com.huawei.smartcampus.datatool.stream.mgt.utils.SqlHandler;
import com.huawei.smartcampus.datatool.stream.mgt.vo.BatchOperateResult;
import com.huawei.smartcampus.datatool.stream.mgt.vo.BatchOperateResultDetail;
import com.huawei.smartcampus.datatool.stream.mgt.vo.JobStatusInfo;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.FlinkRequestRunArgs;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.FlinkRunErrorInfo;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.FlinkRunResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.ListMetricsValue;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.MetricsListResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.MetricsValue;
import com.huawei.smartcampus.datatool.utils.CommonUtil;
import com.huawei.smartcampus.datatool.utils.ParamsCheckUtil;
import com.huawei.smartcampus.datatool.utils.RequestContext;
import com.huawei.smartcampus.datatool.utils.StringUtils;
import com.huawei.smartcampus.datatool.utils.http.DataToolResponseHandler;
import com.huawei.smartcampus.datatool.utils.http.HttpClientPool;
import com.huawei.smartcampus.datatool.vo.flink.StartStreamJobsReq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Flink的部分服务抽象工具
 *
 * @author w00318695
 * @version [SmartCampus V100R001C00, 2022/4/7]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Service
public class FlinkServiceUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlinkServiceUtils.class);

    private static final String FLINK_REST_PATH_JOBS = "/jobs/";

    /**
     * 指标id路径
     */
    private static final String VERTEX_ID_PATH = "$.vertices[*].id";

    /**
     * 算子指标最大数量
     */
    private static final int MAX_METRICS_NUM = 8;

    @Autowired
    private HttpClientService httpClientService;

    @Autowired
    private StreamJobRepository jobRepository;

    @Autowired
    private SqlHandler sqlHandler;

    @Autowired
    private StreamJobRepository streamJobRepository;

    private String remoteUrl = FlinkConfig.flinkRemoteUrl();

    /**
     * 批量提交Flink作业
     *
     * @param jarId Flink Jar Id
     * @param ids 作业id列表
     * @param savepoint savepoint标记
     * @return 批量提交Flink作业的结果
     */
    public FlinkRunResponse batchSubmitFlinkJobs(String jarId, List<String> ids, Short savepoint) {
        int needSlots = 0;
        List<StreamJobEntity> jobs = jobRepository.findStreamJobEntitiesByIdIn(ids);
        for (StreamJobEntity entity : jobs) {
            if (entity.getFlinkId() != null) {
                JobStatus jobStatus = queryJobStatusById(entity.getId()).getStatus();
                if (!jobStatus.judgeRun()) {
                    // 有flinkId并且非运行状态下才统计并行度
                    needSlots += entity.getParallelism();
                }
            } else {
                needSlots += entity.getParallelism();
            }
        }
        int availableSlots = getAvailableSlots();
        if (availableSlots >= needSlots) {
            List<FlinkRunErrorInfo> errorInfos = new ArrayList<>();
            List<String> successNames = new ArrayList<>();
            for (StreamJobEntity job : jobs) {
                submitFlinkJob(jarId, job, savepoint, successNames, errorInfos);
            }
            return new FlinkRunResponse(successNames, errorInfos);
        } else {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CREATE_FLINK_JOB_FAIL,
                    I18nUtils.getMessage(ExceptionCode.DATATOOL_JOBS_SLOTS_EXCEED_LIMIT, availableSlots, needSlots));
        }
    }

    /**
     * 批量提交Flink作业
     *
     * @param jarId Flink Jar Id
     * @param startReq Request
     * @return 批量提交Flink作业的结果
     */
    public BatchOperateResult batchSubmitFlinkJobs(String jarId, StartStreamJobsReq startReq) {
        List<BatchOperateResultDetail> resultDetails = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;
        int needSlots = 0;
        for (String id : startReq.getIds()) {
            Optional<StreamJobEntity> entityOptional = jobRepository.findById(id);
            if (!entityOptional.isPresent()) {
                continue;
            }
            StreamJobEntity entity = entityOptional.get();
            if (entity.getFlinkId() != null) {
                JobStatus jobStatus = queryJobStatusById(entity.getId()).getStatus();
                if (!jobStatus.judgeRun()) {
                    // 有flinkId并且非运行状态下才统计并行度
                    needSlots += entity.getParallelism();
                }
            } else {
                needSlots += entity.getParallelism();
            }
        }
        int availableSlots = getAvailableSlots();
        if (availableSlots >= needSlots) {
            for (String id : startReq.getIds()) {
                Optional<StreamJobEntity> entityOptional = jobRepository.findById(id);
                // 如果id不存在，构造启动成功响应，success+1
                if (!entityOptional.isPresent()) {
                    resultDetails.add(constructFailureResp(id, null, ExceptionCode.DATATOOL_JOB_NOT_EXIST));
                    failureCount++;
                    continue;
                }
                StreamJobEntity entity = entityOptional.get();
                BatchOperateResultDetail resultDetail = submitFlinkJob(jarId, entity, startReq.getFromSavepoint());
                resultDetails.add(resultDetail);
                if (I18nUtils.getMessage(ExceptionCode.DATATOOL_SUCCESS).equals(resultDetail.getStatus())) {
                    successCount += 1;
                } else {
                    failureCount += 1;
                }
            }
            return new BatchOperateResult(resultDetails, failureCount, successCount, failureCount + successCount);
        } else {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CREATE_FLINK_JOB_FAIL,
                    I18nUtils.getMessage(ExceptionCode.DATATOOL_JOBS_SLOTS_EXCEED_LIMIT, availableSlots, needSlots));
        }
    }

    private void submitFlinkJob(String jarId, StreamJobEntity job, Short savepoint, List<String> successNames,
            List<FlinkRunErrorInfo> errorInfos) {
        String oldState = job.getState();
        JobStatus jobStatus = queryJobStatusById(job.getId()).getStatus();
        FlinkRequestRunArgs flinkRequestRunArgs = new FlinkRequestRunArgs();
        try {
            if (!jobStatus.judgeRun()) {
                setFlinkRunRequestArgs(flinkRequestRunArgs, job, Base64.getEncoder(), Base64.getDecoder(), savepoint);
                JSONObject requestJson = JSONObject.parseObject(JSON.toJSONString(flinkRequestRunArgs));
                JSONObject runResObj = httpClientService.doHttp(remoteUrl + "/jars/" + jarId + "/run", requestJson,
                        HttpMethod.POST);
                job.setFlinkId(runResObj.getString("jobid"));
                job.setState(ReliablityKey.SUBMITTED_STATE);
            }
            successNames.add(job.getName());
        } catch (Exception exception) {
            job.setState(oldState);
            LOGGER.error("failed to submit flink job [{}]", job.getName(), exception);
            errorInfos.add(new FlinkRunErrorInfo(job.getName(),
                    I18nUtils.getMessage(ExceptionCode.DATATOOL_FLINK_SERVICE_ERROR)));
        }
        job.setLastModifiedBy(RequestContext.getUserName());
        job.setLastModifiedDate(CommonUtil.getTimestamp());
        streamJobRepository.saveAndFlush(job);
    }

    private BatchOperateResultDetail submitFlinkJob(String jarId, StreamJobEntity job, boolean savepoint) {
        BatchOperateResultDetail detail;
        String oldState = job.getState();
        JobStatus jobStatus = queryJobStatusById(job.getId()).getStatus();
        FlinkRequestRunArgs flinkRequestRunArgs = new FlinkRequestRunArgs();
        JSONObject requestJson = null;
        try {
            if (!jobStatus.judgeRun()) {
                setFlinkRunRequestArgs(flinkRequestRunArgs, job, Base64.getEncoder(), Base64.getDecoder(), savepoint);
                requestJson = JSONObject.parseObject(JSON.toJSONString(flinkRequestRunArgs));
                JSONObject runResObj = httpClientService.doHttp(remoteUrl + "/jars/" + jarId + "/run", requestJson,
                        HttpMethod.POST);
                job.setFlinkId(runResObj.getString("jobid"));
                job.setState(ReliablityKey.SUBMITTED_STATE);
            }
            detail = constructSuccessResp(job.getId(), job.getName());
            // 启动成功后，调用删除保存点接口
            if (job.getSavepointPath() != null) {
                requestFlinkSavepointDisposal(job.getSavepointPath());
            }
            // 恢复保存点后，将之前的保存点删除s
            job.setSavepointPath(null);
            job.setRequestId(null);
        } catch (Exception exception) {
            job.setState(oldState);
            LOGGER.error("failed to submit flink job [{}]", job.getName(), exception);
            detail = constructFailureResp(job.getId(), job.getName(), ExceptionCode.DATATOOL_FLINK_SERVICE_ERROR);
        } finally {
            // requestJson中含有密码，需要使用完后及时清理
            ClearSensitiveDataUtil.clearPlainSensitiveData(requestJson);
            // programArgs里有敏感信息
            ClearSensitiveDataUtil.clearPlainSensitiveData(flinkRequestRunArgs.getProgramArgs());
        }
        job.setLastModifiedBy(RequestContext.getUserName());
        job.setLastModifiedDate(CommonUtil.getTimestamp());
        streamJobRepository.saveAndFlush(job);
        return detail;
    }

    /**
     * 启动完成后，调用删除savepoint接口
     *
     * @param savepointPath 要删除的savepoint路径
     */
    private void requestFlinkSavepointDisposal(String savepointPath) {
        JSONObject requestJson = new JSONObject();
        requestJson.put("savepoint-path", savepointPath);
        try {
            httpClientService.doHttp(remoteUrl + "/savepoint-disposal", requestJson, HttpMethod.POST);
        } catch (Exception e) {
            LOGGER.error("failed to request savepoint-disposal", e);
        }
    }

    /**
     * 构造批量操作成功响应，包含删除、启动、停止
     *
     * @param id 作业id
     * @param name 作业name
     * @return BatchOperateResultDetail
     */
    public BatchOperateResultDetail constructSuccessResp(String id, String name) {
        return new BatchOperateResultDetail(id, name, I18nUtils.getMessage(ExceptionCode.DATATOOL_SUCCESS));
    }

    /**
     * 构造批量失败响应
     *
     * @param id 作业id
     * @param name 作业name
     * @param code 国际码
     * @return BatchOperateResultDetail
     */
    public BatchOperateResultDetail constructFailureResp(String id, String name, String code) {
        return new BatchOperateResultDetail(id, name, I18nUtils.getMessage(ExceptionCode.DATATOOL_FAILURE),
                I18nUtils.getMessage(code));
    }

    private void setFlinkRunRequestArgs(FlinkRequestRunArgs flinkRequestRunArgs, StreamJobEntity job,
            Base64.Encoder encoder, Base64.Decoder decoder, Short savepoint) {
        flinkRequestRunArgs.setEntryClass("com.huawei.dataservice.sql.helpler.SqlExecutor");
        flinkRequestRunArgs.setProgramArgs("-sql " + sqlHandler.sqlAfterReplaceVar(job.getFlinkSql(), encoder, decoder)
                + " -retry-times " + job.getRetryTimes() + " -jobname " + job.getName() + " -enable-chk "
                + job.isEnableChk() + " -chk-mode " + job.getChkMode() + " -chk-interval " + job.getChkInterval()
                + " -chk-min-pause " + job.getChkMinPause() + " -chk-timeout " + job.getChkTimeout());
        flinkRequestRunArgs.setParallelism(job.getParallelism());
        flinkRequestRunArgs.setAllowNonRestoredState(false);
        String savepointPath = job.getSavepointPath();
        if (savepoint == 1 && savepointPath != null) {
            flinkRequestRunArgs.setSavepointPath(savepointPath);
        }
        // 恢复保存点后，将之前的保存点删除s
        job.setSavepointPath(null);
        job.setRequestId(null);
    }

    private void setFlinkRunRequestArgs(FlinkRequestRunArgs flinkRequestRunArgs, StreamJobEntity job,
            Base64.Encoder encoder, Base64.Decoder decoder, boolean savepoint) {
        flinkRequestRunArgs.setEntryClass("com.huawei.dataservice.sql.helpler.SqlExecutor");
        flinkRequestRunArgs.setProgramArgs("-sql " + sqlHandler.sqlAfterReplaceVar(job.getFlinkSql(), encoder, decoder)
                + " -retry-times " + job.getRetryTimes() + " -jobname " + job.getName() + " -enable-chk "
                + job.isEnableChk() + " -chk-mode " + job.getChkMode() + " -chk-interval " + job.getChkInterval()
                + " -chk-min-pause " + job.getChkMinPause() + " -chk-timeout " + job.getChkTimeout());
        flinkRequestRunArgs.setParallelism(job.getParallelism());
        flinkRequestRunArgs.setAllowNonRestoredState(false);
        String savepointPath = job.getSavepointPath();
        if (savepoint && savepointPath != null) {
            flinkRequestRunArgs.setSavepointPath(savepointPath);
        }
    }

    /**
     * 获取可用的slot数量
     *
     * @return 可用的slot数量
     */
    private int getAvailableSlots() {
        JSONObject jsonObject;
        try {
            jsonObject = httpClientService.doHttp(remoteUrl + "/overview", null, HttpMethod.GET);
        } catch (Exception e) {
            LOGGER.error("Get flink available slot failed!", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECT_TO_FLINK_FAIL);
        }
        return jsonObject.getIntValue("slots-available");
    }

    /**
     * 根据jobid获取job的状态
     *
     * @param jobId 作业ID
     * @return 作业是否停止
     */
    public boolean jobStopped(String jobId) {
        JobStatusInfo jobStatusInfo = queryJobStatusById(jobId);
        JobStatus jobStatus = jobStatusInfo.getStatus();
        return jobStatus.checkStopped() || jobStatus.checkException();
    }

    /**
     * 通过jobId获取flinkId
     *
     * @param jobId jobId
     * @return flinkId
     */
    public String getFlinkIdByJobId(String jobId) {
        String flinkId = null;
        Optional<StreamJobEntity> streamJobOption = jobRepository.findById(jobId);
        if (streamJobOption.isPresent()) {
            flinkId = streamJobOption.get().getFlinkId();
        }
        return flinkId;
    }

    /**
     * 根据flinkId查询单个flink作业状态
     *
     * @param flinkId flinkId
     * @return 作业状态
     */
    public Optional<JobStatus> getJobStatusByFlinkId(String flinkId) {
        if (StringUtils.isEmpty(flinkId)) {
            return Optional.empty();
        }
        JSONObject jobsStatusResp;
        try {
            HttpGet httpGet = new HttpGet(remoteUrl + FLINK_REST_PATH_JOBS + "overview");
            // 当前作业数量不多，调用overview接口查询全量作业信息
            String response = HttpClientPool.getHttpClient().execute(httpGet, new DataToolResponseHandler());
            jobsStatusResp = JSONObject.parseObject(response);
        } catch (Exception exception) {
            LOGGER.error("querying jobs status encountered exception", exception);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_GET_JOB_STATUS_ERROR, exception.getMessage());
        }
        JSONArray jobInfos = jobsStatusResp.getJSONArray("jobs");
        for (int i = 0; i < jobInfos.size(); i++) {
            JSONObject jsonObject = jobInfos.getJSONObject(i);
            // 遍历结果，当jid与入参一致时，返回对应job的状态
            if (flinkId.equals(jsonObject.getString("jid"))) {
                return Optional.of(Enum.valueOf(JobStatus.class, jsonObject.getString("state")));
            }
        }
        return Optional.empty();
    }

    /**
     * 查询作业状态
     *
     * @param id 作业id
     * @return JobStatusInfo
     */
    public JobStatusInfo queryJobStatusById(String id) {
        // 根据id获取flink_id
        Optional<StreamJobEntity> streamJob = jobRepository.findById(id);
        // 作业不存在时抛出异常
        String flinkId = streamJob.orElseThrow(() -> new DataToolRuntimeException(ExceptionCode.DATATOOL_JOB_NOT_EXIST))
                .getFlinkId();
        JobStatus status;
        Optional<JobStatus> flinkJobStatus = getJobStatusByFlinkId(flinkId);
        if (flinkJobStatus.isPresent()) {
            status = flinkJobStatus.get();
        } else if (ReliablityKey.SUBMITTED_STATE.equals(streamJob.get().getState())) {
            status = JobStatus.EXCEPTION_STOPPED;
        } else {
            status = JobStatus.STOPPED;
        }
        return new JobStatusInfo(id, status);
    }

    /**
     * 校验指标个数
     * 校验指标是否为空，数量是否超过最大限制
     *
     * @param metrics 算子指标
     */
    public void validateMetricsNum(String metrics) {
        if (StringUtils.isEmpty(metrics)) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_FLINK_JOB_VERTEX_METRICS_CANNOT_EMPTY);
        }

        int metricsNum = metrics.split(FlinkJobConstant.VERTEX_METRIC_SEPARATOR).length;
        if (metricsNum > MAX_METRICS_NUM) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_FLINK_JOB_VERTEX_METRICS_NUM_EXCEED_LIMIT,
                    metricsNum);
        }
    }

    /**
     * 获取算子指标列表或者获取算子指标值
     * 根据入参 metrics 的值进行判断，null表示获取列表，反之表示获取参数值
     *
     * @param jobId 作业id
     * @param vertexId 算子id
     * @param metrics 算子指标
     * @return MetricsListResponse
     */
    public MetricsListResponse getMetricsListOrValue(String jobId, String vertexId, String metrics) {
        if (jobStopped(jobId)) {
            return null;
        }

        if (!judgeVertexIdExist(jobId, vertexId)) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_FLINK_JOB_VERTEX_ID_NOT_EXIST);
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ParamsCheckUtil.checkFlinkRequestUrl(
                new StringBuilder().append(remoteUrl).append(FLINK_REST_PATH_JOBS).append(getFlinkIdByJobId(jobId))
                        .append("/vertices/").append(vertexId).append("/metrics").toString()));
        if (StringUtils.isNotEmpty(metrics)) {
            builder.queryParam(FlinkJobConstant.VERTEX_METRIC_REQUEST_KEY, metrics);
        }
        List<MetricsValue> metricsValueList;
        try {
            HttpGet httpGet = new HttpGet(builder.toUriString());
            String response = HttpClientPool.getHttpClient().execute(httpGet, new DataToolResponseHandler());
            metricsValueList = JSONObject.parseObject(response, ListMetricsValue.class);
        } catch (Exception e) {
            LOGGER.error("Failed to obtain the set or the value of metrics.", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECT_TO_FLINK_FAIL);
        }
        return new MetricsListResponse(metricsValueList.stream()
                .sorted(Comparator.comparingInt(o -> o.getId().length())).collect(Collectors.toList()));
    }

    /**
     * 判断算子id是否存在
     *
     * @param jobId 作业id
     * @param vertexId 算子id
     * @return 判断结果
     */
    public boolean judgeVertexIdExist(String jobId, String vertexId) {
        if (StringUtils.isEmpty(vertexId)) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_FLINK_JOB_VERTEX_ID_CANNOT_EMPTY);
        }
        String jobInfo;
        try {
            HttpGet httpGet = new HttpGet(remoteUrl + FLINK_REST_PATH_JOBS + getFlinkIdByJobId(jobId));
            jobInfo = HttpClientPool.getHttpClient().execute(httpGet, new DataToolResponseHandler());
        } catch (Exception e) {
            LOGGER.error("Failed to query job detail, jobId: {}", jobId, e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECT_TO_FLINK_FAIL);
        }
        JSONArray vertices = JSONObject.parseArray(JSONPath.read(jobInfo, VERTEX_ID_PATH).toString());
        for (Object vertex : vertices) {
            if (vertexId.equals(vertex.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验流作业数量是否超过100个
     */
    public void checkStreamJobNum() {
        if (jobRepository.findAll().size() >= QuantitativeConfig.getMaxStreamJobs()) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_STREAM_JOBS_NUM_EXCEED_LIMIT,
                    QuantitativeConfig.getMaxStreamJobs());
        }
    }

    /**
     * 校验数据库中是否存在同名作业
     *
     * @param name 作业名称
     */
    public void checkJobNameDuplicate(String name) {
        if (jobRepository.findByName(name) != null) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_JOB_NAME_ALREADY_EXIST);
        }
    }

    /**
     * 判断作业名是否已被别的作业使用，存在同名不同 id则表示已被别的作业使用
     *
     * @param id 作业 id
     * @param name 作业 name
     */
    public void checkJobNameDuplicate(String id, String name) {
        StreamJobEntity job = jobRepository.findByName(name);
        if (job != null && !id.equals(job.getId())) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_JOB_NAME_ALREADY_EXIST);
        }
    }
}
