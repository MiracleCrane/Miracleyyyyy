/*
 * 文 件 名:  PlanResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo.flink;

/**
 * 查询作业计划响应体
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/15]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class PlanResponse {
    private Plan plan;

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }
}
