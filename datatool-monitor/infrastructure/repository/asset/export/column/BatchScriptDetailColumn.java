/*
 * 文 件 名:  BatchScriptDetailColumn.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/12/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column;

/**
 * 批处理脚本明细列枚举类
 *
 * @author j00826364
 * @version [Core&Link 23.1, 2023/12/1]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public enum BatchScriptDetailColumn {
    DIR("dir", "DATATOOL_DIR_NAME"),
    NAME("name", "DATATOOL_SCRIPT_NAME"),
    ORIGIN("origin", "DATATOOL_ORIGIN"),
    BUNDLED_JOBS("bundledJobs", "DATATOOL_BUNDLED_JOB");

    private final String value;
    private final String i18nCode;

    BatchScriptDetailColumn(String value, String i18nCode) {
        this.value = value;
        this.i18nCode = i18nCode;
    }

    public String value() {
        return value;
    }

    public String i18nCode() {
        return i18nCode;
    }
}