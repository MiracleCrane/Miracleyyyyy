/*
 * 文 件 名:  TaskInstance.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/21
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * union primary key of task instance entity
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/21]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class TaskInstance implements Serializable {
    private static final long serialVersionUID = -3527970370886223186L;

    private String taskId;
    private String dagId;
    private String runId;
    private Integer mapIndex;

    /**
     * non-parameter constructor
     */
    public TaskInstance() {
    }

    public TaskInstance(String taskId, String dagId, String runId, Integer mapIndex) {
        this.taskId = taskId;
        this.dagId = dagId;
        this.runId = runId;
        this.mapIndex = mapIndex;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDagId() {
        return dagId;
    }

    public void setDagId(String dagId) {
        this.dagId = dagId;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public Integer getMapIndex() {
        return mapIndex;
    }

    public void setMapIndex(Integer mapIndex) {
        this.mapIndex = mapIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TaskInstance taskInstance = (TaskInstance) obj;
        return Objects.equals(taskId, taskInstance.taskId) && Objects.equals(dagId, taskInstance.dagId)
                && Objects.equals(runId, taskInstance.runId) && Objects.equals(mapIndex, taskInstance.mapIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, dagId, runId, mapIndex);
    }
}