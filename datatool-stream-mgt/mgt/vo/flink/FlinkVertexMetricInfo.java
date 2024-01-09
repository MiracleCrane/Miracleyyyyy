/*
 * 文 件 名:  FlinkVertexMetricInfo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2022/5/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo.flink;

/**
 * 查询Flink算子指标值接口入参
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2022/5/28]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class FlinkVertexMetricInfo {
    private String metrics;

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }
}
