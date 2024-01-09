/*
 * 文 件 名:  SysConfigNamesEnum.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/9/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.enums;

/**
 * 系统配置枚举类，目前只包括概览页面的数据库配置
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public enum SysConfigNamesEnum {
    OVERVIEW_DB("overview-db"),
    CUSTOM_FLAG("custom-flag");

    private String value;

    SysConfigNamesEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
