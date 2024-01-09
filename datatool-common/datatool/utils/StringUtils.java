/*
 * 文 件 名:  StringUtils.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/8/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

/**
 * 字符串工具类
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/8/1]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public final class StringUtils {
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen = (cs == null ? 0 : cs.length());
        if (strLen != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isNotEquals(String left, String right) {
        return !isEquals(left, right);
    }

    public static boolean isEquals(String left, String right) {
        if (left == null && right == null) {
            return true;
        } else if (left == null || right == null) {
            return false;
        } else {
            return left.equals(right);
        }
    }
}