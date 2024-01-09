/*
 * 文 件 名:  SysConfigOverviewDbHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/9/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.sysconfig.handler;

import com.huawei.smartcampus.datatool.base.pattern.RegexValidationRule;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.Configuration;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigBody;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigVo;
import com.huawei.smartcampus.datatool.enums.OverviewDbKeysEnum;
import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.repository.DtConnectionRepository;
import com.huawei.smartcampus.datatool.utils.DataToolUtils;
import com.huawei.smartcampus.datatool.utils.ParamsValidatorUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 参数校验
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/28]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SysConfigOverviewDbHandler extends AbstractSysConfigHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysConfigOverviewDbHandler.class);

    @Autowired
    private DtConnectionRepository connectionRepository;

    @Override
    protected void doHandler(SysConfigNamesEnum name, SysConfigBody request, List<SysConfigVo> configs) {
        // 检查是否缺少必填的key
        for (OverviewDbKeysEnum keysEnum : OverviewDbKeysEnum.values()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("start to check attr:{}", keysEnum.value());
            }
            List<Configuration> keyList = request.getConfigurations().stream()
                    .filter(configuration -> keysEnum.value().equals(configuration.getKey()))
                    .collect(Collectors.toList());
            checkKey(keysEnum, keyList);
            configs.add(new SysConfigVo(name.value(), keysEnum.value(), keyList.get(0).getValue()));
        }
    }

    private void checkKey(OverviewDbKeysEnum keysEnum, List<Configuration> keyList) {
        if (keyList.isEmpty()) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_EMPTY, keysEnum.value());
        } else if (keyList.size() > 1) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_CONFIGURATION_DUPLICATE_KEY);
        }
        validateConnId(keysEnum, keyList);
        // 校验database
        validateDataBase(keysEnum, keyList);
    }

    private void validateDataBase(OverviewDbKeysEnum key, List<Configuration> configurations) {
        if (!OverviewDbKeysEnum.DATABASE.equals(key)) {
            return;
        }
        Object databaseObject = configurations.get(0).getValue();
        if (!(databaseObject instanceof String)) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_CONNECTION_DATABASE);
        }
        String database = ((String) databaseObject).trim();
        if (!ParamsValidatorUtils.isMatch(RegexValidationRule.DATABASE_NAME_PATTERN, database)) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_CONNECTION_DATABASE);
        }
        if (DataToolUtils.isBlackListDataBase(database)) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_CONNECTION_DATABASE);
        }
        configurations.get(0).setValue(database);
    }

    private void validateConnId(OverviewDbKeysEnum key, List<Configuration> configurations) {
        if (!OverviewDbKeysEnum.CONNID.equals(key)) {
            return;
        }
        Object connIdObject = configurations.get(0).getValue();
        if (!(connIdObject instanceof String)) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_ID);
        }
        String connId = ((String) connIdObject).trim();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("start to validateConnId {}", connId);
        }

        if (!ParamsValidatorUtils.isMatch(RegexValidationRule.ID_PATTERN, connId)) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_ID);
        }

        if (connectionRepository.findDtConnectionEntityById(connId) == null) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECTION_NOT_EXIST);
        }
        configurations.get(0).setValue(connId);
    }
}