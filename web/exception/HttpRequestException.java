/*
 * 文 件 名:  HttpRequestException.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.0.T22
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/8
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.exception;

/**
 * HTTP请求异常。
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/8]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class HttpRequestException extends DataToolRuntimeException {
    private final int httpStatus;

    /**
     * 含参构造方法
     *
     * @param code 错误码
     * @param httpStatus httpStatus
     */
    public HttpRequestException(String code, Integer httpStatus) {
        super(code, httpStatus);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}