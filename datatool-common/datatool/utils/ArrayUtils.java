/*
 * 文 件 名:  ArrayUtils.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/22
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

/**
 * 数组工具类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/22]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ArrayUtils {
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0) ? true : false;
    }

    public static <T> T[] clone(final T[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }
}
