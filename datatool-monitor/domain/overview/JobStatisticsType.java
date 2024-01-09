/*
 * 文 件 名:  JobStatisticsType.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.overview;

/**
 * 作业统计数据类型
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/15]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public enum JobStatisticsType {
    BATCH_JOB("BatchJob"),
    STREAM_JOB("StreamJob"),
    BATCH_SCRIPT("BatchScript");


    private String jobStatisticsType;

    JobStatisticsType(String jobStatisticsType) {
        this.jobStatisticsType = jobStatisticsType;
    }

    public String jobStatisticsType() {
        return jobStatisticsType;
    }
}