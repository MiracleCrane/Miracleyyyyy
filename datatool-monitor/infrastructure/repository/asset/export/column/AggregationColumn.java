/*
 * 文 件 名:  AggregationColumn.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/12/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column;

/**
 * 分类汇总行枚举类
 *
 * @author j00826364
 * @version [Core&Link 23.1, 2023/12/1]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public enum AggregationColumn {
    BATCH_JOB("batchJob", "DATATOOL_BATCH_JOB"),
    BATCH_SCRIPT("batchScript", "DATATOOL_BATCH_SCRIPT"),
    STREAM_JOB("streamJob", "DATATOOL_STREAM_JOB"),
    TOTAL("total", "DATATOOL_SUMMARY");

    private final String value;
    private final String i18nCode;

    AggregationColumn(String value, String i18nCode) {
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
        for (AggregationColumn aggregationColumn : AggregationColumn.values()) {
            if (aggregationColumn.value.equals(value)) {
                return aggregationColumn.i18nCode;
            }
        }
        return "";
    }
}