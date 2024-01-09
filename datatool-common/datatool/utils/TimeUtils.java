/*
 * 文 件 名:  TimeUtils.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/9/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间处理工具
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/28]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public final class TimeUtils {
    public static String longToUtcDate(long value) {
        Date date = new Date(value);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }
}