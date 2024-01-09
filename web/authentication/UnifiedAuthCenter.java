/*
 * 文 件 名:  UnifiedAuthCenter.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/7/31
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.authentication;

import com.huawei.smartcampus.datatool.config.ApplicationConfig;
import com.huawei.smartcampus.datatool.exception.AccessTokenAuthException;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.resttemplate.provider.CommonRestTemplateProvider;
import com.huawei.smartcampus.datatool.utils.CommonUtil;
import com.huawei.smartcampus.datatool.utils.NormalizerUtil;
import com.huawei.smartcampus.datatool.utils.StringUtils;
import com.huawei.smartcampus.datatool.vo.CommonUser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 统一认证中心服务
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/7/31]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Component
public class UnifiedAuthCenter implements AuthCenterGateway {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnifiedAuthCenter.class);
    private static final String ACCESS_TOKEN_NAME = "access-token";

    @Autowired
    private ApplicationConfig applicationConfig;

    @Override
    public CommonUser getUser(String token) {
        HttpHeaders headers = new HttpHeaders();
        // 标准化access-token防止crlf攻击
        String accessToken = NormalizerUtil.normalizeForString(CommonUtil.replaceCRLF(token));
        headers.add(ACCESS_TOKEN_NAME, accessToken);
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = CommonRestTemplateProvider.getRestTemplate();
        String authenticationURI = applicationConfig.getRemoteUrl() + applicationConfig.getGetUserUrl();
        try {
            ResponseEntity<String> response = restTemplate.exchange(authenticationURI, HttpMethod.GET, httpEntity,
                    String.class);
            JSONObject responseJson = JSON.parseObject(response.getBody());
            JSONObject userInfo = null;
            if (responseJson != null && !responseJson.isEmpty()) {
                userInfo = responseJson.getJSONObject("result");
            }
            CommonUser commonUser = new CommonUser();
            if (userInfo != null && !userInfo.isEmpty()) {
                // 通过统一认证中心获取用户接口获取用户
                commonUser.setUsername(userInfo.getString("loginAccount"));
                commonUser.setUserId(userInfo.getString("userId"));
                commonUser.setUserType(userInfo.getString("userType"));
            }
            return commonUser;
        } catch (HttpStatusCodeException exception) {
            LOGGER.error("post for UnifiedAuthentication center failed.", exception);
            if (exception.getStatusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
                throw new AccessTokenAuthException();
            }
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_REQUEST_USER_INTERFACE_FAIL);
        } catch (Exception exception) {
            LOGGER.error("post for UnifiedAuthentication center failed.", exception);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_REQUEST_USER_INTERFACE_FAIL);
        }
    }

    @Override
    public String getToken(HttpServletRequest request) {
        String accessToken = request.getHeader(ACCESS_TOKEN_NAME);
        if (!StringUtils.isEmpty(accessToken)) {
            return accessToken;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(ACCESS_TOKEN_NAME)) {
                    return cookie.getValue();
                }
            }
        }
        return "";
    }
}