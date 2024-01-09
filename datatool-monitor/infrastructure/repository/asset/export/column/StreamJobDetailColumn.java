/*
 * 文 件 名:  StreamJobDetailColumn.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/12/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column;

/**
 * 流处理作业明细列枚举类
 *
 * @author j00826364
 * @version [Core&Link 23.1, 2023/12/1]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public enum StreamJobDetailColumn {
    DOMAIN("domain", "DATATOOL_DOMAIN_NAME"),
    NAME("name", "DATATOOL_JOB_NAME"),
    ORIGIN("origin", "DATATOOL_ORIGIN");
    private final String value;
    private final String i18nCode;

    StreamJobDetailColumn(String value, String i18nCode) {
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