/*
 * 文 件 名:  FileSuffixEnum.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/14
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.enumeration;

import java.util.Arrays;
import java.util.List;

/**
 * 文件后缀枚举类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/14]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public enum FileSuffixEnum {
    STREAM(".stream"),
    ENV(".env"),
    SCRIPT(".script"),
    JOB(".job"),
    CONN(".conn"),
    CIPHER(".cipher"),
    JSON(".json");

    private String suffix;

    FileSuffixEnum(String suffix) {
        this.suffix = suffix;
    }

    public String suffix() {
        return suffix;
    }

    public static List<String> getJobScriptSuffixList() {
        return Arrays.asList(".job", ".script");
    }
}
