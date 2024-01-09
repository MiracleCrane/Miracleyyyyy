/*
 * 文 件 名:  AuditLogProperties.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/9/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.properties;

/**
 * 审计日志配置
 *
 * @author s30009470
 * @version [Campus Core 23.0, 2023/9/23]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class AuditLogProperties {
    private static final ApplicationProperties PROPERTIES = ApplicationProperties.instance();

    private static final String AUDIT_ADDRESS = "auditlog.address";

    public static String auditAddress() {
        return PROPERTIES.getString(AUDIT_ADDRESS);
    }
}