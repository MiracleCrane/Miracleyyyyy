/*
 * 文 件 名:  ScheduleType.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.enums;

/**
 * 调度类型枚举类
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public enum ScheduleType {
    CRON("cron", "DATATOOL_CRON"),
    ONCE("once", "DATATOOL_ONCE");

    private final String value;
    private final String i18nCode;

    ScheduleType(String value, String i18nCode) {
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
        for (ScheduleType scheduleType : ScheduleType.values()) {
            if (scheduleType.value.equals(value)) {
                return scheduleType.i18nCode;
            }
        }
        return "";
    }
}