/*
 * 文 件 名:  ParamsValidatorUtils.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/9/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

import java.util.regex.Pattern;

/**
 * 参数校验
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/30]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class ParamsValidatorUtils {
    public static boolean isMatch(Pattern pattern, String value) {
        return pattern.matcher(value).find();
    }
}