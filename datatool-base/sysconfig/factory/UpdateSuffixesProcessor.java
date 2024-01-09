/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.base.sysconfig.factory;

import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigBody;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigVo;
import com.huawei.smartcampus.datatool.base.sysconfig.handler.SysConfigAddAuditHandler;
import com.huawei.smartcampus.datatool.base.sysconfig.handler.SysConfigSuffixesHandler;
import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.PostConstruct;

/**
 * 通用配置责任链
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/11/1]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Component
@DependsOn("springContextHelper")
public class UpdateSuffixesProcessor implements ConfigProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSuffixesProcessor.class);

    private SysConfigAddAuditHandler sysConfigAddAuditHandler = SpringContextHelper
            .getBean(SysConfigAddAuditHandler.class);
    private SysConfigSuffixesHandler sysConfigSuffixesHandler = SpringContextHelper
            .getBean(SysConfigSuffixesHandler.class);

    @PostConstruct
    public void init() {
        sysConfigSuffixesHandler.setSuccessor(sysConfigAddAuditHandler);
    }

    @Override
    public void process(SysConfigNamesEnum name, SysConfigBody request, List<SysConfigVo> configs) {
        sysConfigSuffixesHandler.handler(name, request, configs);
    }
}