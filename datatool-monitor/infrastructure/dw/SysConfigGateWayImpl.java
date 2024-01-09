/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.dw;

import com.huawei.smartcampus.datatool.entity.DtConnectionEntity;
import com.huawei.smartcampus.datatool.entity.SystemConfigurationEntity;
import com.huawei.smartcampus.datatool.enums.OverviewDbKeysEnum;
import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnInfo;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.SysConfigGateWay;
import com.huawei.smartcampus.datatool.repository.DtConnectionRepository;
import com.huawei.smartcampus.datatool.repository.DtSystemConfigurationRepository;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 数据库概览实现
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/24]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Component
public class SysConfigGateWayImpl implements SysConfigGateWay {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysConfigGateWayImpl.class);

    private DtConnectionRepository dtConnectionRepository;

    private DtSystemConfigurationRepository sysRepository;

    @Autowired
    public SysConfigGateWayImpl(DtConnectionRepository dtConnectionRepository,
            DtSystemConfigurationRepository sysRepository) {
        this.dtConnectionRepository = dtConnectionRepository;
        this.sysRepository = sysRepository;
    }

    @Override
    public DtConnectionEntity getConnInfo(String id) {
        return dtConnectionRepository.findDtConnectionEntityById(id);
    }

    @Override
    public Object getConfig(String name, String key) {
        Optional<SystemConfigurationEntity> optional = sysRepository.findByNameAndKey(name, key);
        if (!optional.isPresent()) {
            return null;
        }
        JSONObject config = JSONObject.parseObject(optional.get().getValue());
        return config.get("val");
    }

    @Override
    public <T> T getConfig(String name, String key, Class<T> clazz) {
        Optional<SystemConfigurationEntity> optional = sysRepository.findByNameAndKey(name, key);
        if (!optional.isPresent()) {
            return null;
        }
        JSONObject config = JSONObject.parseObject(optional.get().getValue());
        try {
            return config.getObject("val", clazz);
        } catch (JSONException ex) {
            LOGGER.error("Get system config fail", ex);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONFIG_TYPE_ERROR, key, clazz);
        }
    }

    @Override
    public DWConnInfo getMonitorDBInfo() {
        DWConnInfo connInfo = new DWConnInfo();
        String connId = getConfig(SysConfigNamesEnum.OVERVIEW_DB.value(), OverviewDbKeysEnum.CONNID.value(),
                String.class);
        String database = getConfig(SysConfigNamesEnum.OVERVIEW_DB.value(), OverviewDbKeysEnum.DATABASE.value(),
                String.class);
        connInfo.setConnId(connId);
        connInfo.setDatabase(database);

        DtConnectionEntity dtConnectionEntity = dtConnectionRepository.findDtConnectionEntityById(connInfo.getConnId());
        if (dtConnectionEntity != null) {
            connInfo.setConnName(dtConnectionEntity.getName());
            connInfo.setHost(dtConnectionEntity.getHost());
            connInfo.setPort(String.valueOf(dtConnectionEntity.getPort()));
            connInfo.setUser(dtConnectionEntity.getUser());
            connInfo.setEncryptedPwd(dtConnectionEntity.getPassword());
            connInfo.setType(dtConnectionEntity.getType());
        }

        return connInfo;
    }
}