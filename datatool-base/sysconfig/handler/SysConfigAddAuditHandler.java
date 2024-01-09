/*
 * 文 件 名:  SysConfigAddAuditHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/9/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.sysconfig.handler;

import com.huawei.smartcampus.datatool.base.sysconfig.SysConfigGateWay;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.impl.SysConfigServiceImpl;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigBody;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigVo;
import com.huawei.smartcampus.datatool.entity.SystemConfigurationEntity;
import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;
import com.huawei.smartcampus.datatool.utils.RequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 添加审计字段
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/30]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SysConfigAddAuditHandler extends AbstractSysConfigHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysConfigServiceImpl.class);

    @Autowired
    private SysConfigGateWay sysConfigGateWay;

    @Override
    protected void doHandler(SysConfigNamesEnum name, SysConfigBody request, List<SysConfigVo> configs) {
        List<SystemConfigurationEntity> entities = sysConfigGateWay.queryOriginSysConfig(name);
        boolean isUpdate = entities.stream()
                .anyMatch(config -> config.getKey().equals("createdBy") || config.getKey().equals("createdDate"));
        Date now = new Date();
        if (!isUpdate) {
            configs.add(new SysConfigVo(name.value(), "createdBy", RequestContext.getUserName()));
            configs.add(new SysConfigVo(name.value(), "createdDate", now));
        }
        configs.add(new SysConfigVo(name.value(), "lastModifiedBy", RequestContext.getUserName()));
        configs.add(new SysConfigVo(name.value(), "lastModifiedDate", now));
    }
}