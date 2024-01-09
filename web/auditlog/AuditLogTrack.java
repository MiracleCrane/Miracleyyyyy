/*
 * 文 件 名:  AuditLogTrack.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l00610328
 * 修改时间： 2022/9/19
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.auditlog;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件类型注解
 * 用于获取审计事件类型的注解
 *
 * @author l00610328
 * @version [SmartCampus V100R001C00, 2022/9/19 ]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Documented
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLogTrack {
    /**
     * 事件类型
     */
    String operation();

    /**
     * 操作对象，用于记录审计日志
     */
    String operationObject() default "";

    /**
     * 是否导入和导出操作
     */
    boolean isImportOrExport() default false;

}
