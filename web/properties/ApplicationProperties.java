/*
 * 文 件 名:  ApplicationProperties.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.properties;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class ApplicationProperties extends AbstractProperties {
    private static ApplicationProperties properties = new ApplicationProperties("application.properties");

    protected ApplicationProperties(String filename) {
        super(filename);
    }

    /**
     * 返回类实例
     *
     * @return properties实例
     */
    public static ApplicationProperties instance() {
        return properties;
    }

}