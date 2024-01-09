/*
 * 文 件 名:  DagRunState.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.batchjob;

/**
 * dag_run状态
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/2]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public enum DagRunState {
    QUEUED("queued"),
    RUNNING("running"),
    SUCCESS("success"),
    FAILED("failed");

    private final String value;

    DagRunState(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}