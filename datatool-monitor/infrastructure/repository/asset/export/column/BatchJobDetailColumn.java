/*
 * 文 件 名:  BatchJobDetailColumn.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/22
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column;

/**
 * 批处理作业明细列枚举类
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/22]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public enum BatchJobDetailColumn {
    DIR("dir", "DATATOOL_DIR_NAME"),
    NAME("name", "DATATOOL_JOB_NAME"),
    ORIGIN("origin", "DATATOOL_ORIGIN"),
    BUNDLED_SCRIPT("bundledScript", "DATATOOL_BUNDLED_SCRIPT"),
    SCHEDULE_TYPE("scheduleType", "DATATOOL_SCHEDULE_TYPE"),
    PERIOD_UNIT("periodUnit", "DATATOOL_PERIOD_UNIT"),
    PERIOD_INTERVAL("periodInterval", "DATATOOL_PERIOD_INTERVAL"),
    SCHEDULE_TIME_PER_DAY("scheduleTimePerDay", "DATATOOL_SCHEDULE_TIME_PER_DAY"),
    SELF_DEPENDENT("selfDependent", "DATATOOL_SELF_DEPENDENT"),
    DEPEND_JOBS("dependJobs", "DATATOOL_DEPEND_JOBS"),
    RUNNING("running", "DATATOOL_RUNNING");

    private final String value;
    private final String i18nCode;

    BatchJobDetailColumn(String value, String i18nCode) {
        this.value = value;
        this.i18nCode = i18nCode;
    }

    public String value() {
        return value;
    }

    public String i18nCode() {
        return i18nCode;
    }

    public static BatchJobDetailColumn getBatchJobDetailColumnByValue(String value) {
        if (value == null) {
            return null;
        }
        for (BatchJobDetailColumn batchJobDetailColumn : BatchJobDetailColumn.values()) {
            if (batchJobDetailColumn.value.equals(value)) {
                return batchJobDetailColumn;
            }
        }
        return null;
    }
}