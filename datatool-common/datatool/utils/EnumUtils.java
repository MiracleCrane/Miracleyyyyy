/*
 * 文 件 名:  EnumUtils.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/10/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 枚举值工具类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/10/10]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class EnumUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnumUtils.class);

    /**
     * 判断某个枚举是否包某个int类型值
     *
     * @param enumClass 需要判断是否存在那个枚举类中
     * @param code 需要判断的值
     * @param methodName 方法名称
     * @return 包含返回true，否则返回false
     */
    public static boolean isInclude(Class enumClass, int code, String methodName) {
        List enumList = new ArrayList(Arrays.asList(enumClass.getEnumConstants()));
        for (int i = 0; i < enumList.size(); i++) {
            Object en = enumList.get(i);
            Class<?> enClass = en.getClass();
            try {
                // 需要与枚举类方法对应
                Method method = enClass.getMethod(methodName);
                Object invoke = method.invoke(en, null);
                if (Integer.parseInt(invoke.toString()) == code) {
                    return true;
                }
            } catch (Exception e) {
                LOGGER.error("Enum execute {} method failed.", methodName);
            }
        }
        return false;
    }

    /**
     * 判断某个枚举是否包某个int类型值
     *
     * @param enumClass 需要判断是否存在那个枚举类中
     * @param code 需要判断的值
     * @param methodName 方法名称
     * @return 包含返回true，否则返回false
     */
    public static boolean isInclude(Class enumClass, String code, String methodName) {
        List enumList = new ArrayList(Arrays.asList(enumClass.getEnumConstants()));
        for (int i = 0; i < enumList.size(); i++) {
            Object en = enumList.get(i);
            Class<?> enClass = en.getClass();
            try {
                // 需要与枚举类方法对应
                Method method = enClass.getMethod(methodName);
                Object invoke = method.invoke(en, null);
                if (invoke.toString().equals(code)) {
                    return true;
                }
            } catch (Exception e) {
                LOGGER.error("Enum execute {} method failed.", methodName);
            }
        }
        return false;
    }
}