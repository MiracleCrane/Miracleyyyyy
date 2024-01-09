/*
 * 文 件 名:  StreamJobController.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.controller;

import com.huawei.hicampus.campuscommon.common.accessLimit.AccessFreq;
import com.huawei.smartcampus.datatool.auditlog.AuditLogTrack;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.model.BaseResponse;
import com.huawei.smartcampus.datatool.stream.mgt.service.BusinessService;
import com.huawei.smartcampus.datatool.stream.mgt.service.FlinkService;
import com.huawei.smartcampus.datatool.stream.mgt.vo.DeleteStreamJobsReq;
import com.huawei.smartcampus.datatool.stream.mgt.vo.SaveUpdateResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.StreamJobQueryCondi;
import com.huawei.smartcampus.datatool.stream.mgt.vo.StreamJobReq;
import com.huawei.smartcampus.datatool.stream.mgt.vo.ValidateNameReq;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.FlinkVertexMetricInfo;
import com.huawei.smartcampus.datatool.validation.groups.DataToolGroup;
import com.huawei.smartcampus.datatool.vo.flink.StartStreamJobsReq;
import com.huawei.smartcampus.datatool.vo.flink.StopStreamJobsReq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 实时调测作业运行管理的控制层
 * 功能包含实时调测作业的运行、停止和日志下载、节点查看等
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@RestController
public class StreamJobController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamJobController.class);

    @Autowired
    private FlinkService flinkService;

    @Autowired
    private BusinessService jobBusinessService;

    /**
     * 创建作业
     *
     * @param jobInfo 作业配置
     * @return BaseResponse
     */
    @PostMapping(value = "/v1/jobs")
    @AccessFreq(period = "${stream.access.limit.period}", times = "${stream.access.limit.times}")
    @AuditLogTrack(operation = "create stream job", operationObject = "stream job")
    public BaseResponse createJob(@RequestBody @Validated(DataToolGroup.class) StreamJobReq jobInfo) {
        return BaseResponse.newOk(new SaveUpdateResponse(jobBusinessService.saveJob(jobInfo)));
    }

    /**
     * 编辑作业
     *
     * @param jobId 作业Id
     * @param jobInfo 作业配置
     * @return BaseResponse
     */
    @PutMapping(value = "/v1/jobs/{jobId}")
    @AccessFreq(period = "${stream.access.limit.period}", times = "${stream.access.limit.times}")
    @AuditLogTrack(operation = "edit stream job", operationObject = "stream job")
    public BaseResponse editJob(@PathVariable String jobId,
            @RequestBody @Validated(DataToolGroup.class) StreamJobReq jobInfo) {
        return BaseResponse.newOk(new SaveUpdateResponse(jobBusinessService.updateJob(jobId, jobInfo)));
    }

    /**
     * 删除作业
     *
     * @param deleteStreamJobsReq 批量操作
     * @return BaseResponse
     */
    @PostMapping(value = "/v1/jobs/delete")
    @AuditLogTrack(operation = "delete stream job", operationObject = "stream job")
    public BaseResponse deleteJob(@RequestBody @Valid DeleteStreamJobsReq deleteStreamJobsReq) {
        return BaseResponse.newOk(jobBusinessService.deleteJobs(deleteStreamJobsReq));
    }

    /**
     * 查询作业，批量查询
     *
     * @param jobQueryCondition 查询条件
     * @return BaseResponse
     */
    @GetMapping(value = "/v1/jobs")
    public BaseResponse queryJob(@Valid StreamJobQueryCondi jobQueryCondition) {
        return BaseResponse.newOk(jobBusinessService.queryJobs(jobQueryCondition));
    }

    /**
     * 获取作业详情
     *
     * @param jobId 作业id
     * @return BaseResponse
     */
    @GetMapping(value = "/v1/jobs/{jobId}")
    public BaseResponse getJobInfo(@PathVariable String jobId) {
        return BaseResponse.newOk(jobBusinessService.getDataStreamJobInfo(jobId));
    }

    /**
     * 查询作业状态
     *
     * @param jobId 作业id
     * @return BaseResponse
     */
    @GetMapping(value = "/v1/jobs/{jobId}/status")
    public BaseResponse queryJobStatusById(@PathVariable String jobId) {
        return BaseResponse.newOk(jobBusinessService.queryJobStatusById(jobId));
    }

    /**
     * 校验作业重名
     *
     * @param validateNameReq 校验重名
     * @return BaseResponse
     */
    @PostMapping(value = "/v1/jobs/name/validate")
    public BaseResponse checkJobName(@RequestBody @Valid ValidateNameReq validateNameReq) {
        return BaseResponse.newOk(jobBusinessService.checkJobName(validateNameReq));
    }

    /**
     * 批量运行
     *
     * @param startStreamJobsReq 批量操作
     * @return BaseResponse
     */
    @PostMapping(value = "/v1/jobs/start")
    @AccessFreq(period = "${stream.access.limit.period}", times = "${stream.access.limit.times}")
    @AuditLogTrack(operation = "run stream jobs in a batch", operationObject = "stream job")
    public BaseResponse batchRun(@RequestBody @Valid StartStreamJobsReq startStreamJobsReq) {
        return BaseResponse.newOk(flinkService.batchStartOperation(startStreamJobsReq));
    }

    /**
     * 批量取消
     *
     * @param stopStreamJobsReq 批量操作
     * @return BaseResponse
     */
    @PostMapping(value = "/v1/jobs/stop")
    @AccessFreq(period = "${stream.access.limit.period}", times = "${stream.access.limit.times}")
    @AuditLogTrack(operation = "stop stream jobs in a batch", operationObject = "stream job")
    public BaseResponse batchCancel(@RequestBody @Valid StopStreamJobsReq stopStreamJobsReq) {
        return BaseResponse.newOk(flinkService.batchStopOperation(stopStreamJobsReq));
    }

    /**
     * 获取作业详情
     *
     * @param jobId 作业id
     * @return BaseResponse
     */
    @GetMapping(value = "/v1/jobs/{jobId}/tasks")
    public BaseResponse jobDetails(@PathVariable String jobId) {
        return flinkService.getTaskList(jobId);
    }

    /**
     * 获取流处理作业日志列表
     *
     * @return BaseResponse
     */
    @GetMapping(value = "/v1/jobs/logs")
    public BaseResponse logList() {
        return BaseResponse.newOk(flinkService.getLogList());
    }

    /**
     * 日志下载
     *
     * @param logName 日志名称
     * @param response 响应
     */
    @GetMapping(value = "/v1/jobs/logs/{logName}")
    @AccessFreq(interval = "1s")
    @AuditLogTrack(operation = "download stream log by logName", operationObject = "stream job", isImportOrExport = true)
    public void logDownload(@PathVariable String logName, HttpServletResponse response) {
        try (ServletOutputStream outputStream = response.getOutputStream();
                InputStream logStream = flinkService.getLog(logName)) {
            if (null == logStream) {
                LOGGER.error("logStream is null, please check!");
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_FLINK_LOG_DOWNLOAD_ERROR);
            }
            byte[] buff = new byte[1024 * 1024];
            int len = 0;
            while ((len = logStream.read(buff)) > 0) {
                outputStream.write(buff, 0, len);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_FLINK_LOG_DOWNLOAD_ERROR);
        }
    }

    /**
     * 获取作业异常信息
     *
     * @param jobId 作业id
     * @return 异常信息响应
     */
    @GetMapping(value = "/v1/jobs/{jobId}/exception")
    public BaseResponse jobException(@PathVariable String jobId) {
        return BaseResponse.newOk(flinkService.getJobExceptions(jobId));
    }

    /**
     * 获取节点的watermarks
     *
     * @param jobId 作业id
     * @param taskId 顶点ID
     * @return BaseResponse 结果
     */
    @GetMapping(value = "/v1/jobs/{jobId}/tasks/{taskId}/watermarks")
    public BaseResponse watermarks(@PathVariable String jobId, @PathVariable String taskId) {
        return BaseResponse.newOk(flinkService.getVerticesWatermarks(jobId, taskId));
    }

    /**
     * 获取节点的反压状态
     *
     * @param jobId 作业id
     * @param taskId 顶点ID
     * @return BaseResponse 结果
     */
    @GetMapping(value = "/v1/jobs/{jobId}/tasks/{taskId}/backpressure")
    public BaseResponse backpressure(@PathVariable String jobId, @PathVariable String taskId) {
        return BaseResponse.newOk(flinkService.getVertexBackpressure(jobId, taskId));
    }

    /**
     * 获取作业的计划
     *
     * @param jobId 作业id
     * @return BaseResponse 结果
     */
    @GetMapping(value = "/v1/jobs/{jobId}/plan")
    public BaseResponse backpressure(@PathVariable String jobId) {
        return BaseResponse.newOk(flinkService.getJobPlan(jobId));
    }

    /**
     * 获取算子指标列表
     *
     * @param jobId 作业id
     * @param taskId 算子id
     * @return BaseResponse 结果
     */
    @GetMapping(value = "/v1/jobs/{jobId}/tasks/{taskId}/metrics")
    public BaseResponse getMetricsList(@PathVariable String jobId, @PathVariable String taskId) {
        return BaseResponse.newOk(flinkService.getMetricsList(jobId, taskId));
    }

    /**
     * 获取算子指标值
     *
     * @param jobId 作业id
     * @param taskId 算子id
     * @param metricInfo 算子指标入参
     * @return BaseResponse 结果
     */
    @PostMapping(value = "/v1/jobs/{jobId}/tasks/{taskId}/metrics/value/query")
    public BaseResponse getMetricsValue(@PathVariable String jobId, @PathVariable String taskId,
            @RequestBody FlinkVertexMetricInfo metricInfo) {
        return BaseResponse.newOk(flinkService.getMetricsValue(jobId, taskId, metricInfo.getMetrics()));
    }

    @GetMapping(value = "/v1/jobs/max-jobs")
    public BaseResponse queryMaxStreamJob() {
        return BaseResponse.newOk(jobBusinessService.queryMaxJobs());
    }
}
