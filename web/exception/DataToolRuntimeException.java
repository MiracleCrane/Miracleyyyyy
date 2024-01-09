/*
 * 文 件 名:  DataToolRuntimeException.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.0.T22
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/7
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.exception;

import com.huawei.smartcampus.datatool.i18n.I18nUtils;

/**
 * 抽象的根异常，继承自RuntimeException，其他异常必须直接或间接继承此类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/7]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class DataToolRuntimeException extends RuntimeException {
    /**
     * 存储异常的数据类
     */
    protected ExceptionBean exceptionBean = new ExceptionBean();

    public DataToolRuntimeException(String i18nCode) {
        // 根据资源文件的属性名以及当前语言环境，获取国际化信息
        super(I18nUtils.getMessageUS(i18nCode));
        exceptionBean.setCode(i18nCode);
    }

    public DataToolRuntimeException(String i18nCode, Object... params) {
        // 根据资源文件的属性名，属性值中的参数以及当前语言环境，获取国际化信息
        // params用来替换资源文件属性值中的占位符参数
        super(I18nUtils.getMessageUS(i18nCode, params));
        exceptionBean.setCode(i18nCode);
        exceptionBean.setParameters(params);
    }

    /**
     * 获取存储异常的数据
     *
     * @return 返回相应的异常码
     */
    public ExceptionBean getExceptionBean() {
        return exceptionBean;
    }

    /**
     * 获取异常码
     *
     * @return 返回相应的异常码
     */
    public String getExceptionCode() {
        return exceptionBean.getCode();
    }
}