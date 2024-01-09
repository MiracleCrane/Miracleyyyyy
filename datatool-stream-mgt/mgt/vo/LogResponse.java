/*
 * 文 件 名:  LogResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/12
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo;

/**
 * logs接口返回响应类，单条日志
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/12]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class LogResponse {
    private String name;

    private int size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}