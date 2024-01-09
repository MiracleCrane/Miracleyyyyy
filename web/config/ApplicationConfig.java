/*
 * 文 件 名:  ApplicationConfig.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  22.1.0
 * 描    述:  <描述>
 * 修 改 人:  l30006786
 * 修改时间： 2022/9/5
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 应用配置类
 *
 * @author l30006786
 * @version [22.2.0, 2022/9/5]
 * @see [相关类/方法]
 * @since [22.2.0]
 */
@Component
@Validated
@ConfigurationProperties(prefix = "auth")
public class ApplicationConfig {
    @Value("${authcenter.host}")
    @NotEmpty
    @Pattern(regexp = "^http[s]?://[\\w.:/-]{1,127}$", message = "error format for authcenter.host")
    private String remoteUrl;

    @Value("${authcenter.login.url}")
    @NotEmpty
    @Pattern(regexp = "^[/][/\\w-.]{1,127}$")
    private String tokenUrl;

    @Value("${client.id}")
    @NotEmpty
    private String clientId;

    @Value("${client.secret}")
    @NotEmpty
    private String clientSecret;

    @Value("${authcenter.user.url}")
    @NotEmpty
    @Pattern(regexp = "^[/][/\\w-.]{1,127}$")
    private String getUserUrl;

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGetUserUrl() {
        return getUserUrl;
    }

    public void setGetUserUrl(String getUserUrl) {
        this.getUserUrl = getUserUrl;
    }
}
