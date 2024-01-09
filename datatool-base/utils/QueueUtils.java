/*
 * 文 件 名:  QueueUtils.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/11
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.utils;

import com.huawei.smartcampus.datatool.base.handler.importdata.chain.ImportHandlerChain;
import com.huawei.smartcampus.datatool.base.model.TaskModel;
import com.huawei.smartcampus.datatool.utils.RequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 任务队列
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/11]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Component
public class QueueUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueUtils.class);

    // LinkedBlockingQueue构造的时候若没有指定大小，则默认大小为Integer.MAX_VALUE
    private final LinkedBlockingQueue<TaskModel> tasks = new LinkedBlockingQueue<>(50000);

    // 类似于一个线程总管 保证所有的任务都在队列之中
    private ExecutorService service = Executors.newSingleThreadExecutor();

    @Autowired
    private ImportHandlerChain importHandlerChain;

    /**
     * 初始化线程
     */
    @PostConstruct
    public void init() {
        // 启动消费线程
        Runnable taskModelHandle = () -> {
            while (true) {
                try {
                    // 开始一个任务,take()出队列,当队列为空时阻塞
                    TaskModel task = tasks.take();
                    RequestContext.set(RequestContext.USER_NAME_FIELD, task.getCreator());
                    RequestContext.set(RequestContext.USER_TYPE_FIELD, task.getCreatorType());
                    LocaleContextHolder.setLocale(task.getLocale());
                    importHandlerChain.handler(task);
                } catch (Exception exception) {
                    LOGGER.error("process handle exception: ", exception);
                }
            }
        };
        service.execute(taskModelHandle);
    }

    // 防止内存泄漏
    @PreDestroy
    private void destroy() {
        if (service != null) {
            service.shutdown();
            try {
                if (!service.awaitTermination(15, TimeUnit.SECONDS)) {
                    service.shutdownNow();
                }
            } catch (InterruptedException e) {
                LOGGER.error("waiting thread is too long: ", e);
            }
        }
    }

    /**
     * 生产者，put队满阻塞等待
     *
     * @param taskModel 任务模型
     */
    public void addTaskModel(TaskModel taskModel) {
        try {
            tasks.put(taskModel);
        } catch (InterruptedException e) {
            LOGGER.error("putting task model in the queue was interrupted: ", e);
        }
    }
}