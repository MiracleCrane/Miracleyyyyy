/*
 * 文 件 名:  VerifyTokenInterceptor.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2021/2/9
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.interceptor;

import com.huawei.smartcampus.datatool.authentication.AuthCenterGateway;
import com.huawei.smartcampus.datatool.utils.RequestContext;
import com.huawei.smartcampus.datatool.utils.StringUtils;
import com.huawei.smartcampus.datatool.vo.CommonUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 校验token拦截器
 *
 * @author z00569896
 * @version [SmartCampus V100R001C00, 2021/2/9]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Component
public class GetUserInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetUserInterceptor.class);
    @Autowired
    private AuthCenterGateway authCenterGateway;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("start to get current user, current url is {}",
                    request.getContextPath() + request.getServletPath() + request.getPathInfo());
        }
        String accessToken = authCenterGateway.getToken(request);
        if (!StringUtils.isEmpty(accessToken)) {
            CommonUser commonUser = authCenterGateway.getUser(accessToken);
            RequestContext.set(RequestContext.USER_NAME_FIELD, commonUser.getUsername());
            RequestContext.set(RequestContext.USER_ID_FIELD, commonUser.getUserId());
            RequestContext.set(RequestContext.USER_TYPE_FIELD, commonUser.getUserType());
        }
        return true;
    }
}
