/*
 * 文 件 名:  TableStateEnum.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/12/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.overview;

/**
 * 表状态枚举类
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/12/15]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public enum TableStateEnum {
    USED("used", "DATATOOL_USED", Boolean.TRUE),
    UNUSED("unused", "DATATOOL_UNUSED", Boolean.FALSE),
    UNKNOWN("unknown", "DATATOOL_NA", Boolean.TRUE);

    private final String value;

    private final String i18nCode;

    private final Boolean booleanValue;

    TableStateEnum(String value, String i18nCode, Boolean booleanValue) {
        this.value = value;
        this.i18nCode = i18nCode;
        this.booleanValue = booleanValue;
    }

    public String value() {
        return value;
    }

    public String i18nCode() {
        return i18nCode;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public static String getI18nCodeByValue(String value) {
        if (value == null) {
            return "";
        }
        for (TableStateEnum domainClassificationColumn : TableStateEnum.values()) {
            if (domainClassificationColumn.value.equals(value)) {
                return domainClassificationColumn.i18nCode;
            }
        }
        return "";
    }
}