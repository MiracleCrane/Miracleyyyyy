/*
 * 文 件 名:  BatchJobServiceImpl.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/17
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.impl;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.model.BaseResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.BatchJobService;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.CurrentDateRequest;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.batchjob.BatchJobInstanceStateCountResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.batchjob.BatchJobInstanceStateCountVo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.batchjob.BatchRunningJobInstanceCountRequest;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.batchjob.BatchRunningJobInstanceCountResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.batchjob.DagRunState;
import com.huawei.smartcampus.datatool.utils.TimeUtil;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.BatchJobGateway;
import com.huawei.smartcampus.datatool.vo.BatchRunningJobInstanceCountVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 批处理服务
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Service
public class BatchJobServiceImpl implements BatchJobService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchJobServiceImpl.class);

    @Autowired
    private BatchJobGateway batchJobGateway;

    @Override
    public BaseResponse queryTodayJobInstanceStateCount(CurrentDateRequest currentDateRequest) {
        String currentDate = currentDateRequest.getCurrentDate();
        TimeUtil.isValidDate(currentDate);
        Timestamp currentDateTs = TimeUtil.getLocalTimeStamp(currentDate);
        Timestamp currentDayStartTime = TimeUtil.getUtcZeroOClock(currentDateTs);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("start date: {}, end date: {}", currentDayStartTime, currentDateTs);
        }
        List<BatchJobInstanceStateCountVo> batchJobInstanceStateCountVoList = batchJobGateway
                .queryBatchJobInstanceStateCount(currentDayStartTime, currentDateTs);
        BatchJobInstanceStateCountResponse batchJobInstanceStateCountResponse = new BatchJobInstanceStateCountResponse();
        batchJobInstanceStateCountResponse
                .setFailed(getBatchJobInstanceStateCount(batchJobInstanceStateCountVoList, DagRunState.FAILED.value()));
        batchJobInstanceStateCountResponse.setSuccess(
                getBatchJobInstanceStateCount(batchJobInstanceStateCountVoList, DagRunState.SUCCESS.value()));
        // 查询dag_run表状态是running下的实际waiting状态的作业实例数量
        List<Long> waitingCountList = batchJobGateway.queryBatchJobInstanceStateWaitingCount(currentDayStartTime,
                currentDateTs);
        // dag_run的queued状态认为是waiting状态
        long queuedCount = getBatchJobInstanceStateCount(batchJobInstanceStateCountVoList, DagRunState.QUEUED.value());
        // 真正running状态数量是之前查询的running - waiting
        if (waitingCountList != null && !waitingCountList.isEmpty()) {
            long waitingCount = waitingCountList.get(0);
            batchJobInstanceStateCountResponse.setRunning(
                    getBatchJobInstanceStateCount(batchJobInstanceStateCountVoList, DagRunState.RUNNING.value())
                            - waitingCount);
            batchJobInstanceStateCountResponse.setWaiting(waitingCount + queuedCount);
        } else {
            batchJobInstanceStateCountResponse.setRunning(
                    getBatchJobInstanceStateCount(batchJobInstanceStateCountVoList, DagRunState.RUNNING.value()));
            batchJobInstanceStateCountResponse.setWaiting(queuedCount);
        }
        return BaseResponse.newOk(batchJobInstanceStateCountResponse);
    }

    @Override
    public BaseResponse queryDayRunningJobInstanceCount(
            BatchRunningJobInstanceCountRequest batchRunningJobInstanceCountRequest) {
        int interval = batchRunningJobInstanceCountRequest.getInterval();
        if (interval != 10 && interval != 60) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_TIME_INTERVAL_TYPE_NOT_SUPPORT,
                    String.valueOf(interval));
        }
        String currentDate = batchRunningJobInstanceCountRequest.getCurrentDate();
        TimeUtil.isValidDate(currentDate);
        Timestamp currentDateTs = Timestamp.valueOf(currentDate);
        String flooredYesterdayStr = getFlooredYesterday(currentDateTs, interval);
        Timestamp localFlooredYesterday = TimeUtil.getLocalTimeStamp(flooredYesterdayStr);
        List<String> timeAxis = initTimeAxis(flooredYesterdayStr, currentDate, interval);
        List<BatchRunningJobInstanceCountVo> batchRunningJobInstanceCountVoList = batchJobGateway
                .queryBatchRunningJobInstanceCount(interval, localFlooredYesterday,
                        TimeUtil.getLocalTimeStamp(currentDate));
        List<Long> countList = new ArrayList<>();
        fillRawData(timeAxis, batchRunningJobInstanceCountVoList, countList);
        BatchRunningJobInstanceCountResponse batchRunningJobInstanceCountResponse = new BatchRunningJobInstanceCountResponse();
        batchRunningJobInstanceCountResponse.setCountList(countList);
        batchRunningJobInstanceCountResponse.setTimeList(timeAxis);
        return BaseResponse.newOk(batchRunningJobInstanceCountResponse);
    }

    private long getBatchJobInstanceStateCount(List<BatchJobInstanceStateCountVo> batchJobInstanceStateCountVoList,
            String state) {
        if (batchJobInstanceStateCountVoList == null || batchJobInstanceStateCountVoList.isEmpty()) {
            return 0;
        }
        Optional<BatchJobInstanceStateCountVo> batchJobInstanceStateCountVoOptional = batchJobInstanceStateCountVoList
                .stream().filter(item -> state.equals(item.getState())).findFirst();
        if (batchJobInstanceStateCountVoOptional.isPresent()) {
            BatchJobInstanceStateCountVo batchJobInstanceStateCountVo = batchJobInstanceStateCountVoOptional.get();
            return batchJobInstanceStateCountVo.getCount();
        }
        return 0;
    }

    private String getFlooredYesterday(Timestamp currentDate, int interval) {
        Timestamp yesterday = Timestamp.from(currentDate.toInstant().minus(Duration.ofDays(1)));
        SimpleDateFormat sdf = new SimpleDateFormat(TimeUtil.COMMON_DATE_FORMAT);
        String yesterdayStr = sdf.format(yesterday);
        String minute = yesterdayStr.split(":")[1];
        // 根据interval向下取整
        int floorMinute = (Integer.parseInt(minute) / interval) * interval;
        // 向下取整结果小于10的补一个0
        String floorMinuteStr = floorMinute < 10 ? floorMinute + "0:00" : floorMinute + ":00";
        StringBuilder floorYesterdaySb = new StringBuilder(yesterdayStr);
        floorYesterdaySb.replace(14, 19, floorMinuteStr);
        return floorYesterdaySb.toString();
    }

    private List<String> initTimeAxis(String startFlooredDate, String endDate, int interval) {
        SimpleDateFormat sdf = new SimpleDateFormat(TimeUtil.COMMON_DATE_FORMAT);
        long endDateTime = Timestamp.valueOf(endDate).getTime();
        // index存储迭代的时间戳毫秒值
        long indexTime = Timestamp.valueOf(startFlooredDate).getTime();
        List<String> xAxis = new ArrayList<>();
        while (endDateTime > indexTime) {
            Timestamp timestamp = new Timestamp(indexTime);
            String timeX = sdf.format(timestamp);
            xAxis.add(timeX);
            // interval单位是minute，因此需要乘60*1000
            indexTime += interval * 60 * 1000;
        }
        return xAxis;
    }

    private void fillRawData(List<String> rawTimeAXis,
            List<BatchRunningJobInstanceCountVo> batchRunningJobInstanceCountVoList, List<Long> countAxis) {
        for (String rawTime : rawTimeAXis) {
            Optional<BatchRunningJobInstanceCountVo> countVoOptional = batchRunningJobInstanceCountVoList.stream()
                    .filter(item -> {
                        String timeInterval = item.getTimeInterval();
                        // 只有15位说明是整时，需要补一个0
                        if (timeInterval.length() == 15) {
                            timeInterval = timeInterval.concat("0:00");
                        } else {
                            timeInterval = timeInterval.concat(":00");
                        }
                        Timestamp localRawTime = TimeUtil.getLocalTimeStamp(rawTime);
                        return localRawTime.equals(Timestamp.valueOf(timeInterval));
                    }).findFirst();
            if (countVoOptional.isPresent()) {
                countAxis.add(countVoOptional.get().getCount());
            } else {
                countAxis.add(0L);
            }
        }
    }
}