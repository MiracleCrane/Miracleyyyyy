/*
 * 文 件 名:  MetricsResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo.flink;

/**
 * 获取算子指标列表响应
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/16]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class MetricsValue {
    private String id;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
