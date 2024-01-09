/*
 * 文 件 名:  DataSizeUnit.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/12/4
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export;

/**
 * 数据量大小单位
 *
 * @author j00826364
 * @version [Core&Link 23.1, 2023/12/4]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public enum DataSizeUnit {
    FIRST(1, "DATATOOL_FIRST_UNIT"),
    SECOND(2, "DATATOOL_SECOND_UNIT"),
    THIRD(3, "DATATOOL_THIRD_UNIT"),
    FOURTH(4, "DATATOOL_FOURTH_UNIT");

    private final int value;
    private final String i18nCode;

    DataSizeUnit(int value, String i18nCode) {
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
        for (DataSizeUnit dataSizeUnit : DataSizeUnit.values()) {
            if (dataSizeUnit.value == value) {
                return dataSizeUnit.i18nCode;
            }
        }
        return null;
    }
}