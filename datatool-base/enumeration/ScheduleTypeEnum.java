/*
 * 文 件 名:  ScheduleTypeEnum.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/10/19
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.enumeration;

import java.util.Arrays;
import java.util.List;

/**
 * 作业调度类型枚举类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/10/19]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public enum ScheduleTypeEnum {
    CRON("cron"),
    ONCE("once");

    private String type;

    ScheduleTypeEnum(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

    public static List<String> getDgcScheduleTypeList() {
        return Arrays.asList("cron", "execute_once");
    }
}