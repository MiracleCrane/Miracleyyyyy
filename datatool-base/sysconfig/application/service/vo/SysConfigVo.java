/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo;

/**
 * 系统配置
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/31]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class SysConfigVo {
    private String name;
    private String key;
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public SysConfigVo(String name, String key, Object value) {
        this.name = name;
        this.key = key;
        this.value = value;
    }
}