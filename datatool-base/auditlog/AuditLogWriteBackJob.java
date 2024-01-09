/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.base.auditlog;

import com.huawei.hicampus.campuscommon.auditlog.common.AuditLogValidateUtils;
import com.huawei.hicampus.campuscommon.auditlog.entity.AuditLog;
import com.huawei.smartcampus.datatool.base.auditlog.gateway.AuditLogGateway;
import com.huawei.smartcampus.datatool.entity.LocalAuditLogEntity;
import com.huawei.smartcampus.datatool.repository.LocalAuditLogRepository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

/**
 * 将本地的审计日志回写到审计日志服务中
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/9/23]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
@Component
public class AuditLogWriteBackJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogWriteBackJob.class);

    private static final int MAX_WRITE_BACK_BATCH_NUM = 50;

    private static final int BATCH_SELECT_MAX_NUM = 1000;

    private static final int WRITE_BACK_INTERVAL_SEC = 10;

    @Autowired
    private AuditLogGateway auditLogGateway;

    @Autowired
    private LocalAuditLogRepository localAuditLogRepository;

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void onStartup() {
        scheduledExecutorService.scheduleAtFixedRate(new Task(), 60, WRITE_BACK_INTERVAL_SEC, TimeUnit.SECONDS);
    }

    private class Task implements Runnable {
        @Override
        public void run() {
            try {
                long totalCnt = localAuditLogRepository.count();
                while (totalCnt > 0) {
                    int deleteCnt = batchWriteBackLocalAuditLog();
                    // deleteCnt 为0，则说明删除动作失败，退出定时任务，下一分钟定时任务创建后继续执行，而非连续尝试
                    if (deleteCnt == 0) {
                        break;
                    }
                    totalCnt -= deleteCnt;
                }
            } catch (Exception e) {
                LOGGER.error("delete localAuditLog failed.", e);
            }
        }
    }

    private List<AuditLog> filterInvalidAuditLog(List<AuditLog> auditLogs) {
        List<AuditLog> newAuditLogs = new ArrayList<>();
        for (AuditLog auditLog : auditLogs) {
            try {
                AuditLogValidateUtils.validateAuditLog(auditLog);
                // 合法性检查通过，放入过滤后的列表中
                newAuditLogs.add(auditLog);
            } catch (Exception e) {
                LOGGER.error("filter invalidAuditLog=" + JSON.toJSONString(auditLog) + "error=" + e.getMessage());
                localAuditLogRepository.deleteById(auditLog.getLocalAuditLogId());
            }
        }
        return newAuditLogs;
    }

    private int batchWriteBackLocalAuditLog() {
        List<AuditLog> orgAuditLogs = batchSelect(MAX_WRITE_BACK_BATCH_NUM);
        int sendCount = orgAuditLogs.size();

        // 防止本地存在不符合要求的审计日志，过滤掉不符合要求的日志记录
        List<AuditLog> filterAuditLogs = filterInvalidAuditLog(orgAuditLogs);
        if (filterAuditLogs.size() == 0) {
            // 全部是脏数据，过滤过程中已全部删除掉，直接返回
            return sendCount;
        }

        List<Long> ids = filterAuditLogs.stream().map(AuditLog::getLocalAuditLogId).collect(Collectors.toList());
        boolean isSuccess = auditLogGateway.sendBatchAuditLog(filterAuditLogs); // sendBatchAuditLog 中已经捕获异常
        if (isSuccess) {
            // 如果发送写入日志请求成功，则删除本地记录
            localAuditLogRepository.deleteAllByIdInBatch(ids);
        } else {
            sendCount = 0; // 发送失败则认为审计日志服务不可用，让外部调用跳出循环
            LOGGER.error("send batch auditLog to auditLogService failed.");
        }

        return sendCount;
    }

    private List<AuditLog> batchSelect(int count) {
        if (count <= 0) {
            return new ArrayList<>(0);
        }
        count = Math.min(count, BATCH_SELECT_MAX_NUM);
        List<LocalAuditLogEntity> auditLogs = localAuditLogRepository.batchSelect(count);
        List<AuditLog> result = new ArrayList<>();
        for (LocalAuditLogEntity localLog : auditLogs) {
            AuditLog auditLog = null;
            try {
                auditLog = JSONObject.parseObject(localLog.getContent(), AuditLog.class);
            } catch (Exception e) {
                LOGGER.error("parse local audit log to jsonObject failed: " + localLog.getContent());
                auditLog = new AuditLog();
            }
            auditLog.setLocalAuditLogId(localLog.getId());
            result.add(auditLog);
        }

        return result;
    }
}