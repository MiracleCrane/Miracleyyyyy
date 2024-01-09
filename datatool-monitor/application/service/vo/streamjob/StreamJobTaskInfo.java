/*
 * 文 件 名:  StreamJobTaskInfo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/13
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.streamjob;

/**
 * 流处理作业任务信息
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/13]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class StreamJobTaskInfo {
    private String taskId;
    private String name;
    private String backpressure;

    public StreamJobTaskInfo() {
    }

    public StreamJobTaskInfo(String taskId, String name, String backpressure) {
        this.taskId = taskId;
        this.name = name;
        this.backpressure = backpressure;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackpressure() {
        return backpressure;
    }

    public void setBackpressure(String backpressure) {
        this.backpressure = backpressure;
    }
}