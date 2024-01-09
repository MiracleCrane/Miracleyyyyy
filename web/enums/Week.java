/*
 * 文 件 名:  Week.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.enums;

/**
 * 周枚举类
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public enum Week {
    MONDAY(1,"DATATOOL_MONDAY"),
    TUESDAY(2, "DATATOOL_TUESDAY"),
    WEDNESDAY(3, "DATATOOL_WEDNESDAY"),
    THURSDAY(4, "DATATOOL_THURSDAY"),
    FRIDAY(5, "DATATOOL_FRIDAY"),
    SATURDAY(6, "DATATOOL_SATURDAY"),
    SUNDAY(7, "DATATOOL_SUNDAY");


    private final int value;
    private final String i18nCode;

    Week(int value, String i18nCode) {
        this.value = value;
        this.i18nCode = i18nCode;
    }

    public int value() {
        return value;
    }

    public String i18nCode() {
        return i18nCode;
    }

    public static String getI18nCodeByValue(int value) {
        for (Week week : Week.values()) {
            if (week.value == value) {
                return week.i18nCode;
            }
        }
        return "";
    }
}