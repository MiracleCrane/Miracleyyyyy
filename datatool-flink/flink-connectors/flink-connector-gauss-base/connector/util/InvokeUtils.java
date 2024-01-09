/*
 * 文 件 名:  InvokeUtils.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/9/9
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * 反射用工具类
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/9/9]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class InvokeUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvokeUtils.class);

    /**
     * 反射修改final属性的值
     * 
     * @param object 反射对象
     * @param fieldName 待修改属性名
     * @param newFieldValue 新属性值
     * @throws NoSuchFieldException 异常
     * @throws IllegalAccessException 异常
     */
    public static void invokeModify(Object object, String fieldName, Object newFieldValue)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        final boolean[] invokeFailed = new boolean[1];
        invokeFailed[0] = false;

        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            try {
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(object, newFieldValue);
            } catch (Exception e) {
                invokeFailed[0] = true;
                LOGGER.error("invokeModify failed, IllegalAccess fieldname:" + fieldName);
            }
            return null;
        });

        if (invokeFailed[0]) {
            throw new IllegalAccessException();
        }
    }
}