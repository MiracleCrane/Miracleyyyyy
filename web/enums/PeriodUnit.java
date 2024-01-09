/*
 * 文 件 名:  PeriodUnit.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.enums;

/**
 * 调度周期单位枚举类
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public enum PeriodUnit {
    MINUTE("minute", "DATATOOL_MINUTE"),
    HOUR("hour", "DATATOOL_HOUR"),
    DAY("day", "DATATOOL_DAY"),
    WEEK("week", "DATATOOL_WEEK"),
    MONTH("month", "DATATOOL_MONTH");

    private final String value;
    private final String i18nCode;

    PeriodUnit(String value, String i18nCode) {
        this.value = value;
        this.i18nCode = i18nCode;
    }

    public String value() {
        return value;
    }

    public String i18nCode() {
        return i18nCode;
    }

    public static String getI18nCodeByValue(String value) {
        if (value == null) {
            return "";
        }
        for (PeriodUnit periodUnit : PeriodUnit.values()) {
            if (periodUnit.value.equals(value)) {
                return periodUnit.i18nCode;
            }
        }
        return "";
    }

    public static PeriodUnit getPeriodUnitByValue(String value) {
        if (value == null) {
            return null;
        }
        for (PeriodUnit periodUnit : PeriodUnit.values()) {
            if (periodUnit.value.equals(value)) {
                return periodUnit;
            }
        }
        return null;
    }
}