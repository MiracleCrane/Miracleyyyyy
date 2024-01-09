/*
 * 文 件 名:  AlarmLevel.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/26
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm;

/**
 * 告警级别
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/26]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public enum AlarmLevel {
    INFORMATION("Information"),
    HIGH("High");

    private final String value;

    AlarmLevel(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}