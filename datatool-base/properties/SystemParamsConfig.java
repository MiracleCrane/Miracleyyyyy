/*
 * 文 件 名:  SystemParamsConfig.java
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
 * 系统参数配置项
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/14]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class SystemParamsConfig {
    private static final ApplicationProperties PROPERTIES = ApplicationProperties.instance();
    private static final String MAX_CUSTOM_SUFFIXES = "datatool.max.custom.suffixes";

    public static int maxCustomSuffixes() {
        return PROPERTIES.getInt(MAX_CUSTOM_SUFFIXES, 10);
    }
}