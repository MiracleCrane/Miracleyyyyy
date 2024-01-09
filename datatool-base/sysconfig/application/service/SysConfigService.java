/*
 * 文 件 名:  SysConfigService.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/10/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.sysconfig.application.service;

import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigBody;

/**
 * 系统配置服务
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public interface SysConfigService {
    SysConfigBody querySysConfig(String name);

    void updateSysConfig(String name, SysConfigBody request);
}