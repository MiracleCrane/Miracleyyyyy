/*
 * 文 件 名:  AuthCenterGateway.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/7/31
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.authentication;

import com.huawei.smartcampus.datatool.vo.CommonUser;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证中心接口
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/7/31]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public interface AuthCenterGateway {
    /**
     * 获取用户
     *
     * @param token 用户token
     * @return 通用用户
     */
    CommonUser getUser(String token);

    /**
     * 从请求中获取token
     *
     * @param request 请求
     * @return token
     */
    String getToken(HttpServletRequest request);
}