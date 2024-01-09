/*
 * 文 件 名:  FlinkConfig.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.properties;

import com.huawei.smartcampus.datatool.properties.ApplicationProperties;

/**
 * 流处理相关配置
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/28]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class FlinkConfig {
    private static final ApplicationProperties PROPERTIES = ApplicationProperties.instance();

    private static final String FLINK_REMOTE_URL = "flink.remote.address";

    private static final String TARGET_DIR = "flink.savepoint.stop.targetDirectory";

    public static String flinkRemoteUrl() {
        return PROPERTIES.getString(FLINK_REMOTE_URL);
    }

    public static String targetDir() {
        return PROPERTIES.getString(TARGET_DIR);
    }
}
