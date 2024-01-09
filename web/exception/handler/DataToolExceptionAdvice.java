/*
 * 文 件 名:  DatatoolExceptionAdvice.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2021/3/11
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.exception.handler;

import com.huawei.hicampus.campuscommon.common.exception.CampusException;
import com.huawei.smartcampus.datatool.exception.AccessTokenAuthException;
import com.huawei.smartcampus.datatool.exception.DataToolImportZipException;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.ExceptionBean;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.model.BaseResponse;

import org.apache.logging.log4j.core.config.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 数据加工异常统一处理
 *
 * @author z00569896
 * @version [SmartCampus V100R001C00, 2021/3/11]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@ControllerAdvice
@Order(1)
public class DataToolExceptionAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataToolExceptionAdvice.class);

    /**
     * 获取token失败/token认证失败异常处理
     *
     * @param exception 异常信息
     * @return ExceptionBean
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = AccessTokenAuthException.class)
    public ExceptionBean getTokenFailExceptionHandler(AccessTokenAuthException exception) {
        LOGGER.error("get token user, exception encountered: ", exception);
        return exception.getExceptionBean();
    }

    /**
     * 校验错误拦截处理
     * 处理CampusException异常。
     *
     * @param exception 错误信息集合
     * @return BaseResponse 返回错误信息
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = CampusException.class)
    public BaseResponse campusExceptionHandler(CampusException exception) {
        LOGGER.error("CampusException encountered: ", exception);
        return BaseResponse.newError(exception.getExceptionCode(), exception.getMessage());
    }

    /**
     * 参数不存在异常处理
     *
     * @param exception 异常信息
     * @return BaseResponse
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BindException.class)
    public BaseResponse methodArgumentNotValidExceptionHandler(BindException exception) {
        LOGGER.error("MethodArgumentNotValidException encountered: ", exception);
        return BaseResponse.newError("DATATOOL_METHOD_ARGUMENT_NOT_VALID",
                exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 参数非法异常处理
     *
     * @param exception 异常信息
     * @return BaseResponse
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public BaseResponse illegalArgumentExceptionHandler(IllegalArgumentException exception) {
        LOGGER.error("IllegalArgumentException encountered: ", exception);
        return BaseResponse.newError("DATATOOL_ILLEGAL_ARGUMENT", exception.getMessage());
    }

    /**
     * DataToolRuntime异常处理
     *
     * @param exception 异常信息
     * @return ExceptionBean
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = DataToolRuntimeException.class)
    public ExceptionBean dataToolRuntimeExceptionHandler(DataToolRuntimeException exception) {
        LOGGER.error("DataTool exception encountered: ", exception);
        return exception.getExceptionBean();
    }

    /**
     * 抛出异常处理
     *
     * @param exception 异常信息
     * @return BaseResponse
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Throwable.class)
    public BaseResponse throwableHandler(Throwable exception) {
        LOGGER.error("Throwable exception encountered: ", exception);
        return BaseResponse.newError(ExceptionCode.DATATOOL_SYSTEM_ERROR);
    }

    /**
     * 校验错误拦截处理
     * 处理MaxUploadSizeExceededException异常。
     *
     * @param exception 错误信息集合
     * @return BaseResponse 返回错误信息
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public BaseResponse fileUploadExceptionHandler(MaxUploadSizeExceededException exception) {
        LOGGER.error("zip content over size.", exception.getCause());
        return BaseResponse.newError(ExceptionCode.DATATOOL_ZIP_FILE_SIZE_EXCEED_LIMIT);
    }

    /**
     * 处理导入zip异常
     *
     * @param exception 错误信息集合
     * @return BaseResponse 返回错误信息
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = DataToolImportZipException.class)
    public BaseResponse fileUploadExceptionHandler(DataToolImportZipException exception) {
        LOGGER.error("import zip error.", exception);
        return BaseResponse.newError(exception.getExceptionCode());
    }
}
