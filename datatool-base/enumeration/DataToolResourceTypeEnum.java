/*
 * 文 件 名:  DataToolResourceTypeEnum.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/13
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.enumeration;

/**
 * datatool的资产类型
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/13]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public enum DataToolResourceTypeEnum {
    CONN("conn"),
    STREAM("stream"),
    JOB("job"),
    SCRIPT("script"),
    ENV("env"),
    CIPHER("cipher");

    private String type;

    DataToolResourceTypeEnum(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
