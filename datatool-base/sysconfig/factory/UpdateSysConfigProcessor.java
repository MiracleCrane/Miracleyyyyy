/*
 * 文 件 名:  UpdateSysConfigProcessor.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/9/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.sysconfig.factory;

import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigBody;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigVo;
import com.huawei.smartcampus.datatool.base.sysconfig.handler.SysConfigAddAuditHandler;
import com.huawei.smartcampus.datatool.base.sysconfig.handler.SysConfigOverviewDbHandler;
import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.PostConstruct;

/**
 * 系统配置的处理责任链
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/30]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Component
@DependsOn("springContextHelper")
public class UpdateSysConfigProcessor implements ConfigProcessor {
    private SysConfigAddAuditHandler sysConfigAddAuditHandler = SpringContextHelper
            .getBean(SysConfigAddAuditHandler.class);
    private SysConfigOverviewDbHandler sysConfigOverviewDbHandler = SpringContextHelper
            .getBean(SysConfigOverviewDbHandler.class);

    @PostConstruct
    public void init() {
        // 现在校验少，先放在一个handler,后面再优化
        sysConfigOverviewDbHandler.setSuccessor(sysConfigAddAuditHandler);
    }

    @Override
    public void process(SysConfigNamesEnum name, SysConfigBody request, List<SysConfigVo> configs) {
        sysConfigOverviewDbHandler.handler(name, request, configs);
    }
}