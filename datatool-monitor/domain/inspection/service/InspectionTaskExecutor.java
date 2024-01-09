/*
 * 文 件 名:  InspectionTask.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.inspection.service;

import com.huawei.smartcampus.datatool.monitor.domain.inspection.task.InspectionTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 巡检模块服务，提供定时调度功能
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/1]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class InspectionTaskExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger("Alarm");

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private static InspectionTaskExecutor instance = new InspectionTaskExecutor();

    private InspectionTaskExecutor() {

    }

    public void schedule(InspectionTask task) {
        Command command = new Command(task);
        scheduledExecutorService.scheduleAtFixedRate(command, 600, task.interval(), TimeUnit.SECONDS);
        LOGGER.info("inspection task {} start.", task.name());
    }

    public static InspectionTaskExecutor instance() {
        return instance;
    }

    private static class Command implements Runnable {
        private InspectionTask innerTask;

        public Command(InspectionTask task) {
            this.innerTask = task;
        }

        @Override
        public void run() {
            if (!innerTask.enable()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("inspection task {} disable.", innerTask.name());
                }
                return;
            }

            try {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("inspection task {} begin.", innerTask.name());
                }

                innerTask.run();

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("inspection task {} end.", innerTask.name());
                }
            } catch (Exception e) {
                LOGGER.error("inspection task {} execute failed.", innerTask.name(), e);
            }
        }
    };
}