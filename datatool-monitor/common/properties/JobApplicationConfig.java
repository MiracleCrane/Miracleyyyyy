/*
 * 文 件 名:  JobApplicationConfig.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/17
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.common.properties;

import com.huawei.smartcampus.datatool.properties.ApplicationProperties;

/**
 * 作业应用配置
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class JobApplicationConfig {
    private static final ApplicationProperties PROPERTIES = ApplicationProperties.instance();
    private static final String STREAM_JOB_QUERY_URL = "datatool.stream.job.query.url";
    private static final String STREAM_JOB_DETAIL_QUERY_URL = "datatool.stream.job.detail.query.url";
    private static final String MAX_STREAM_JOBS_QUERY_URL = "stream.job.max.jobs.query.url";
    private static final String MAX_BATCH_JOBS_QUERY_URL = "batch.job.max.jobs.query.url";
    private static final String MAX_BATCH_SCRIPTS_QUERY_URL = "batch.job.max.scripts.query.url";

    public static String streamJobQueryUrl() {
        return PROPERTIES.getString(STREAM_JOB_QUERY_URL);
    }

    public static String streamJobDetailQueryUrl() {
        return PROPERTIES.getString(STREAM_JOB_DETAIL_QUERY_URL);
    }

    public static String maxStreamJobsQueryUrl() {
        return PROPERTIES.getString(MAX_STREAM_JOBS_QUERY_URL);
    }

    public static String maxBatchJobsQueryUrl() {
        return PROPERTIES.getString(MAX_BATCH_JOBS_QUERY_URL);
    }

    public static String maxBatchScriptsQueryUrl() {
        return PROPERTIES.getString(MAX_BATCH_SCRIPTS_QUERY_URL);
    }
}