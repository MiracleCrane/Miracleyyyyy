/*
 * 文 件 名:  HistoryStatusEnum.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/19
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.enumeration;

/**
 * 导入history表中status枚举值
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/19]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public enum HistoryStatusEnum {
    BREAK("break"),
    FAILURE("failure"),
    PARTIAL_FAILURE("partial failure"),
    RUNNING("running"),
    SUCCESS("success");

    private String status;

    HistoryStatusEnum(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }
}
