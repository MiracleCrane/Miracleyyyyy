/*
 * 文 件 名:  MqsConfig.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2021/8/31
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.mqs.config;

import java.io.Serializable;

/**
 * MQS连接配置
 * 连接MQS的信息
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2021/8/31]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class MqsConfig implements Serializable {
    private static final long serialVersionUID = -7799918501161619958L;

    // 服务器地址
    private String namesrvUrls;

    // 客户端ID
    private String appId;

    // 客户端密钥
    private String appSecret;

    // 设置Topic Name
    private String topic;

    public String getNamesrvUrls() {
        return namesrvUrls;
    }

    public void setNamesrvUrls(String namesrvUrls) {
        this.namesrvUrls = namesrvUrls;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
