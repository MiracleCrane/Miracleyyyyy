/*
 * 文 件 名:  FlinkTasksResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/14
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo.flink;

import java.util.List;

/**
 * 获取流处理作业中任务运行信息响应消息
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/14]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class FlinkTasksResponse {
    private List<FlinkTask> tasks;

    public List<FlinkTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<FlinkTask> tasks) {
        this.tasks = tasks;
    }
}