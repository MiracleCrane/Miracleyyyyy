/*
 * 文 件 名:  DataToolImportErrorException.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  wWX643278
 * 修改时间： 2022/7/8
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.exception;

/**
 * 导入zip异常
 *
 * @author g00560618
 * @version [SmartCampus V100R001C00, 2022/7/8]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class DataToolImportZipException extends DataToolRuntimeException {
    /**
     * 导入zip异常
     *
     * @param exceptionCode 异常码
     */
    public DataToolImportZipException(String exceptionCode) {
        super(exceptionCode);
    }
}
