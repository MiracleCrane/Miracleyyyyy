/*
 * 文 件 名:  AlarmServiceImpl.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.impl;

import com.huawei.smartcampus.datatool.entity.AlarmDataEntity;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.model.BaseResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.AlarmService;
import com.huawei.smartcampus.datatool.monitor.application.service.AlarmServiceUtil;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.CurrentDateRequest;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmdata.AlarmDataCountInfo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmdata.AlarmDataCountQueryResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmdata.AlarmDataDetailInfo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmdata.AlarmDataDetailQueryRequest;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmdata.AlarmDataDetailQueryResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmstatistics.batch.BatchTaskExecutionTimeoutStatisticsResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmstatistics.batch.BatchTaskExecutionTimeoutStatisticsVo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmstatistics.batch.BatchTaskFailStatisticsResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmstatistics.batch.BatchTaskFailStatisticsVo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmstatistics.batch.BatchTaskWaitingTimeoutStatisticsResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmstatistics.batch.BatchTaskWaitingTimeoutStatisticsVo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmstatistics.stream.StreamJobBackpressureStatisticsResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmstatistics.stream.StreamJobBackpressureStatisticsVo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmstatistics.stream.StreamJobFailStatisticsResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmstatistics.stream.StreamJobFailStatisticsVo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmDataOperator;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmStatisticsOperator;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmType;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata.AlarmDataCountVo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.batch.TaskExecutionTimeoutStat;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.batch.TaskFailStat;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.batch.TaskWaitingTimeoutStat;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.stream.StreamJobBackpressureStat;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmstatistics.stream.StreamJobFailStat;
import com.huawei.smartcampus.datatool.monitor.domain.job.JobType;
import com.huawei.smartcampus.datatool.utils.StringUtils;
import com.huawei.smartcampus.datatool.utils.TimeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 告警服务
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/15]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Service
public class AlarmServiceImpl implements AlarmService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmServiceImpl.class);

    @Autowired
    private AlarmDataOperator alarmDataOperator;

    @Autowired
    private AlarmStatisticsOperator alarmStatisticsOperator;

    @Override
    public BaseResponse queryCurrentAlarmCount() {
        List<AlarmDataCountVo> alarmDataCountVoList = alarmDataOperator.queryCurrentAlarmCount();
        List<AlarmDataCountInfo> alarmDataCountInfoList = new ArrayList<>();
        List<String> usedAlarmId = new ArrayList<>();
        for (AlarmDataCountVo alarmDataCountVo : alarmDataCountVoList) {
            String alarmId = alarmDataCountVo.getAlarmId();
            String alarmName = AlarmType.getAlarmNameByAlarmId(alarmId);
            if (StringUtils.isNotEmpty(alarmName)) {
                AlarmDataCountInfo alarmDataCountInfo = new AlarmDataCountInfo(alarmName, alarmDataCountVo.getCount());
                alarmDataCountInfoList.add(alarmDataCountInfo);
                usedAlarmId.add(alarmId);
            }
        }
        // 表中没有的alarmId需要在响应中补充该告警名称，数量为0
        for (AlarmType alarmType : AlarmType.values()) {
            if (!usedAlarmId.contains(alarmType.alarmId())) {
                alarmDataCountInfoList.add(new AlarmDataCountInfo(alarmType.alarmName(), 0));
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("query alarm count list result: {}", alarmDataCountInfoList);
        }
        AlarmDataCountQueryResponse alarmDataCountQueryResponse = new AlarmDataCountQueryResponse();
        alarmDataCountQueryResponse.setAlarmCountList(alarmDataCountInfoList);
        return BaseResponse.newOk(alarmDataCountQueryResponse);
    }

    @Override
    public BaseResponse queryCurrentAlarmDetail(AlarmDataDetailQueryRequest alarmDataDetailQueryRequest) {
        int pageSize = alarmDataDetailQueryRequest.getPageSize();
        int pageIndex = alarmDataDetailQueryRequest.getPageIndex();
        String alarmTypeStr = alarmDataDetailQueryRequest.getAlarmType();
        String alarmIdInput = null;
        if (!StringUtils.isEmpty(alarmTypeStr)) {
            AlarmServiceUtil.checkAlarmType(alarmTypeStr);
            alarmIdInput = AlarmType.getAlarmIdByAlarmName(alarmTypeStr);
        }
        Page<AlarmDataEntity> alarmDataEntities = alarmDataOperator.queryCurrentAlarmDetail(pageSize, pageIndex,
                alarmIdInput, alarmDataDetailQueryRequest.getJobName());
        long totalCount = alarmDataEntities.getTotalElements();
        List<AlarmDataDetailInfo> alarmDataDetailInfoList = new ArrayList<>();
        for (AlarmDataEntity alarmDataEntity : alarmDataEntities.getContent()) {
            String createDate = TimeUtil.getUtcDate(alarmDataEntity.getCreateDate());
            String alarmId = alarmDataEntity.getAlarmId();
            String alarmType = AlarmType.getAlarmNameByAlarmId(alarmId);
            String jobName = alarmDataEntity.getName();
            String jobId = alarmDataEntity.getId();
            if (AlarmType.BATCH_TASK_FAIL.alarmId().equals(alarmId)) {
                // 去除jobName和jobId中拼接的时间戳
                String[] jobNameArray = jobName.split("_");
                int charNum = jobNameArray[jobNameArray.length - 1].length();
                jobName = jobName.substring(0, jobName.length() - charNum - 1);
                jobId = jobId.substring(0, jobId.length() - charNum - 1);
            }
            String jobType = getJobTypeByAlarmType(AlarmType.getAlarmTypeByAlarmId(alarmId));
            AlarmDataDetailInfo alarmDataDetailInfo = new AlarmDataDetailInfo(jobType, alarmType, createDate, jobId,
                    jobName);
            alarmDataDetailInfoList.add(alarmDataDetailInfo);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("query alarm detail list result: {}", alarmDataDetailInfoList);
        }
        AlarmDataDetailQueryResponse alarmDataDetailQueryResponse = new AlarmDataDetailQueryResponse();
        alarmDataDetailQueryResponse.setAlarmList(alarmDataDetailInfoList);
        alarmDataDetailQueryResponse.setTotal(totalCount);
        return BaseResponse.newOk(alarmDataDetailQueryResponse);
    }

    @Override
    public BaseResponse queryBatchTaskExecutionTimeoutStatistics(CurrentDateRequest currentDateRequest) {
        String currentDate = currentDateRequest.getCurrentDate();
        TimeUtil.isValidDate(currentDate);
        Timestamp currentDateTs = TimeUtil.getLocalTimeStamp(currentDate);
        Timestamp oneMonthBefore = Timestamp.from(currentDateTs.toInstant().minus(Duration.ofDays(30)));
        List<TaskExecutionTimeoutStat> taskExecutionTimeoutStatList = alarmStatisticsOperator
                .getAlarmTaskExecutionTimeoutStatistics(oneMonthBefore, currentDateTs);
        List<BatchTaskExecutionTimeoutStatisticsVo> jobList = new ArrayList<>();
        for (TaskExecutionTimeoutStat taskExecutionTimeoutStat : taskExecutionTimeoutStatList) {
            BatchTaskExecutionTimeoutStatisticsVo batchTaskExecutionTimeoutStatisticsVo = new BatchTaskExecutionTimeoutStatisticsVo(
                    taskExecutionTimeoutStat.getJobId(), taskExecutionTimeoutStat.getJobName(),
                    taskExecutionTimeoutStat.getExecutionTimeoutCount(),
                    Math.round(taskExecutionTimeoutStat.getAvgExecutionTime()),
                    Math.round(taskExecutionTimeoutStat.getMaxExecutionTime()));
            jobList.add(batchTaskExecutionTimeoutStatisticsVo);
        }
        int total = taskExecutionTimeoutStatList.size();
        BatchTaskExecutionTimeoutStatisticsResponse batchTaskExecutionTimeoutStatisticsResponse = new BatchTaskExecutionTimeoutStatisticsResponse();
        batchTaskExecutionTimeoutStatisticsResponse.setJobList(jobList);
        batchTaskExecutionTimeoutStatisticsResponse.setTotal(total);
        return BaseResponse.newOk(batchTaskExecutionTimeoutStatisticsResponse);
    }

    @Override
    public BaseResponse queryBatchTaskFailStatistics(CurrentDateRequest currentDateRequest) {
        String currentDate = currentDateRequest.getCurrentDate();
        TimeUtil.isValidDate(currentDate);
        Timestamp currentDateTs = TimeUtil.getLocalTimeStamp(currentDate);
        Timestamp oneMonthBefore = Timestamp.from(currentDateTs.toInstant().minus(Duration.ofDays(30)));
        List<TaskFailStat> taskFailStatList = alarmStatisticsOperator.getAlarmTaskFailStatistics(oneMonthBefore,
                currentDateTs);
        List<BatchTaskFailStatisticsVo> jobList = new ArrayList<>();
        for (TaskFailStat taskFailStat : taskFailStatList) {
            BatchTaskFailStatisticsVo batchTaskFailStatisticsVo = new BatchTaskFailStatisticsVo(taskFailStat.getJobId(),
                    taskFailStat.getJobName(), TimeUtil.getUtcDateFromNoHyphen(taskFailStat.getMaxCreateDate()),
                    taskFailStat.getTaskFailCount());
            jobList.add(batchTaskFailStatisticsVo);
        }
        int total = taskFailStatList.size();
        BatchTaskFailStatisticsResponse batchTaskFailStatisticsResponse = new BatchTaskFailStatisticsResponse();
        batchTaskFailStatisticsResponse.setJobList(jobList);
        batchTaskFailStatisticsResponse.setTotal(total);
        return BaseResponse.newOk(batchTaskFailStatisticsResponse);
    }

    @Override
    public BaseResponse queryBatchTaskWaitingTimeoutStatistics(CurrentDateRequest currentDateRequest) {
        String currentDate = currentDateRequest.getCurrentDate();
        TimeUtil.isValidDate(currentDate);
        Timestamp currentDateTs = TimeUtil.getLocalTimeStamp(currentDate);
        Timestamp oneMonthBefore = Timestamp.from(currentDateTs.toInstant().minus(Duration.ofDays(30)));
        List<TaskWaitingTimeoutStat> taskWaitingTimeoutStatList = alarmStatisticsOperator
                .getAlarmTaskWaitingTimeoutStatistics(oneMonthBefore, currentDateTs);
        List<BatchTaskWaitingTimeoutStatisticsVo> jobList = new ArrayList<>();
        for (TaskWaitingTimeoutStat taskWaitingTimeoutStat : taskWaitingTimeoutStatList) {
            BatchTaskWaitingTimeoutStatisticsVo batchTaskWaitingTimeoutStatisticsVo = new BatchTaskWaitingTimeoutStatisticsVo(
                    taskWaitingTimeoutStat.getJobId(), taskWaitingTimeoutStat.getJobName(),
                    taskWaitingTimeoutStat.getWaitingTimeoutCount(),
                    Math.round(taskWaitingTimeoutStat.getAvgWaitingTime()),
                    Math.round(taskWaitingTimeoutStat.getMaxWaitingTime()));
            jobList.add(batchTaskWaitingTimeoutStatisticsVo);
        }
        int total = taskWaitingTimeoutStatList.size();
        BatchTaskWaitingTimeoutStatisticsResponse batchTaskWaitingTimeoutStatisticsResponse = new BatchTaskWaitingTimeoutStatisticsResponse();
        batchTaskWaitingTimeoutStatisticsResponse.setJobList(jobList);
        batchTaskWaitingTimeoutStatisticsResponse.setTotal(total);
        return BaseResponse.newOk(batchTaskWaitingTimeoutStatisticsResponse);
    }

    @Override
    public BaseResponse queryStreamJobFailStatistics(CurrentDateRequest currentDateRequest) {
        String currentDate = currentDateRequest.getCurrentDate();
        TimeUtil.isValidDate(currentDate);
        Timestamp currentDateTs = TimeUtil.getLocalTimeStamp(currentDate);
        Timestamp oneMonthBefore = Timestamp.from(currentDateTs.toInstant().minus(Duration.ofDays(30)));
        List<StreamJobFailStat> streamJobFailStatList = alarmStatisticsOperator
                .getAlarmStreamJobFailStatistics(oneMonthBefore, currentDateTs);
        List<StreamJobFailStatisticsVo> jobList = new ArrayList<>();
        for (StreamJobFailStat streamJobFailStat : streamJobFailStatList) {
            StreamJobFailStatisticsVo streamJobFailStatisticsVo = new StreamJobFailStatisticsVo(
                    streamJobFailStat.getJobId(), streamJobFailStat.getJobName(),
                    streamJobFailStat.getStreamJobFailCount(),
                    TimeUtil.getUtcDate(streamJobFailStat.getMaxCreateDate()));
            jobList.add(streamJobFailStatisticsVo);
        }
        int total = streamJobFailStatList.size();
        StreamJobFailStatisticsResponse streamJobFailStatisticsResponse = new StreamJobFailStatisticsResponse();
        streamJobFailStatisticsResponse.setJobList(jobList);
        streamJobFailStatisticsResponse.setTotal(total);
        return BaseResponse.newOk(streamJobFailStatisticsResponse);
    }

    @Override
    public BaseResponse queryStreamJobBackpressureStatistics(CurrentDateRequest currentDateRequest) {
        String currentDate = currentDateRequest.getCurrentDate();
        TimeUtil.isValidDate(currentDate);
        Timestamp currentDateTs = TimeUtil.getLocalTimeStamp(currentDate);
        Timestamp oneMonthBefore = Timestamp.from(currentDateTs.toInstant().minus(Duration.ofDays(30)));
        List<StreamJobBackpressureStat> streamJobBackpressureStatList = alarmStatisticsOperator
                .getAlarmStreamJobBackpressureStatistics(oneMonthBefore, currentDateTs);
        List<StreamJobBackpressureStatisticsVo> jobList = new ArrayList<>();
        for (StreamJobBackpressureStat streamJobBackpressureStat : streamJobBackpressureStatList) {
            Double recoverTime = streamJobBackpressureStat.getAvgRecoverTime();
            Long avgRecoverTime = null;
            if (recoverTime != null) {
                avgRecoverTime = Math.round(recoverTime);
            }
            StreamJobBackpressureStatisticsVo streamJobBackpressureStatisticsVo = new StreamJobBackpressureStatisticsVo(
                    streamJobBackpressureStat.getJobId(), streamJobBackpressureStat.getJobName(),
                    streamJobBackpressureStat.getStreamJobBackpressureCount(), avgRecoverTime);
            jobList.add(streamJobBackpressureStatisticsVo);
        }
        int total = streamJobBackpressureStatList.size();
        StreamJobBackpressureStatisticsResponse streamJobBackpressureStatisticsResponse = new StreamJobBackpressureStatisticsResponse();
        streamJobBackpressureStatisticsResponse.setJobList(jobList);
        streamJobBackpressureStatisticsResponse.setTotal(total);
        return BaseResponse.newOk(streamJobBackpressureStatisticsResponse);
    }

    private String getJobTypeByAlarmType(AlarmType alarmType) {
        switch (alarmType) {
            case BATCH_TASK_FAIL:
            case BATCH_TASK_EXECUTION_TIMEOUT:
            case BATCH_TASK_WAITING_TIMEOUT:
            case BATCH_TASK_QUEUED_TIMEOUT:
                return JobType.DATA_BATCH.jobType();
            case STREAM_JOB_FAIL:
            case STREAM_JOB_BACKPRESSURE:
                return JobType.DATA_STREAM.jobType();
            default:
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ALARM_TYPE_NOT_SUPPORT, alarmType.name());
        }
    }
}