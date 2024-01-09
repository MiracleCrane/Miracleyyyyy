/*
 * 文 件 名:  ChooseOne.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  ywx890060
 * 修改时间： 2021/5/17
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 校验注解
 * 创建校验short值多选一
 *
 * @author yWX890060
 * @version [SmartCampus V100R001C00, 2021/6/2]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChooseOneShortValidator.class)
public @interface ChooseOneShort {

    short[] value() default {};

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
