/*
 * 文 件 名:  AssetOriginEnum.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.overview;

/**
 * 分组类型
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public enum AssetOriginEnum {
    BASELINE("baseline"),
    CUSTOM("custom");

    private String value;

    AssetOriginEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
