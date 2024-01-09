/*
 * 文 件 名:  ImportExportConfig.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/14
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.properties;

import com.huawei.smartcampus.datatool.properties.ApplicationProperties;

/**
 * 导入导出配置项
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/14]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ImportExportConfig {
    private static final ApplicationProperties PROPERTIES = ApplicationProperties.instance();
    private static final String STREAM_HOST = "datatool.stream.host";
    private static final String STREAM_START_URL = "datatool.stream.start.url";
    private static final String STREAM_STOP_URL = "datatool.stream.stop.url";
    private static final String BATCH_HOST = "datatool.batch.host";
    private static final String BATCH_START_URL = "datatool.batch.start.url";

    public static String streamHost() {
        return PROPERTIES.getString(STREAM_HOST);
    }

    public static String streamStartUrl() {
        return PROPERTIES.getString(STREAM_START_URL);
    }

    public static String streamStopUrl() {
        return PROPERTIES.getString(STREAM_STOP_URL);
    }

    public static String batchHost() {
        return PROPERTIES.getString(BATCH_HOST);
    }

    public static String getBatchStartUrl() {
        return PROPERTIES.getString(BATCH_START_URL);
    }
}