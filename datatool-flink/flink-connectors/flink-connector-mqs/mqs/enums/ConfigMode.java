/*
 * 文 件 名:  ConfigMode.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2021/10/23
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.mqs.enums;

/**
 * 参数配置方式
 * 分为平铺（tile）、fieldMapping、parameter和全映射四种参数配置方式
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2021/10/23]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public enum ConfigMode {
    TILE,
    FIELD_MAPPINGS,
    PARAMETER,
    TOTAL_MAPPINGS;
}
