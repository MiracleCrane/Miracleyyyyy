/*
 * 文 件 名:  FlinkJobConstant.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2023/4/11
 * 修改内容:  修改消息分发常量名，删除无用常量
 */

package com.huawei.smartcampus.datatool.stream.mgt.constan;

/**
 * Flink作业常量
 *
 * @author w00318695
 * @version [SmartCampus V100R001C00, 2021/5/9]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public final class FlinkJobConstant {
    /**
     * 算子指标分隔符
     */
    public static final String VERTEX_METRIC_SEPARATOR = ",";

    /**
     * 算子指标值查询接口请求参数的key，值由Flink原生接口定义
     */
    public static final String VERTEX_METRIC_REQUEST_KEY = "get";

    /**
     * flink运行日志，job日志
     */
    public static final String FLINK_JOB_MANAGER_LOG = "flink-jobmanager.log";

    /**
     * flink运行日志，task日志
     */
    public static final String FLINK_TASK_MANAGE_LOG = "flink-taskmanager.log";

    private FlinkJobConstant() {
        throw new IllegalStateException("Utility class");
    }
}
