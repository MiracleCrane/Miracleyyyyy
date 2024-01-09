/*
 * 文 件 名:  SysConfigGateWay.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/9/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.sysconfig;

import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.Configuration;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigVo;
import com.huawei.smartcampus.datatool.entity.SystemConfigurationEntity;
import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;

import java.util.List;

/**
 * 系统配置抽象接口
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/19]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public interface SysConfigGateWay {
    SysConfigNamesEnum getSysConfigName(String name);

    List<Configuration> querySysConfig(SysConfigNamesEnum configName);

    List<SystemConfigurationEntity> queryOriginSysConfig(SysConfigNamesEnum name);

    void saveAll(SysConfigNamesEnum configname, List<SysConfigVo> sysConfigVos);
}