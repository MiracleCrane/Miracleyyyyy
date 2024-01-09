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
import com.huawei.smartcampus.datatool.base.properties.SystemParamsConfig;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.Configuration;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigBody;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigVo;
import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;
import com.huawei.smartcampus.datatool.enums.SysConfigSuffixesKeysEnum;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.utils.ParamsValidatorUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SysConfigSuffixesHandler extends AbstractSysConfigHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysConfigSuffixesHandler.class);

    @Override
    protected void doHandler(SysConfigNamesEnum name, SysConfigBody request, List<SysConfigVo> configs) {
        for (SysConfigSuffixesKeysEnum keysEnum : SysConfigSuffixesKeysEnum.values()) {
            List<Configuration> keyList = request.getConfigurations().stream()
                    .filter(configuration -> keysEnum.value().equals(configuration.getKey()))
                    .collect(Collectors.toList());
            checkKey(keysEnum, keyList);
            // check的时候已经检查过类型了
            configs.add(new SysConfigVo(name.value(), keysEnum.value(),
                    ((List<String>) keyList.get(0).getValue()).stream().distinct().collect(Collectors.toList())));
        }
    }

    private void checkKey(SysConfigSuffixesKeysEnum keysEnum, List<Configuration> keyList) {
        if (keyList.isEmpty()) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_EMPTY, keysEnum.value());
        } else if (keyList.size() > 1) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_CONFIGURATION_DUPLICATE_KEY);
        }
        Object suffixes = keyList.get(0).getValue();
        if (!(suffixes instanceof List) || ((List<?>) suffixes).size() > 10) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_COMMON_CONFIGURATION_SUFFIXES,
                    SystemParamsConfig.maxCustomSuffixes());
        }
        List<?> suffixesList = (List<?>) suffixes;
        for (Object suffix : suffixesList) {
            if (!(suffix instanceof String) || !ParamsValidatorUtils
                    .isMatch(RegexValidationRule.COMMON_CONFIG_CUSTOM_SUFFIXES_PATTERN, (String) suffix)) {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_COMMON_CONFIGURATION_SUFFIX_ID);
            }
        }
    }
}