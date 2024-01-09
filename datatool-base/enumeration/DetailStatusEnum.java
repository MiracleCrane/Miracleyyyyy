/*
 * 文 件 名:  DetailStatusEnum.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/13
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.enumeration;

/**
 * 导入detail表中status枚举值
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/13]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public enum DetailStatusEnum {
    SKIPPED("DATATOOL_SKIPPED"),
    FAILURE("DATATOOL_FAILURE"),
    SUCCESS("DATATOOL_SUCCESS");

    private String status;

    DetailStatusEnum(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }
}
