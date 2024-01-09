/*
 * 文 件 名:  DWConnInfo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.connector;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库连接信息
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/23]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWConnInfo implements Cloneable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DWConnInfo.class);

    private String connId;

    private String connName;

    private String database;

    private String type;

    private String host;

    private String port;

    private String user;

    private String encryptedPwd;

    /**
     * 脱敏，返回一个不包含密码的新对象
     *
     * @return DWConnInfo
     */
    public DWConnInfo desensitization() {
        DWConnInfo secure = this.clone();
        secure.setEncryptedPwd(null);
        return secure;
    }

    @Override
    public DWConnInfo clone() {
        try {
            return (DWConnInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            // 实际不会发生这个异常,因为已经实现了接口Cloneable
            LOGGER.error("clone error.");
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_ERROR);
        }
    }

    public boolean sameWith(DWConnInfo other) {
        if (this == other) {
            return true;
        }

        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("my conninfo is： {}.", this);
            LOGGER.debug("other conninfo is {}.", other);
        }

        boolean connIdEquals = StringUtils.isEquals(this.getConnId(), other.getConnId());
        boolean databaseEquals = StringUtils.isEquals(this.getDatabase(), other.getDatabase());
        boolean hostEquals = StringUtils.isEquals(this.getHost(), other.getHost());
        boolean portEquals = StringUtils.isEquals(this.getPort(), other.getPort());
        boolean userEquals = StringUtils.isEquals(this.getUser(), other.getUser());
        boolean pwdEquals = StringUtils.isEquals(this.getEncryptedPwd(), other.getEncryptedPwd());
        boolean typeEquals = StringUtils.isEquals(this.getType(), other.getType());
        boolean nameEquals = StringUtils.isEquals(this.getConnName(), other.getConnName());
        return connIdEquals && databaseEquals && hostEquals && portEquals && userEquals && pwdEquals && typeEquals
                && nameEquals;
    }

    @Override
    public String toString() {
        return "DWConnInfo{" + "connId='" + connId + '\'' + ", connName='" + connName + '\'' + ", database='" + database
                + '\'' + ", type='" + type + '\'' + ", host='" + host + '\'' + ", port='" + port + '\'' + ", user='"
                + user + '\'' + '}';
    }

    public String getConnId() {
        return connId;
    }

    public void setConnId(String connId) {
        this.connId = connId;
    }

    public String getConnName() {
        return connName;
    }

    public void setConnName(String connName) {
        this.connName = connName;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEncryptedPwd() {
        return encryptedPwd;
    }

    public void setEncryptedPwd(String encryptedPwd) {
        this.encryptedPwd = encryptedPwd;
    }
}