/*
 * 文 件 名:  BusinessDatabaseGatewayImpl.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/14
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.datasource;

import com.huawei.hicampus.campuscommon.common.util.ClearSensitiveDataUtil;
import com.huawei.smartcampus.datatool.entity.DtConnectionEntity;
import com.huawei.smartcampus.datatool.enums.DataSourceDriverEnum;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.utils.DataToolCryptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

/**
 * 业务数据库连接
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/11/14]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Component
public class BusinessDatabaseGatewayImpl implements BusinessDatabaseGateway {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessDatabaseGatewayImpl.class);

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.error("Init org.postgresql.Driver class error!!!", e);
        }
    }
    @Override
    public Connection getConnection(DtConnectionEntity connectionEntity, String database) {
        DataSourceDriverEnum type = Enum.valueOf(DataSourceDriverEnum.class,
                connectionEntity.getType().toUpperCase(Locale.ROOT));
        String url = type.getUrl(connectionEntity.getHost(), connectionEntity.getPort(), database);

        String username = connectionEntity.getUser();
        String password = null;
        Connection conn = null;
        try {
            password = DataToolCryptor.decodingSecret(connectionEntity.getPassword());
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            LOGGER.error("Failed to obtain the connection based on url:{}.", url);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_DATABASE_CONNECTION_ERROR);
        } finally {
            ClearSensitiveDataUtil.clearPlainSensitiveData(password);
        }
        return conn;
    }

    @Override
    public void shutdown(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            LOGGER.error("Failed to close the connection.");
        }
    }
}