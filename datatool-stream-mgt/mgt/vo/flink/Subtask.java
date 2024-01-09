/*
 * 文 件 名:  Subtask.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/14
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo.flink;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 获取节点的反压状态，响应组成类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/14]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class Subtask {
    @JsonProperty(value = "backpressure-level")
    private String backpressureLevel;

    private String busyRatio;
    private Double idleRatio;
    private Double ratio;
    private Integer subtask;

    public String getBackpressureLevel() {
        return backpressureLevel;
    }

    public void setBackpressureLevel(String backpressureLevel) {
        this.backpressureLevel = backpressureLevel;
    }

    public String getBusyRatio() {
        return busyRatio;
    }

    public void setBusyRatio(String busyRatio) {
        this.busyRatio = busyRatio;
    }

    public Double getIdleRatio() {
        return idleRatio;
    }

    public void setIdleRatio(Double idleRatio) {
        this.idleRatio = idleRatio;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public void setSubtask(Integer subtask) {
        this.subtask = subtask;
    }

    public int getSubtask() {
        return subtask;
    }

    public void setSubtask(int subtask) {
        this.subtask = subtask;
    }
}
