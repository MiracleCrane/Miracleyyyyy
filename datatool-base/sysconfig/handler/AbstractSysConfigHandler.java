/*
 * 文 件 名:  AbstractSysConfigHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/9/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.sysconfig.handler;

import com.huawei.smartcampus.datatool.base.handler.importdata.base.AbstractImportHandler;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigBody;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigVo;
import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 抽象系统配置handler
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/28]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public abstract class AbstractSysConfigHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImportHandler.class);
    private AbstractSysConfigHandler nextHandler;

    public void setSuccessor(AbstractSysConfigHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public void handler(SysConfigNamesEnum name, SysConfigBody request, List<SysConfigVo> configs) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("system configuration processor:{}", this.getClass().getSimpleName());
        }
        doHandler(name, request, configs);
        if (nextHandler == null) {
            return;
        }
        nextHandler.handler(name, request, configs);
    }

    protected abstract void doHandler(SysConfigNamesEnum name, SysConfigBody request, List<SysConfigVo> configs);
}