/*
 * 文 件 名:  ConnectionTypeEnum.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/10/21
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.enumeration;

/**
 * 数据连接类型
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/10/21]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public enum ConnectionTypeEnum {
    OPENGAUSS("opengauss"),
    RDS("rdspostgresql");

    private String type;

    ConnectionTypeEnum(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
