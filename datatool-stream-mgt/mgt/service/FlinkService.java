/*
 * 文 件 名:  FlinkService.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.service;

import com.huawei.smartcampus.datatool.model.BaseResponse;
import com.huawei.smartcampus.datatool.stream.mgt.enums.FlinkOperationName;
import com.huawei.smartcampus.datatool.stream.mgt.vo.BatchOperateResult;
import com.huawei.smartcampus.datatool.stream.mgt.vo.LogsResponse;
import com.huawei.smartcampus.datatool.vo.flink.StartStreamJobsReq;
import com.huawei.smartcampus.datatool.vo.flink.StopStreamJobsReq;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.BackpressureResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.FlinkBasicInfo;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.FlinkRunResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.JobExceptionsResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.MetricsListResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.PlanResponse;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.WatermarksResponse;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 服务层
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public interface FlinkService {
    /**
     * 批量操作
     *
     * @param ids id数组
     * @param operationName 操作名称
     * @param savepoint 保存点
     * @return FlinkRunResponse
     */
    FlinkRunResponse batchOperation(List<String> ids, FlinkOperationName operationName, Short savepoint);

    /**
     * 批量操作
     *
     * @param startStreamJobsReq 批量启动操作的
     * @return BatchOperateResult
     */
    BatchOperateResult batchStartOperation(StartStreamJobsReq startStreamJobsReq);

    /**
     * 批量操作
     *
     * @param stopStreamJobsReq 批量启动操作的
     * @return BatchOperateResult
     */
    BatchOperateResult batchStopOperation(StopStreamJobsReq stopStreamJobsReq);

    /**
     * 获取作业状态
     *
     * @return Map<String, FlinkBasicInfo>
     */
    Map<String, FlinkBasicInfo> getJobsStatus();

    /**
     * 获取日志列表
     *
     * @return LogsResponse
     */
    LogsResponse getLogList();

    /**
     * 获取日志
     *
     * @param logName 日志名称
     * @return InputStream
     */
    InputStream getLog(String logName);

    /**
     * 获取任务列表
     *
     * @param jobId 作业id
     * @return BaseResponse
     */
    BaseResponse getTaskList(String jobId);

    /**
     * 获取作业异常信息
     *
     * @param jobId jobId
     * @return JobExceptionsResponse
     */
    JobExceptionsResponse getJobExceptions(String jobId);

    /**
     * 获取节点watermarks
     *
     * @param jobId jobId
     * @param vertexId 节点id
     * @return WatermarksResponse
     */
    WatermarksResponse getVerticesWatermarks(String jobId, String vertexId);

    /**
     * 获取节点反压状态
     *
     * @param jobId jobId
     * @param vertexId 节点id
     * @return BackpressureResponse
     */
    BackpressureResponse getVertexBackpressure(String jobId, String vertexId);

    /**
     * 获取作业执行计划
     *
     * @param jobId jobId
     * @return PlanResponse
     */
    PlanResponse getJobPlan(String jobId);

    /**
     * 获取算子指标列表
     *
     * @param jobId 作业id
     * @param vertexId 算子id
     * @return MetricsListResponse
     */
    MetricsListResponse getMetricsList(String jobId, String vertexId);

    /**
     * 获取算子指标值
     *
     * @param jobId 作业id
     * @param vertexId 算子id
     * @param metrics 指标参数
     * @return MetricsListResponse
     */
    MetricsListResponse getMetricsValue(String jobId, String vertexId, String metrics);

    /**
     * 根据 flinkId 判断作业是否正在集群运行
     *
     * @param flinkId flinkId
     * @return true表示在运行，false表示没有在运行
     */
    boolean isJobRunning(String flinkId);
}
