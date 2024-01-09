/*
 * 文 件 名:  ExceptionBean.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.exception;

import com.huawei.smartcampus.datatool.utils.ArrayUtils;

import java.io.Serializable;

/**
 * Exception的数据Bean
 * 异常类内部使用，存取异常信息，特别的可以保存扩展信息
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/23]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ExceptionBean implements Serializable {
    private static final long serialVersionUID = 1240131782010793226L;
    private String code;

    private String message;

    private transient Object[] parameters = null;

    public Object[] getParameters() {
        return ArrayUtils.clone(this.parameters);
    }

    public void setParameters(Object[] parameters) {
        this.parameters = ArrayUtils.clone(parameters);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}