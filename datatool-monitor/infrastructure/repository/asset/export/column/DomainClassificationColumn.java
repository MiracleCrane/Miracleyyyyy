/*
 * 文 件 名:  DomainClassificationColumn.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column;

/**
 * 领域分类汇总列枚举类
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/28]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public enum DomainClassificationColumn {
    LAYER("layer", "DATATOOL_LAYER"),
    DOMAIN_NAME("name", "DATATOOL_DOMAIN_NAME"),
    DOMAIN_ABBREVIATION("abbreviation", "DATATOOL_DOMAIN_ABBREVIATION"),
    ORIGIN("origin", "DATATOOL_ORIGIN"),
    BASELINE_TABLE_NUM("baselineTableNum", "DATATOOL_BASELINE_TABLE_NUM"),
    CUSTOM_TABLE_NUM("customTableNum", "DATATOOL_CUSTOM_TABLE_NUM");

    private final String value;
    private final String i18nCode;

    DomainClassificationColumn(String value, String i18nCode) {
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
        for (DomainClassificationColumn domainClassificationColumn : DomainClassificationColumn.values()) {
            if (domainClassificationColumn.value.equals(value)) {
                return domainClassificationColumn.i18nCode;
            }
        }
        return "";
    }
}