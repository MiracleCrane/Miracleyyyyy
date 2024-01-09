/*
 * 文 件 名:  ExportConnContent.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/19
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.vo.conn;

/**
 * 导出数据连接，文本内容类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/19]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ExportConnContent {
    private String name;
    private String type;
    private String username;
    private String password;
    private String host;
    private int port;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}