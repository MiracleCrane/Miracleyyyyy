/*
 * 文 件 名:  TableDataOverviewColumn.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column;

/**
 * 表数据总览列枚举类
 *
 * @author j00826364
 * @version [Core&Link 23.1, 2023/11/28]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public enum TableDataOverviewColumn {
    TOTAL_TABLE_NUM("totalTableNum", "DATATOOL_TOTAL_TABLE_NUM"),
    USED_TABLE_NUM("usedTableNum", "DATATOOL_USED_TABLE_NUM"),
    UTILIZATION_RATE("utilizationRate", "DATATOOL_UTILIZATION_RATE"),
    TOTAL_DATA_SIZE("totalDataSize", "DATATOOL_TOTAL_DATA_SIZE");

    private final String value;
    private final String i18nCode;

    TableDataOverviewColumn(String value, String i18nCode) {
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
        for (TableDataOverviewColumn tableDataOverviewColumn : TableDataOverviewColumn.values()) {
            if (tableDataOverviewColumn.value.equals(value)) {
                return tableDataOverviewColumn.i18nCode;
            }
        }
        return "";
    }
}