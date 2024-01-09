/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.monitor.domain.factory;

import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.DBType;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.DWGateway;
import com.huawei.smartcampus.datatool.monitor.infrastructure.dw.OpenGaussDW;
import com.huawei.smartcampus.datatool.monitor.infrastructure.dw.RdsPostgreSQLDW;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DWGateway工厂类
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/12/11]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWGatewayFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(DWGatewayFactory.class);

    private static Map<DBType, DWGateway> dwGatewayMap = new ConcurrentHashMap<>();

    static {
        dwGatewayMap.put(DBType.OPENGAUSS, new OpenGaussDW());
        dwGatewayMap.put(DBType.RDSPOSTGRESQL, new RdsPostgreSQLDW());
    }

    public static DWGateway create(DBType dbType) {
        if (!dwGatewayMap.containsKey(dbType)) {
            LOGGER.error("dbType {} not support.", dbType.value());
            return null;
        }

        return dwGatewayMap.get(dbType);
    }
}