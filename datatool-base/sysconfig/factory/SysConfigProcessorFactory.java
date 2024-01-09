/*
 * 文 件 名:  SysConfigProcessorFactory.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/9/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.sysconfig.factory;

import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

/**
 * 系统配置工厂
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/30]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class SysConfigProcessorFactory {
    public static ConfigProcessor createProcessor(SysConfigNamesEnum name) {
        if (name == null) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYS_CONFIG_TYPE_NOT_SUPPORT);
        }
        // 目前只有资产概览配置
        switch (name) {
            case OVERVIEW_DB:
                return SpringContextHelper.getBean(UpdateSysConfigProcessor.class);
            case CUSTOM_FLAG:
                return SpringContextHelper.getBean(UpdateSuffixesProcessor.class);
            default:
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYS_CONFIG_TYPE_NOT_SUPPORT);
        }
    }
}