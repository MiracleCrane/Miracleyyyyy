/*
 * 文 件 名:  User.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/10/27
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.vo;

/**
 * 通用用户
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/10/27]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class CommonUser {
    private String username;
    private String userId;
    private String userType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}