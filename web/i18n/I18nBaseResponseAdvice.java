/*
 * 文 件 名:  I18nBaseResponseAdvice.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/22
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.i18n;

import com.huawei.smartcampus.datatool.exception.ExceptionBean;
import com.huawei.smartcampus.datatool.model.BaseResponse;
import com.huawei.smartcampus.datatool.utils.RequestContext;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import org.apache.logging.log4j.core.config.Order;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 处理响应消息，国际化返回
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/22]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@ControllerAdvice
@Order(2)
public class I18nBaseResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        BaseResponse result = new BaseResponse();
        if (body instanceof ExceptionBean) {
            ExceptionBean exceptionBean = ((ExceptionBean) body);
            result.setResCode(exceptionBean.getCode());
            String resMsg = I18nUtils.getMessage(exceptionBean.getCode(), exceptionBean.getParameters());
            result.setResMsg(StringUtils.isEmpty(resMsg) ? exceptionBean.getMessage() : resMsg);
        }
        if (body instanceof BaseResponse) {
            result.setResCode(((BaseResponse) body).getResCode());
            String resMsg = I18nUtils.getMessage(((BaseResponse) body).getResCode());
            result.setResMsg(StringUtils.isEmpty(resMsg) ? ((BaseResponse) body).getResMsg() : resMsg);
            result.setResult(((BaseResponse) body).getResult());
        }
        // 缓存响应信息用于记录审计日志
        RequestContext.set(RequestContext.RES_CODE, result.getResCode());
        RequestContext.set(RequestContext.RES_MSG, result.getResMsg());

        return result;
    }
}
