/*
 * 文 件 名:  ClassUtils.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/22
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * 类工具类
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/22]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public final class ClassUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtils.class);

    /**
     * 获取该对象的对应属性的值
     *
     * @param obj 对象
     * @param fieldName 属性名
     * @param args 可变入参（如果该参数的get方法有入参，则需要传入）
     * @param clazz 属性的类型
     * @return 属性值
     */
    public static <T> T getFieldValue(Object obj, String fieldName, Class<T> clazz, Object... args) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(fieldName, obj.getClass());
            Method method = pd.getReadMethod();
            Object value = method.invoke(obj, args);
            if (value != null) {
                return clazz.cast(method.invoke(obj));
            }
        } catch (Exception e) {
            LOGGER.error("get field value failed", e);
        }
        return null;
    }

    /**
     * 获取该对象的对应属性的类型
     *
     * @param obj 对象
     * @param fieldName 属性名
     * @return 属性类型
     */
    public static Class<?> getFieldType(Object obj, String fieldName) {
        try {
            return obj.getClass().getDeclaredField(fieldName).getType();
        } catch (NoSuchFieldException e) {
            LOGGER.error("no such field {} in class {}", fieldName, obj.getClass().getName(), e);
        }
        return null;
    }
}