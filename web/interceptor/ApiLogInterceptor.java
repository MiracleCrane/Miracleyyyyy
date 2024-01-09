/*
 * 文 件 名:  ApiLogInterceptor.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <接口日志Handler，确保所有接口请求日志都能记录到>
 * 修 改 人:  l30006786
 * 修改时间： 2021/7/30
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.interceptor;

import com.huawei.smartcampus.datatool.utils.RequestContext;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接口日志Handler
 *
 * @author l30006786
 * @version [SmartCampus V100R001C00, 2021/7/30]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Component
public class ApiLogInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger("Interface");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        RequestContext.set(RequestContext.REQUEST_TIME, requestTime);
        RequestContext.set(RequestContext.REQUEST_START_TIME, System.currentTimeMillis());
        RequestContext.set(RequestContext.REQUEST_ID, String.valueOf(getMicTime()));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        int responseStatus = response.getStatus();
        String currentUsername = RequestContext.getUserName();
        String currentUserType = (String) RequestContext.get(RequestContext.USER_TYPE_FIELD);
        String requestId = (String) RequestContext.get(RequestContext.REQUEST_ID);
        String requestIp = (String) RequestContext.get(RequestContext.REQUEST_IP);
        String requestTime = (String) RequestContext.get(RequestContext.REQUEST_TIME);
        if (responseStatus == HttpStatus.OK.value()) {
            long takeTime = System.currentTimeMillis() - (long) RequestContext.get(RequestContext.REQUEST_START_TIME);
            LOGGER.info(
                    "request id is :{}, request user type :{} , request user :{}, request ip :{}, request time :{},"
                            + " request path :{}, http method :{}, spends :{}ms, response_status :{}.",
                    requestId, currentUserType, currentUsername, requestIp, requestTime, getRequestUrl(request),
                    request.getMethod(), takeTime, responseStatus);
        } else {
            LOGGER.error(
                    "request id is :{}, request user type :{} , request user :{}, request ip :{}, request time :{},"
                            + " request path :{}, http method :{}, response_status :{}.",
                    requestId, currentUserType, currentUsername, requestIp, requestTime, getRequestUrl(request),
                    request.getMethod(), responseStatus);
        }
    }

    private String getRequestUrl(HttpServletRequest request) {
        if (StringUtils.isEmpty(request.getPathInfo())) {
            return request.getContextPath() + request.getServletPath();
        }
        return request.getContextPath() + request.getServletPath() + request.getPathInfo();
    }

    private Long getMicTime() {
        // 获取mic时间，用于生成REQUEST_ID
        Long cuTime = System.currentTimeMillis() * 1000;
        Long nanoTime = System.nanoTime();
        return cuTime + (nanoTime - nanoTime / 1_000_000 * 1_000_000) / 1000;
    }
}
