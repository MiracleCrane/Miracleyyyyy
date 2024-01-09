/*
 * 文 件 名:  RegexValidationRule.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/10/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.pattern;

import java.util.regex.Pattern;

/**
 * 正则表达式校验规则
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/28]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public final class RegexValidationRule {
    public static final Pattern ID_PATTERN = Pattern.compile("^[a-f0-9]{32}$");

    public static final Pattern DATABASE_NAME_PATTERN = Pattern.compile("^.{1,255}$");

    public static final Pattern COMMON_CONFIG_CUSTOM_SUFFIXES_PATTERN = Pattern.compile("^_[a-zA-Z0-9]{1,9}$");
}