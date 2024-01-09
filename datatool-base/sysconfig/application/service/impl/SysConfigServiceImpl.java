/*
 * 文 件 名:  SysConfigServiceImpl.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/10/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.sysconfig.application.service.impl;

import com.huawei.smartcampus.datatool.base.sysconfig.SysConfigGateWay;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.SysConfigService;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigBody;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigVo;
import com.huawei.smartcampus.datatool.base.sysconfig.factory.ConfigProcessor;
import com.huawei.smartcampus.datatool.base.sysconfig.factory.SysConfigProcessorFactory;
import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统配置服务
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysConfigServiceImpl.class);

    @Autowired
    private SysConfigGateWay sysConfigGateWay;

    @Override
    public SysConfigBody querySysConfig(String name) {
        SysConfigBody sysConfigBody = new SysConfigBody();
        SysConfigNamesEnum configName = sysConfigGateWay.getSysConfigName(name);
        // 类型不对查询结果为空
        if (configName == null) {
            return sysConfigBody;
        }
        sysConfigBody.setConfigurations(sysConfigGateWay.querySysConfig(configName));
        return sysConfigBody;
    }

    @Override
    public void updateSysConfig(String name, SysConfigBody request) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("updateSysConfig {},request: {}", name, request.toString());
        }
        SysConfigNamesEnum configName = sysConfigGateWay.getSysConfigName(name);
        ConfigProcessor processor = SysConfigProcessorFactory.createProcessor(configName);
        List<SysConfigVo> configs = new ArrayList<>();
        processor.process(configName, request, configs);
        sysConfigGateWay.saveAll(configName, configs);
    }
}