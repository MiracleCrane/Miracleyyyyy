/*
 * 文 件 名:  TableDetailColumn.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/30
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column;

/**
 * 表明细列枚举类
 *
 * @author j00826364
 * @version [Core&Link 23.1, 2023/11/30]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public enum TableDetailColumn {
    MODEL_LAYERING("modelLayering", "DATATOOL_MODEL_LAYERING"),
    DOMAIN("domain", "DATATOOL_DOMAIN_ABBREVIATION"),
    SCHEMA("schema", "DATATOOL_SCHEMA"),
    SOURCE("source", "DATATOOL_ORIGIN"),
    USED("used", "DATATOOL_IF_USED"),
    TABLE_NAME("tableName", "DATATOOL_TABLE_NAME"),
    TABLE_NAME_CN("tableNameCN", "DATATOOL_TABLE_NAME_CN"),
    FIELD("field", "DATATOOL_FIELD"),
    FIELD_DESCRIPTION("fieldDescription", "DATATOOL_FIELD_DESCRIPTION"),
    FIELD_TYPE("fieldType", "DATATOOL_FIELD_TYPE"),
    IS_PART_KEY("isPartKey", "DATATOOL_IS_PART_KEY"),
    IS_PRIMARY_KEY("isPrimaryKey", "DATATOOL_IS_PRIMARY_KEY");

    private final String value;
    private final String i18nCode;

    TableDetailColumn(String value, String i18nCode) {
        this.value = value;
        this.i18nCode = i18nCode;
    }

    public String value() {
        return value;
    }

    public String i18nCode() {
        return i18nCode;
    }

    public static TableDetailColumn getTableDetailColumnByValue(String value) {
        if (value == null) {
            return null;
        }
        for (TableDetailColumn tableDetailColumn : TableDetailColumn.values()) {
            if (tableDetailColumn.value.equals(value)) {
                return tableDetailColumn;
            }
        }
        return null;
    }
}