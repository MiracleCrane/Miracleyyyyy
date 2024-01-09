/*
 * 文 件 名:  JobType.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.job;

/**
 * 作业类型
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/16]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public enum JobType {
    DATA_BATCH("DataBatch"),
    DATA_STREAM("DataStream");

    private String jobType;

    JobType(String jobType) {
        this.jobType = jobType;
    }

    public String jobType() {
        return jobType;
    }
}