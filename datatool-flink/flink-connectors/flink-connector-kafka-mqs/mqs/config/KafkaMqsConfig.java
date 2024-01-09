/*
 * 文 件 名:  KafkaMqsConfig.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  dWX1154687
 * 修改时间： 2022/6/8
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.kafka.mqs.config;

import java.io.Serializable;

/**
 * kafka连接配置
 * 连接KAFKAMQS的信息
 *
 * @author dWX1154687
 * @version [SmartCampus V100R001C00, 2022/6/8]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class KafkaMqsConfig implements Serializable {
    private static final long serialVersionUID = -7799918501161619958L;

    private String services;

    private String groupId;

    private String latest;

    private String format;

    private String sslTruststoreLocationConfig;

    private String sslTruststorePasswordConfig;

    private String securityProtocolConfig;

    private String saslMechanism;

    private String appId;

    private String appSecret;

    private String startupMode;

    private String topic;

    private String parseErrors;

    private String algorithm;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getStartupMode() {
        return startupMode;
    }

    public void setStartupMode(String startupMode) {
        this.startupMode = startupMode;
    }

    public String getParseErrors() {
        return parseErrors;
    }

    public void setParseErrors(String parseErrors) {
        this.parseErrors = parseErrors;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSslTruststoreLocationConfig() {
        return sslTruststoreLocationConfig;
    }

    public void setSslTruststoreLocationConfig(String sslTruststoreLocationConfig) {
        this.sslTruststoreLocationConfig = sslTruststoreLocationConfig;
    }

    public String getSslTruststorePasswordConfig() {
        return sslTruststorePasswordConfig;
    }

    public void setSslTruststorePasswordConfig(String sslTruststorePasswordConfig) {
        this.sslTruststorePasswordConfig = sslTruststorePasswordConfig;
    }

    public String getSecurityProtocolConfig() {
        return securityProtocolConfig;
    }

    public void setSecurityProtocolConfig(String securityProtocolConfig) {
        this.securityProtocolConfig = securityProtocolConfig;
    }

    public String getSaslMechanism() {
        return saslMechanism;
    }

    public void setSaslMechanism(String saslMechanism) {
        this.saslMechanism = saslMechanism;
    }
}
