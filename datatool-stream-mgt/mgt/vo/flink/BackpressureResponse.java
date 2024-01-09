/*
 * 文 件 名:  BackpressureResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/14
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo.flink;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 获取节点的反压状态
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/14]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class BackpressureResponse {
    @JsonProperty(value = "backpressure-level")
    private String backpressureLevel;

    @JsonProperty(value = "end-timestamp")
    private Long endTimestamp;

    private String status;
    private List<Subtask> subtasks;

    public String getBackpressureLevel() {
        return backpressureLevel;
    }

    public void setBackpressureLevel(String backpressureLevel) {
        this.backpressureLevel = backpressureLevel;
    }

    public Long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }
}
