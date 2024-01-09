/*
 * 文 件 名:  InspectionTask.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.inspection.task;

/**
 * 巡检任务，巡检执行的基本单元
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/1]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public interface InspectionTask {

    /**
     * 巡检任务id，用于标识区分任务
     * 
     * @return taskid
     */
    String id();

    /**
     * 巡检任务名称
     * 
     * @return name
     */
    String name();

    /**
     * 巡检任务开关
     * 
     * @return true/false
     */
    boolean enable();

    /**
     * 巡检间隔，单位：分钟
     * 
     * @return 间隔时间
     */
    int interval();

    /**
     * 执行巡检任务
     */
    void run();
}