/*
 * 文 件 名:  ConfigMode.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  dWX1154687
 * 修改时间： 2022/6/8
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.kafka.mqs.enums;

/**
 * 配置项方式
 * 分为平铺（tile）和 parameter 两种种参数配置方式
 *
 * @author dWX1154687
 * @version [SmartCampus V100R001C00, 2022/6/8]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public enum ConfigMode {
    TILE,
    PARAMETER;

    /**
     * 判断是否为TILE配置方式
     *
     * @return 判断结果
     */
    public boolean isTile() {
        return this == TILE;
    }

    /**
     * 判断是否为parameter配置方式
     *
     * @return 判断结果
     */
    public boolean isParameter() {
        return this == PARAMETER;
    }
}
