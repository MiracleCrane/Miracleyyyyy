/*
 * 文 件 名:  PlanNodesInput.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo.flink;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 查询作业计划，部分响应
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/15]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class PlanNodesInput {
    private int num;
    private String id;

    @JsonProperty(value = "ship_strategy")
    private String shipStrategy;

    private String exchange;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShipStrategy() {
        return shipStrategy;
    }

    public void setShipStrategy(String shipStrategy) {
        this.shipStrategy = shipStrategy;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}
