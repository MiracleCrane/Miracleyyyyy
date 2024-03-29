/*
 * 文 件 名:  AccessControl.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/6
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.concurrentlimit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 并发流控注解
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/11/6]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConcurrentLimit {
    int value() default 1;
}
