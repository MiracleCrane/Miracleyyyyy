/*
 * 文 件 名:  BaseResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/17
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.model;

import com.huawei.smartcampus.datatool.constant.Constant;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 原生服务响应
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class BaseResponse {
    private String resCode;

    private String resMsg;

    private Object result;

    public BaseResponse() {
    }

    public BaseResponse(String resCode, String resMsg, Object result) {
        this.resCode = resCode;
        this.resMsg = resMsg;
        this.result = result;
    }

    private BaseResponse(String resCode, String resMsg) {
        this(resCode, resMsg, null);
    }

    public BaseResponse(String resCode, Object result) {
        this(resCode, null, result);
    }

    private BaseResponse(String resCode) {
        this(resCode, null, null);
    }

    /**
     * ok response
     *
     * @return successful response
     */
    public static BaseResponse newOk() {
        return new BaseResponse(Constant.RESULT_CODE_SUCCESS);
    }

    /**
     * ok response with JSON object
     *
     * @param resJSON result JSON object
     * @return successful response with JSON object
     */
    public static BaseResponse newOk(Object resJSON) {
        return new BaseResponse(Constant.RESULT_CODE_SUCCESS, resJSON);
    }

    /**
     * error response
     *
     * @param resCode result code
     * @param resMsg  result message
     * @return error response with error code and error message
     */
    public static BaseResponse newError(String resCode, String resMsg) {
        return new BaseResponse(resCode, resMsg);
    }

    public static BaseResponse newError(String resCode) {
        return new BaseResponse(resCode);
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}