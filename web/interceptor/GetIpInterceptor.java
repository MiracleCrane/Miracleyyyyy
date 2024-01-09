/*
 * 文 件 名:  GetIpInterceptor.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/13
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.interceptor;

import com.huawei.hicampus.campuscommon.common.util.WebUtils;
import com.huawei.smartcampus.datatool.utils.RequestContext;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ip拦截器
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/13]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Component
public class GetIpInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        RequestContext.set(RequestContext.REQUEST_IP, WebUtils.getIPAddress(request));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        RequestContext.clear();
    }
}