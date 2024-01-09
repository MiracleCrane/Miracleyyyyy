/*
 * 文 件 名:  ExportFileNameEnum.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/25
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.enumeration;

/**
 * 导出时，文件的名称
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/25]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public enum ExportFileNameEnum {
    ENV("datatool.env"),
    CONN("datatool.conn"),
    CIPHER("datatool.cipher");

    private String name;

    ExportFileNameEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
