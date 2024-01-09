/*
 * 文 件 名:  FlinkServiceImpl.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  服务类
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.service.impl;

import com.huawei.smartcampus.datatool.constant.Constant;
import com.huawei.smartcampus.datatool.entity.StreamJobEntity;
import com.huawei.smartcampus.datatool.enums.JobStatus;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.HttpRequestException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.i18n.I18nUtils;
import com.huawei.smartcampus.datatool.model.BaseResponse;
import com.huawei.smartcampus.datatool.repository.StreamJobRepository;
import com.huawei.smartcampus.datatool.stream.mgt.constan.FlinkJobConstant;
import com.huawei.smartcampus.datatool.stream.mgt.enums.FlinkOperationName;
import com.huawei.smartcampus.datatool.stream.mgt.properties.FlinkConfig;
import com.huawei.smartcampus.datatool.stream.mgt.service.FlinkService;
import com.huawei.smartcampus.datatool.stream.mgt.service.impl.util.FlinkServiceUtils;
import com.huawei.smartcampus.datatool.stream.mgt.service.key.ReliablityKey;
import com.huawei.smartcampus.datatool.stream.mgt.utils.HttpClientService;
import com.huawei.smartcampus.datatool.stream.mgt.vo.BatchOperateResult;
import com.huawei.smartcampus.datatool.stream.mgt.vo.BatchOperateResultDetail;
import com.huawei.smartcampus.datatool.stream.mgt.vo.JobStatusInfo;
import com.huawei.smartcampus.datatool.stream.mgt.vo.LogResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.LogsResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.BackpressureResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.FlinkBasicInfo;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.FlinkRunErrorInfo;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.FlinkRunResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.FlinkTask;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.FlinkTasksResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.JobExceptionsResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.MetricsListResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.PlanResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.WatermarksResponse;
import com.huawei.smartcampus.datatool.utils.CommonUtil;
import com.huawei.smartcampus.datatool.utils.NormalizerUtil;
import com.huawei.smartcampus.datatool.utils.RequestContext;
import com.huawei.smartcampus.datatool.utils.StringUtils;
import com.huawei.smartcampus.datatool.utils.http.DataToolResponseHandler;
import com.huawei.smartcampus.datatool.utils.http.HttpClientPool;
import com.huawei.smartcampus.datatool.utils.http.InputStreamResponseHandler;
import com.huawei.smartcampus.datatool.vo.flink.StartStreamJobsReq;
import com.huawei.smartcampus.datatool.vo.flink.StopStreamJobsReq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

/**
 * 定义实现类 flink作业的相关操作：批量处理，获取作业状态、任务列表、日志等操作
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Service
@Transactional
public class FlinkServiceImpl implements FlinkService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlinkServiceImpl.class);

    private static final String FLINK_REST_PATH_JOBS = "/jobs/";

    private static final String END_TIME = "end-time";

    private static final String SAVEPOINT_STATUS = "status";

    private static final String COMPLETED_STATUS = "COMPLETED";

    private static final String IN_PROGRESS_STATUS = "IN_PROGRESS";

    private static final String LOG_FILE_NAME_PREFIX = "flink";

    @Autowired
    private HttpClientService httpClientService;

    @Autowired
    private StreamJobRepository jobRepository;

    @Autowired
    private StreamJobRepository streamJobRepository;

    @Autowired
    private FlinkServiceUtils flinkServiceUtils;

    private SynchronousQueue<Runnable> threadQueue = new SynchronousQueue<>();

    private ExecutorService executor = new ThreadPoolExecutor(10, 64, 60L, TimeUnit.SECONDS, threadQueue);

    private String remoteUrl = FlinkConfig.flinkRemoteUrl();

    @Override
    public FlinkRunResponse batchOperation(List<String> ids, FlinkOperationName operationName, Short savepoint) {
        String jarId = null;
        try {
            jarId = httpClientService.getRemoteSqlJarId(remoteUrl);
        } catch (IOException | URISyntaxException exception) {
            LOGGER.error("batch operation encountered exception: ", exception);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_GET_JAR_ID_FAIL, exception.getMessage());
        }
        if (operationName == FlinkOperationName.RUN) {
            // 在提交作业时加锁，防止多个作业并发启动时查询的flink可用slot数量不准确
            synchronized (FlinkServiceImpl.class) {
                return flinkServiceUtils.batchSubmitFlinkJobs(jarId, ids, savepoint);
            }
        }

        List<FlinkRunErrorInfo> errorInfos = new ArrayList<>();
        List<String> successNames = new ArrayList<>();
        if (operationName == FlinkOperationName.CANCEL) {
            batchStopFlinkJobs(ids, savepoint, successNames, errorInfos);
        }
        return new FlinkRunResponse(successNames, errorInfos);
    }

    @Override
    public BatchOperateResult batchStartOperation(StartStreamJobsReq startStreamJobsReq) {
        String jarId;
        try {
            jarId = httpClientService.getRemoteSqlJarId(remoteUrl);
        } catch (IOException | URISyntaxException exception) {
            LOGGER.error("batch start job encountered exception: ", exception);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_GET_JAR_ID_FAIL, exception.getMessage());
        }

        synchronized (FlinkServiceImpl.class) {
            return flinkServiceUtils.batchSubmitFlinkJobs(jarId, startStreamJobsReq);
        }
    }

    @Override
    public BatchOperateResult batchStopOperation(StopStreamJobsReq stopStreamJobsReq) {
        return batchStopFlinkJobs(stopStreamJobsReq);
    }

    private void batchStopFlinkJobs(List<String> ids, Short savepoint, List<String> successNames,
            List<FlinkRunErrorInfo> errorInfos) {
        List<StreamJobEntity> jobs = jobRepository.findStreamJobEntitiesByIdIn(ids);
        for (StreamJobEntity job : jobs) {
            String oldState = job.getState();
            JobStatus jobStatus = flinkServiceUtils.queryJobStatusById(job.getId()).getStatus();
            try {
                if (jobStatus.judgeRun()) {
                    batchStopOperation(job, (savepoint != 0));
                }
                successNames.add(job.getName());
            } catch (Exception exception) {
                job.setState(oldState);
                LOGGER.error("failed to stop flink job [{}]", job.getName(), exception);
                errorInfos.add(new FlinkRunErrorInfo(job.getName(),
                        I18nUtils.getMessage(ExceptionCode.DATATOOL_FLINK_SERVICE_ERROR)));
            }
            job.setLastModifiedBy(RequestContext.getUserName());
            job.setLastModifiedDate(CommonUtil.getTimestamp());
            streamJobRepository.save(job);
        }
    }

    private BatchOperateResult batchStopFlinkJobs(StopStreamJobsReq jobsReq) {
        List<BatchOperateResultDetail> resultDetails = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;
        for (String id : jobsReq.getIds()) {
            Optional<StreamJobEntity> entityOptional = jobRepository.findById(id);
            if (!entityOptional.isPresent()) {
                resultDetails
                        .add(flinkServiceUtils.constructFailureResp(id, null, ExceptionCode.DATATOOL_JOB_NOT_EXIST));
                failureCount++;
                continue;
            }
            StreamJobEntity job = entityOptional.get();
            BatchOperateResultDetail resultDetail = new BatchOperateResultDetail();
            resultDetail.setId(job.getId());
            resultDetail.setName(job.getName());
            String oldState = job.getState();
            JobStatus jobStatus = flinkServiceUtils.queryJobStatusById(job.getId()).getStatus();
            try {
                if (jobStatus.judgeRun()) {
                    batchStopOperation(job, jobsReq.getSavepoint());
                }
                resultDetail.setStatus(I18nUtils.getMessage(ExceptionCode.DATATOOL_SUCCESS));
                successCount += 1;
            } catch (Exception exception) {
                job.setState(oldState);
                LOGGER.error("failed to stop flink job [{}]", job.getName(), exception);
                resultDetail.setStatus(I18nUtils.getMessage(ExceptionCode.DATATOOL_FAILURE));
                resultDetail.setMessage(I18nUtils.getMessage(ExceptionCode.DATATOOL_FLINK_SERVICE_ERROR));
                failureCount += 1;
            }
            job.setLastModifiedBy(RequestContext.getUserName());
            job.setLastModifiedDate(CommonUtil.getTimestamp());
            streamJobRepository.save(job);
            resultDetails.add(resultDetail);
        }
        return new BatchOperateResult(resultDetails, failureCount, successCount, failureCount + successCount);
    }

    private void batchStopOperation(StreamJobEntity job, boolean savepoint) throws IOException, URISyntaxException {
        job.setState(ReliablityKey.UNSUBMITTED_STATE);
        if (!savepoint) {
            httpClientService.doHttp(new StringBuilder().append(remoteUrl).append(FLINK_REST_PATH_JOBS)
                    .append(job.getFlinkId()).toString(), null, HttpMethod.PATCH);
            // 不触发保存点的情况下需要对requestId和setSavepointPath重置
            job.setSavepointPath(null);
            job.setRequestId(null);
        } else {
            // 请求触发保存点接口
            JSONObject jsonObject = requestSavepointsWithCancelJob(job.getFlinkId());
            String requestId = jsonObject.getString("request-id");
            job.setRequestId(requestId);
            // 异步调用创建savepoint接口
            asyncSavepoint(job.getFlinkId(), requestId, job);
        }
        job.setFlinkId(null);
    }

    private JSONObject requestSavepointsWithCancelJob(String flinkId) {
        JSONObject requestIdObj = null;
        // 触发保存点操作
        JSONObject requestBody = new JSONObject();
        requestBody.put("target-directory", FlinkConfig.targetDir());
        requestBody.put("cancel-job", "true");
        // 调用停止接口，获取request_id，并保存
        try {
            requestIdObj = httpClientService.doHttp(new StringBuilder().append(remoteUrl).append(FLINK_REST_PATH_JOBS)
                    .append(flinkId).append("/savepoints").toString(), requestBody, HttpMethod.POST);
        } catch (Exception e) {
            LOGGER.error("request Savepoints with Cancel-Job failed. ", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECT_TO_FLINK_FAIL);
        }
        return requestIdObj;
    }

    @Override
    public Map<String, FlinkBasicInfo> getJobsStatus() {
        JSONObject jobsStatus = null;
        try {
            jobsStatus = httpClientService.doHttp(remoteUrl + FLINK_REST_PATH_JOBS + "overview", null, HttpMethod.GET);
        } catch (IOException | URISyntaxException exception) {
            LOGGER.error("querying jobs status encountered exception", exception);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_GET_JOB_STATUS_ERROR, exception.getMessage());
        }
        JSONArray jobInfos = jobsStatus.getJSONArray("jobs");
        Map<String, FlinkBasicInfo> idStatusMap = new HashMap<>(jobInfos.size());
        for (int i = 0; i < jobInfos.size(); i++) {
            JSONObject json = jobInfos.getJSONObject(i);
            JobStatus jobStatus = Enum.valueOf(JobStatus.class, json.getString("state"));
            FlinkBasicInfo oldBasicInfo = idStatusMap.get(json.getString("jid"));
            if (oldBasicInfo != null && !oldBasicInfo.getStatus().equals(JobStatus.CANCELED)) {
                continue;
            }
            String jid = json.getString("jid");
            jobStatus = savepointingStatus(jid, jobStatus);
            idStatusMap.put(jid,
                    new FlinkBasicInfo(jobStatus, new Date(json.getLong("start-time")),
                            json.getLong(END_TIME).equals(-1L) ? null : new Date(json.getLong(END_TIME)),
                            json.getLong("duration")));
        }
        return idStatusMap;
    }

    /**
     * 查询保存点创建状态
     *
     * @param jid jobId
     * @param jobStatus jobStatus
     * @return 作业状态
     */
    private JobStatus savepointingStatus(String jid, JobStatus jobStatus) {
        // 根据id获取作业的request_id和save_point_location
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("The query job id is {}", jid);
        }
        StreamJobEntity streamJobEntity = jobRepository.findStreamJobEntityByFlinkId(jid);
        if (streamJobEntity == null) {
            return jobStatus;
        }

        String requestId = streamJobEntity.getRequestId();
        String savepointPath = streamJobEntity.getSavepointPath();
        if (requestId == null || StringUtils.isNotEmpty(savepointPath)) {
            return jobStatus;
        }
        // 只有requestId不为null且savepointPath为null时，才会进入下面的逻辑，表示正在创建savepoint
        JSONObject savepointJson = null;
        JSONObject statusJson = null;
        try {
            savepointJson = flinkSavepoint(jid, requestId);
            statusJson = savepointJson.getJSONObject(SAVEPOINT_STATUS);
        } catch (DataToolRuntimeException dataToolRuntimeException) {
            Throwable cause = dataToolRuntimeException.getCause();
            if (cause instanceof HttpRequestException) {
                int httpStatus = ((HttpRequestException) cause).getHttpStatus();
                if (httpStatus == HttpStatus.SC_NOT_FOUND) {
                    resetRequestId(streamJobEntity);
                }
            }
            LOGGER.error("Query savepointing exception:{}", dataToolRuntimeException);
        } catch (Exception exception) {
            LOGGER.error("Query savepointing exception:{}", exception);
        }
        return savepointStatusQuery(statusJson, savepointJson, streamJobEntity, jobStatus);
    }

    /**
     * 判断作业是否处于savepoing状态
     *
     * @param statusJson statusJson
     * @param savepointJson savepointJson
     * @param streamJobEntity streamJobEntity
     * @param jobStatus jobStatus
     * @return JobStatus
     */
    private JobStatus savepointStatusQuery(JSONObject statusJson, JSONObject savepointJson,
            StreamJobEntity streamJobEntity, JobStatus jobStatus) {
        if (statusJson != null) {
            String statusId = statusJson.getString("id");
            savepointPathCheck(statusId, savepointJson, streamJobEntity);
            if (IN_PROGRESS_STATUS.equals(statusId)) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("The flink job is savepointing");
                }
                return Enum.valueOf(JobStatus.class, "SAVEPOINTING");
            }
        }
        return jobStatus;
    }

    @Override
    public LogsResponse getLogList() {
        LogsResponse logsJson = null;
        try {
            String taskManagerId = this.getTaskManagerId();
            if (taskManagerId != null && !"".equals(taskManagerId)) {
                HttpGet httpGet = new HttpGet(
                        CommonUtil.encodeForURL(remoteUrl + "/taskmanagers/" + taskManagerId + "/logs"));
                String response = HttpClientPool.getHttpClient().execute(httpGet, new DataToolResponseHandler());
                logsJson = JSONObject.parseObject(response, LogsResponse.class);
            }
            // 处理响应，将flink-jobmanager.log、flink-taskmanager.log置顶
            if (logsJson != null) {
                List<LogResponse> logResponses = Optional.ofNullable(logsJson.getLogs()).orElse(Lists.newArrayList())
                        .stream().sorted((o1, o2) -> {
                            if (FlinkJobConstant.FLINK_JOB_MANAGER_LOG.equals(o1.getName())
                                    || FlinkJobConstant.FLINK_TASK_MANAGE_LOG.equals(o1.getName())) {
                                return -1;
                            }
                            return 0;
                        }).filter(logResponse -> logResponse.getName().startsWith(LOG_FILE_NAME_PREFIX))
                        .collect(Collectors.toList());
                logsJson.setLogs(logResponses);
            }
        } catch (Exception e) {
            LOGGER.error("Get flink log list error:", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECT_TO_FLINK_FAIL);
        }
        return logsJson;
    }

    @Override
    public InputStream getLog(String logName) {
        InputStream inputStream = null;
        try {
            String taskManagerId = this.getTaskManagerId();
            if (StringUtils.isNotEmpty(taskManagerId)) {
                HttpGet httpGet = new HttpGet(CommonUtil.encodeForURL(remoteUrl + "/taskmanagers/" + taskManagerId
                        + "/logs/" + NormalizerUtil.normalizeForString(logName)));
                inputStream = HttpClientPool.getHttpClient().execute(httpGet, new InputStreamResponseHandler());
            }
        } catch (Exception e) {
            LOGGER.error("Get log error:", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECT_TO_FLINK_FAIL);
        }
        return inputStream;
    }

    @Override
    public BaseResponse getTaskList(String jobId) {
        // 根据作业id获取flink_id
        JSONArray taskList = queryTasksByJobId(jobId);
        ArrayList<FlinkTask> flinkTasks = new ArrayList<>();
        if (taskList != null && !taskList.isEmpty()) {
            for (int i = 0; i < taskList.size(); i++) {
                FlinkTask flinkTask = new FlinkTask();
                JSONObject task = taskList.getJSONObject(i);
                String taskId = task.getString("id");
                flinkTask.setBackpressure(getBackpressure(jobId, taskId));
                flinkTask.setTaskId(taskId);
                flinkTask.setName(task.getString("name"));
                flinkTask.setDuration(task.getIntValue("duration"));
                flinkTask.setParallelism(task.getIntValue("parallelism"));
                flinkTask.setStatus(task.getString(SAVEPOINT_STATUS));
                flinkTask.setStartTime(task.getLongValue("start-time"));
                flinkTask.setEndTime(task.getLongValue(END_TIME));
                JSONObject metrics = task.getJSONObject("metrics");
                flinkTask.setWriteRecords(String.valueOf(metrics.getLongValue("write-records")));
                flinkTask.setReadRecords(String.valueOf(metrics.getLongValue("read-records")));
                flinkTask.setWriteBytes(new BigDecimal(metrics.getLongValue("write-bytes")).divide(new BigDecimal(1024))
                        .setScale(2, BigDecimal.ROUND_CEILING).toString());
                flinkTask.setReadBytes(new BigDecimal(metrics.getLongValue("read-bytes")).divide(new BigDecimal(1024))
                        .setScale(2, BigDecimal.ROUND_CEILING).toString());
                flinkTasks.add(flinkTask);
            }
        }
        FlinkTasksResponse tasksResponse = new FlinkTasksResponse();
        tasksResponse.setTasks(flinkTasks);
        return new BaseResponse(Constant.RESULT_CODE_SUCCESS, tasksResponse);
    }

    /**
     * 获取作业任务列表
     *
     * @param jobId 作业id
     * @return 作业任务列表
     */
    public JSONArray queryTasksByJobId(String jobId) {
        String flinkId = null;
        Optional<StreamJobEntity> streamJobOption = jobRepository.findById(jobId);
        if (streamJobOption.isPresent()) {
            flinkId = streamJobOption.get().getFlinkId();
        }
        JobStatusInfo jobStatusInfo = flinkServiceUtils.queryJobStatusById(jobId);
        if (flinkId == null || JobStatus.EXCEPTION_STOPPED.name().equals(jobStatusInfo.getStatus().name())) {
            return null;
        }
        JSONObject taskListObj;
        try {
            HttpGet httpGet = new HttpGet(CommonUtil.encodeForURL(remoteUrl + FLINK_REST_PATH_JOBS + flinkId));
            String response = HttpClientPool.getHttpClient().execute(httpGet, new DataToolResponseHandler());
            taskListObj = JSONObject.parseObject(response);
        } catch (Exception e) {
            LOGGER.error("Get task list error:", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECT_TO_FLINK_FAIL);
        }
        return taskListObj.getJSONArray("vertices");
    }

    /**
     * 查询作业任务反压状态并返回
     *
     * @param jobId 作业id
     * @param taskId 任务id
     * @return 任务反压状态
     */
    private String getBackpressure(String jobId, String taskId) {
        BackpressureResponse backpressureResponse = getVertexBackpressure(jobId, taskId);
        if (backpressureResponse == null || "deprecated".equals(backpressureResponse.getStatus())) {
            return "";
        }
        return backpressureResponse.getBackpressureLevel();
    }

    /**
     * 获取flink taskmanager id
     *
     * @return 集群id
     */
    private String getTaskManagerId() {
        JSONObject clusterJsonObj;
        try {
            HttpGet httpGet = new HttpGet(CommonUtil.encodeForURL(remoteUrl + "/taskmanagers"));
            String response = HttpClientPool.getHttpClient().execute(httpGet, new DataToolResponseHandler());
            clusterJsonObj = JSONObject.parseObject(response);
        } catch (Exception e) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECT_TO_FLINK_FAIL);
        }
        JSONArray clusterList = clusterJsonObj.getJSONArray("taskmanagers");
        if (clusterList != null && !clusterList.isEmpty()) {
            return clusterList.getJSONObject(0).getString("id");
        } else {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_GET_CLUSTER_ID_FAIL);
        }
    }

    /**
     * 异步执行创建保存点方法
     *
     * @param jobId jobId
     * @param requestId requestId
     * @param job job
     */
    private void asyncSavepoint(String jobId, String requestId, StreamJobEntity job) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("async create savepoint");
        }
        // 异步创建savepoint

        executor.execute(() -> {
            try {
                // 异步的创建保存点操作
                createSavepoint(jobId, requestId, job);
            } catch (Exception exception) {
                LOGGER.error("process handle exception: ", exception);
            }
        });
    }

    /**
     * 创建savepoint
     *
     * @param jobId Flink作业id
     * @param requestId stop接口返回的request-id
     * @param job Flink作业对象
     */
    private void createSavepoint(String jobId, String requestId, StreamJobEntity job) {
        try {
            pollingSavepoint(jobId, requestId, job);
        } catch (DataToolRuntimeException e) {
            // 记录创建保存点失败异常
            resetRequestId(job);
            LOGGER.error("Failed to create savepoint,exception encountered:{}", e);
        }
    }

    /**
     * 轮询创建并查询savepoint状态
     *
     * @param jobId jobId
     * @param requestId requestId
     * @param job job
     */
    private void pollingSavepoint(String jobId, String requestId, StreamJobEntity job) {
        JSONObject savepointJson = null;
        for (int i = 1; i < 7; i++) {
            // 调用Flink创建保存点接口
            savepointJson = flinkSavepoint(jobId, requestId);
            JSONObject statusJson = savepointJson.getJSONObject(SAVEPOINT_STATUS);
            savepointStatusCheck(statusJson, job);
            String statusId = statusJson.getString("id");
            if (COMPLETED_STATUS.equals(statusId)) {
                JSONObject locationJson = savepointJson.getJSONObject("operation");
                String location = locationJson.getString("location");
                if (location != null) {
                    job.setSavepointPath(location);
                    streamJobRepository.save(job);
                    break;
                } else {
                    resetRequestId(job);
                    LOGGER.error("Fail to create savepoint.rollback the request_id");
                    throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SAVEPOINT_CREATE_FAIL);
                }
            }
            if (IN_PROGRESS_STATUS.equals(statusId) && (i != 6)) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("polling query ,wait 5 seconds....");
                }
                waiting(5000);
            } else {
                // 轮询结束，重置request_id
                resetRequestId(job);
                LOGGER.error("Timeout to create savepoint,rollback the request_id.");
            }
        }
    }

    /**
     * 轮询等待
     *
     * @param timeout 等待时长
     */
    public void waiting(int timeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            LOGGER.error("Polling wait error:{}", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 调用Flink接口创建savepoint，获取响应
     *
     * @param jobId flink作业id
     * @param requestId stop接口返回的request-id
     * @return flink创建savepoint响应
     */
    private JSONObject flinkSavepoint(String jobId, String requestId) {
        JSONObject savepointJson = null;
        try {
            savepointJson = httpClientService.doHttp(new StringBuilder().append(remoteUrl).append(FLINK_REST_PATH_JOBS)
                    .append(jobId).append("/savepoints/").append(requestId).toString(), null, HttpMethod.GET);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("The response of create savepoint:{}", savepointJson);
            }
        } catch (Exception e) {
            LOGGER.error("Connection flink exception: {}", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECT_TO_FLINK_FAIL);
        }
        return savepointJson;
    }

    /**
     * 校验创建保存点响应，若statusJson为null，认为创建失败，request_id进行回滚
     *
     * @param statusJson statusJson
     * @param streamJobEntity stream_job
     */
    private void savepointStatusCheck(JSONObject statusJson, StreamJobEntity streamJobEntity) {
        if (statusJson == null) {
            // 记录日志，抛出创建保存点失败异常
            streamJobEntity.setRequestId(null);
            streamJobRepository.save(streamJobEntity);
            LOGGER.error(" The request_id is invalid, fail to create savepoint.");
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SAVEPOINT_CREATE_FAIL);
        }
    }

    /**
     * 校验保存点是否创建成功，若创建成功，将location保存，否则将request_id回滚
     *
     * @param statusId 创建保存点响应状态
     * @param savepointJson 创建保存点响应
     * @param job job
     */
    private void savepointPathCheck(String statusId, JSONObject savepointJson, StreamJobEntity job) {
        if (COMPLETED_STATUS.equals(statusId)) {
            JSONObject locationJson = savepointJson.getJSONObject("operation");
            String location = locationJson.getString("location");
            if (location != null) {
                job.setSavepointPath(location);
            } else {
                job.setRequestId(null);
                LOGGER.error("Fail to create savepoint.rollback the request_id");
            }
        }
        streamJobRepository.save(job);
    }

    /**
     * 容器启动时，检查一次request_id
     */
    @PostConstruct
    private void checkRequestId() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Check savepoint when start the project");
        }
        List<StreamJobEntity> jobs = streamJobRepository
                .findStreamJobEntitiesByRequestIdIsNotNullAndSavepointPathIsNotNull();
        jobs.stream().forEach((StreamJobEntity job) -> resetRequestId(job));
    }

    /**
     * 重置request_id
     *
     * @param job job
     */
    private void resetRequestId(StreamJobEntity job) {
        job.setRequestId(null);
        streamJobRepository.save(job);
    }

    @Override
    public JobExceptionsResponse getJobExceptions(String jobId) {
        JobStatusInfo jobStatusInfo = flinkServiceUtils.queryJobStatusById(jobId);
        JobStatus jobStatus = jobStatusInfo.getStatus();
        if (jobStatus.checkStopped()) {
            return null;
        }
        String flinkId = flinkServiceUtils.getFlinkIdByJobId(jobId);
        JobExceptionsResponse jobExceptionsResponse;
        try {
            HttpGet httpGet = new HttpGet(
                    CommonUtil.encodeForURL(remoteUrl + FLINK_REST_PATH_JOBS + flinkId + "/exceptions"));
            String response = HttpClientPool.getHttpClient().execute(httpGet, new DataToolResponseHandler());
            jobExceptionsResponse = JSONObject.parseObject(response, JobExceptionsResponse.class);
        } catch (Exception e) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECT_TO_FLINK_FAIL);
        }
        return jobExceptionsResponse;
    }

    @Override
    public WatermarksResponse getVerticesWatermarks(String jobId, String vertexId) {
        if (flinkServiceUtils.jobStopped(jobId)) {
            return null;
        }
        String flinkId = flinkServiceUtils.getFlinkIdByJobId(jobId);
        JSONArray result;
        try {
            HttpGet httpGet = new HttpGet(CommonUtil.encodeForURL(remoteUrl + FLINK_REST_PATH_JOBS + flinkId
                    + "/vertices/" + NormalizerUtil.normalizeForString(vertexId) + "/watermarks"));
            String response = HttpClientPool.getHttpClient().execute(httpGet, new DataToolResponseHandler());
            result = JSONObject.parseObject(response, JSONArray.class);
        } catch (Exception e) {
            LOGGER.error("Get watermark error!", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECT_TO_FLINK_FAIL);
        }
        WatermarksResponse watermarksResponse = new WatermarksResponse();
        watermarksResponse.setWatermarks(result);
        return watermarksResponse;
    }

    @Override
    public BackpressureResponse getVertexBackpressure(String jobId, String vertexId) {
        if (flinkServiceUtils.jobStopped(jobId)) {
            return null;
        }
        String flinkId = flinkServiceUtils.getFlinkIdByJobId(jobId);
        BackpressureResponse backpressure;
        try {
            HttpGet httpGet = new HttpGet(CommonUtil.encodeForURL(remoteUrl + FLINK_REST_PATH_JOBS + flinkId
                    + "/vertices/" + NormalizerUtil.normalizeForString(vertexId) + "/backpressure"));
            String response = HttpClientPool.getHttpClient().execute(httpGet, new DataToolResponseHandler());
            backpressure = JSONObject.parseObject(response, BackpressureResponse.class);
        } catch (Exception e) {
            LOGGER.error("Get Vertex Backpressure error!", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECT_TO_FLINK_FAIL);
        }
        return backpressure;
    }

    @Override
    public PlanResponse getJobPlan(String jobId) {
        if (flinkServiceUtils.jobStopped(jobId)) {
            return null;
        }
        String flinkId = flinkServiceUtils.getFlinkIdByJobId(jobId);
        PlanResponse plan;
        try {
            HttpGet httpGet = new HttpGet(
                    CommonUtil.encodeForURL(remoteUrl + FLINK_REST_PATH_JOBS + flinkId + "/plan"));
            String response = HttpClientPool.getHttpClient().execute(httpGet, new DataToolResponseHandler());
            plan = JSONObject.parseObject(response, PlanResponse.class);
        } catch (Exception e) {
            LOGGER.error("Get job plan Error!", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECT_TO_FLINK_FAIL);
        }
        return plan;
    }

    @Override
    public MetricsListResponse getMetricsList(String jobId, String vertexId) {
        return flinkServiceUtils.getMetricsListOrValue(jobId, vertexId, null);
    }

    @Override
    public MetricsListResponse getMetricsValue(String jobId, String vertexId, String metrics) {
        flinkServiceUtils.validateMetricsNum(metrics);
        return flinkServiceUtils.getMetricsListOrValue(jobId, vertexId, metrics);
    }

    @Override
    public boolean isJobRunning(String flinkId) {
        Optional<JobStatus> jobStatus = flinkServiceUtils.getJobStatusByFlinkId(flinkId);
        return jobStatus.isPresent() && !jobStatus.get().judgeBeDeleted();
    }
}
