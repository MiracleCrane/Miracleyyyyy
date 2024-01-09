/*
 * 文 件 名:  CreateEnvReq.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.vo.env;

/**
 * 环境变量创建请求入参类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/15]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class CreateEnvReq {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}