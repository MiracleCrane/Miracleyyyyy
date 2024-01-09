/*
 * 文 件 名:  AssetListInfoColumn.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/25
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column;

/**
 * 资产列表信息列枚举类
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/25]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public enum AssetListInfoColumn {
    DATA_CONN("dataConn", "DATATOOL_DATA_CONN"),
    DATABASE("database", "DATATOOL_DATABASE"),
    EXPORT_TIME("exportTime", "DATATOOL_EXPORT_TIME");

    private final String value;
    private final String i18nCode;

    AssetListInfoColumn(String value, String i18nCode) {
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
        for (AssetListInfoColumn assetListInfoColumn : AssetListInfoColumn.values()) {
            if (assetListInfoColumn.value.equals(value)) {
                return assetListInfoColumn.i18nCode;
            }
        }
        return "";
    }
}