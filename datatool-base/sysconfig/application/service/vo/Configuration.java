/*
 * 文 件 名:  Configuration.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/9/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo;

import javax.validation.constraints.NotBlank;

/**
 * 配置信息
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/30]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class Configuration {
    @NotBlank
    private String key;
    @NotBlank
    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Configuration{" + "key='" + key + '\'' + ", value=" + value + '}';
    }
}