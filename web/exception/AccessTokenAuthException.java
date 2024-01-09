/*
 * 文 件 名:  AccessTokenAuthException.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  yWX895180
 * 修改时间： 2021/7/9
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.exception;

import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;

/**
 * 认证失败
 *
 * @author yWX895180
 * @version [SmartCampus V100R001C00, 2021/7/9]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class AccessTokenAuthException extends DataToolRuntimeException {
    /**
     * token认证失败异常构造方法
     */
    public AccessTokenAuthException() {
        super(ExceptionCode.DATATOOL_ACCESS_TOKEN_AUTH_FAILED);
    }
}
