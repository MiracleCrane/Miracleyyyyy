/*
 * 文 件 名:  BusinessTableScanTask.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/17
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application;

import com.huawei.smartcampus.datatool.monitor.domain.gateway.BusinessDbInfoGateway;
import com.huawei.smartcampus.datatool.repository.DtTableRecordRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

/**
 * 更新业务表询操作记录数量
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/11/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Component
public class BusinessTableScanTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessTableScanTask.class);

    private static final int SCAN_TABLE_INTERVAL_SEC = 60 * 5;
    private static final int SCAN_TABLE_INITIAL_DELAY = 60 * 5;

    @Autowired
    private BusinessDbInfoGateway businessDbInfoGateway;

    @Autowired
    private DtTableRecordRepository dtTableRecordRepository;

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void onStartup() {
        scheduledExecutorService.scheduleAtFixedRate(new Task(), SCAN_TABLE_INITIAL_DELAY, SCAN_TABLE_INTERVAL_SEC,
                TimeUnit.SECONDS);
    }

    private class Task implements Runnable {
        @Override
        public void run() {
            try {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Start to query the usage records of the service database.");
                }
                businessDbInfoGateway.recordRealTimeTableRecord();
            } catch (Exception e) {
                LOGGER.error("Failed to query the table operation record.", e);
            }
        }
    }
}