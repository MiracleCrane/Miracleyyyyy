/*
 * 文 件 名:  BooleanType.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/21
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.enums;

/**
 * 布尔类型枚举类
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/21]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public enum BooleanType {
    TRUE(true, "DATATOOL_TRUE"),
    FALSE(false, "DATATOOL_FALSE");

    private final boolean value;
    private final String i18nCode;

    BooleanType(boolean value, String i18nCode) {
        this.value = value;
        this.i18nCode = i18nCode;
    }

    public boolean value() {
        return value;
    }

    public String i18nCode() {
        return i18nCode;
    }

    public static String getI18nCodeByValue(boolean value) {
        for (BooleanType booleanType : BooleanType.values()) {
            if (booleanType.value == value) {
                return booleanType.i18nCode;
            }
        }
        return "";
    }
}