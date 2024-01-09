/*
 * 文 件 名:  DgcResourceTypeEnum.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/13
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.enumeration;

import java.util.Arrays;
import java.util.List;

/**
 * 华为云dgc的资产类型
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/13]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public enum DgcResourceTypeEnum {
    CONN("conn"),
    DLI("dli"),
    DLI_VAR("dli_var"),
    JOB("job"),
    SCRIPT("script"),
    JOB_SCRIPT("job_script"),
    ENV("env");

    private String type;

    DgcResourceTypeEnum(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

    public static List<String> getJsonSuffixResourceTypeList() {
        return Arrays.asList("conn", "dli", "dli_var");
    }
}
