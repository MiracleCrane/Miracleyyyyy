/*
 * 文 件 名:  JobType.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.overview;

/**
 * 分组类型
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public enum GroupStatisticsEnum {
    ALL("all", "DATATOOL_ALL"),
    BASELINE("baseline", "DATATOOL_BASELINE"),
    CUSTOM("custom", "DATATOOL_CUSTOM");

    private final String value;
    private final String i18nCode;

    GroupStatisticsEnum(String value, String i18nCode) {
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
        for (GroupStatisticsEnum groupStatisticsEnum : GroupStatisticsEnum.values()) {
            if (groupStatisticsEnum.value.equals(value)) {
                return groupStatisticsEnum.i18nCode;
            }
        }
        return "";
    }
}
