/*
 * 文 件 名:  I18nConfig.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.0.T22
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/7
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.properties;

/**
 * 读取国际化的配置项
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/7]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class I18nConfig {
    private static final ApplicationProperties PROPERTIES = ApplicationProperties.instance();

    private static final String MESSAGES_BASENAME = "spring.messages.basename";

    private static final String MESSAGES_ENCODING = "spring.messages.encoding";

    public static String messagesBasename() {
        return PROPERTIES.getString(MESSAGES_BASENAME);
    }

    public static String messagesEncoding() {
        return PROPERTIES.getString(MESSAGES_ENCODING);
    }
}
