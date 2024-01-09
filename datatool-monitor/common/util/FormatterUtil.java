/*
 * 文 件 名:  OverviewUtil.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/10/21
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.common.util;

import java.text.DecimalFormat;

/**
 * 概览工具类
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/21]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public final class FormatterUtil {
    private static final float EPSILON = 0.0001f;

    public static float formatNum(float num) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String roundedNumber = decimalFormat.format(num);
        return Float.parseFloat(roundedNumber);
    }

    public static boolean isZero(float num) {
        return Math.abs(num) < EPSILON;
    }

    public static float bytesToMB(long bytes) {
        return (float) (bytes / (1024.0 * 1024.0));
    }
}