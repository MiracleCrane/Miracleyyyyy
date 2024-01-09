/*
 * 文 件 名:  PlanNode.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo.flink;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 查询作业计划，部分响应
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/15]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class PlanNode {
    private List<PlanNodesInput> inputs;
    private int parallelism;
    private String description;

    @JsonProperty(value = "operator_strategy")
    private String operatorStrategy;

    private String id;
    private String operator;

    @JsonProperty(value = "optimizer_properties")
    private JSONObject optimizerProperties;

    public List<PlanNodesInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<PlanNodesInput> inputs) {
        this.inputs = inputs;
    }

    public int getParallelism() {
        return parallelism;
    }

    public void setParallelism(int parallelism) {
        this.parallelism = parallelism;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperatorStrategy() {
        return operatorStrategy;
    }

    public void setOperatorStrategy(String operatorStrategy) {
        this.operatorStrategy = operatorStrategy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public JSONObject getOptimizerProperties() {
        return optimizerProperties;
    }

    public void setOptimizerProperties(JSONObject optimizerProperties) {
        this.optimizerProperties = optimizerProperties;
    }
}
