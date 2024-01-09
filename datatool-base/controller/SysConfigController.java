/*
 * 文 件 名:  SysConfigController.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/9/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.controller;

import com.huawei.smartcampus.datatool.auditlog.AuditLogTrack;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.SysConfigService;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigBody;
import com.huawei.smartcampus.datatool.model.BaseResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 系统配置接口
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/19]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@RestController
@RequestMapping(value = "/v1/configure")
public class SysConfigController {
    @Autowired
    private SysConfigService sysConfigService;

    @GetMapping(value = "/system-configurations/{name}")
    public BaseResponse querySystemConfiguration(@PathVariable String name) {
        return BaseResponse.newOk(sysConfigService.querySysConfig(name));
    }

    @PutMapping(value = "/system-configurations/{name}")
    @AuditLogTrack(operation = "update system configuration", operationObject = "system configuration")
    public BaseResponse updateSystemConfiguration(@PathVariable String name,
            @RequestBody @Valid SysConfigBody request) {
        sysConfigService.updateSysConfig(name, request);
        return BaseResponse.newOk();
    }
}