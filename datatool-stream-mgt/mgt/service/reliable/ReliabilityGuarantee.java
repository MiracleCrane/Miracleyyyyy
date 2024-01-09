/*
 * 文 件 名:  ReliabilityGuarantee.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.service.reliable;

import com.huawei.smartcampus.datatool.entity.ReliabilityVariableEntity;
import com.huawei.smartcampus.datatool.entity.StreamJobEntity;
import com.huawei.smartcampus.datatool.repository.ReliabilityVarRepository;
import com.huawei.smartcampus.datatool.repository.StreamJobRepository;
import com.huawei.smartcampus.datatool.stream.mgt.enums.FlinkOperationName;
import com.huawei.smartcampus.datatool.stream.mgt.properties.FlinkConfig;
import com.huawei.smartcampus.datatool.stream.mgt.service.FlinkService;
import com.huawei.smartcampus.datatool.stream.mgt.service.key.ReliablityKey;
import com.huawei.smartcampus.datatool.stream.mgt.utils.HttpClientService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * flink可靠性保障类
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Transactional
@Service
public class ReliabilityGuarantee {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReliabilityGuarantee.class);

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private HttpClientService httpClientService;

    @Autowired
    private ReliabilityVarRepository reliabilityVarRepository;

    @Autowired
    private StreamJobRepository streamJobRepository;

    @Autowired
    private FlinkService flinkService;

    private ScheduledExecutorService executorService;

    @PreDestroy
    private void destroy() {
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(15, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException exception) {
                LOGGER.error("waiting thread is too long", exception);
            }
        }
    }

    @PostConstruct
    private void monitorFlinkAndRestartJob() {
        executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(this::processMonitorTask, 10, 15, TimeUnit.SECONDS);
    }

    private void processMonitorTask() {
        try {
            monitorFlinkAndRestartJobHandle();
        } catch (Exception e) {
            LOGGER.error(
                    "error encountered when getting transaction or rolling back db operations or restarting flink jobs",
                    e);
        }
    }

    /**
     * 监控Flink，重启作业实现逻辑
     */
    private void monitorFlinkAndRestartJobHandle() {
        boolean shouldRestartJobs = false;
        // 判断taskManager id是否变化，做重启动作
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Optional<ReliabilityVariableEntity> standaloneTaskManger = reliabilityVarRepository
                    .findByVarKey(ReliablityKey.STANDALONE_TASKMANGER_NAME);
            String currentTaskManagerId = this.getFlinkTaskManagerId();
            LOGGER.debug("current task manager id is {}", currentTaskManagerId);
            // 当数据库不存在，且接口获取的taskManagerId有值时，保存到数据库
            if (!standaloneTaskManger.isPresent() && currentTaskManagerId != null) {
                ReliabilityVariableEntity variableEntity = new ReliabilityVariableEntity(
                        ReliablityKey.STANDALONE_TASKMANGER_NAME, currentTaskManagerId);
                reliabilityVarRepository.save(variableEntity);
                transactionManager.commit(status);
                return;
            }
            // 当数据库存在，且接口获取的taskManagerId有值，且值不等于数据库的值时，更新数据库的值，并将需要重启操作标记置为true
            if (currentTaskManagerId != null
                    && !standaloneTaskManger.get().getVarValue().equals(currentTaskManagerId)) {
                shouldRestartJobs = true;
                ReliabilityVariableEntity taskManagerVar = standaloneTaskManger.get();
                taskManagerVar.setVarValue(currentTaskManagerId);
                reliabilityVarRepository.save(taskManagerVar);
            }
            transactionManager.commit(status);
        } catch (Throwable throwable) {
            LOGGER.error("error encountered when monitoring flink", throwable);
            transactionManager.rollback(status);
        }
        if (shouldRestartJobs) {
            this.restartSubmittedJob();
        }
    }

    private void restartSubmittedJob() {
        List<StreamJobEntity> jobsSubmitted = streamJobRepository.findByState(ReliablityKey.SUBMITTED_STATE);
        for (StreamJobEntity job : jobsSubmitted) {
            LOGGER.info("Restarting stream job {}, job name: {}", job.getId(), job.getName());
            try {
                flinkService.batchOperation(Collections.singletonList(job.getId()), FlinkOperationName.RUN, (short) 0);
            } catch (Throwable e) {
                // 启动异常
                LOGGER.error("Restarted stream job {}, job name: {} failed!", job.getId(), job.getName(), e);
            }
        }
    }

    private String getFlinkTaskManagerId() throws IOException, URISyntaxException {
        JSONObject result = httpClientService.doHttp(FlinkConfig.flinkRemoteUrl() + "/taskmanagers", null,
                HttpMethod.GET);
        JSONArray jsonArray = result.getJSONArray("taskmanagers");
        String currentTaskManagerId = null;
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            currentTaskManagerId = jsonObject.getString("id");
        }
        return currentTaskManagerId;
    }
}
