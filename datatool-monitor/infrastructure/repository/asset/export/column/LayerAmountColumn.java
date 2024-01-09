/*
 * 文 件 名:  LayerAmountColumn.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/30
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column;

/**
 * 分层汇总列枚举类
 *
 * @author j00826364
 * @version [Core&Link 23.1, 2023/11/30]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public enum LayerAmountColumn {
    DM("dm", "DATATOOL_DM"),
    DWR("dwr", "DATATOOL_DWR"),
    DWI("dwi", "DATATOOL_DWI"),
    TOTAL("total", "DATATOOL_SUMMARY");

    private final String value;
    private final String i18nCode;

    LayerAmountColumn(String value, String i18nCode) {
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
        for (LayerAmountColumn layerAmountColumn : LayerAmountColumn.values()) {
            if (layerAmountColumn.value.equals(value)) {
                return layerAmountColumn.i18nCode;
            }
        }
        return "";
    }
}