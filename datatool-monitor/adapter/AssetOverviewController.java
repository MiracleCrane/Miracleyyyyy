/*
 * 文 件 名:  AssetOverviewController.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.adapter;

import com.huawei.smartcampus.datatool.auditlog.AuditLogTrack;
import com.huawei.smartcampus.datatool.concurrentlimit.ConcurrentLimit;
import com.huawei.smartcampus.datatool.model.BaseResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.AssetOverviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 资产概览接口
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/19]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@RestController
@RequestMapping(value = "/v1/assets")
public class AssetOverviewController {
    @Autowired
    private AssetOverviewService assetOverviewService;

    @GetMapping(value = "/jobs/amount")
    @ConcurrentLimit()
    public BaseResponse queryJobAmount() {
        return BaseResponse.newOk(assetOverviewService.queryJobAmount());
    }

    @GetMapping(value = "/tables/amount")
    @ConcurrentLimit()
    public BaseResponse queryTableAmount() {
        return BaseResponse.newOk(assetOverviewService.queryTableAmount());
    }

    @GetMapping(value = "/tables/state")
    @ConcurrentLimit()
    public BaseResponse queryTableState() {
        return BaseResponse.newOk(assetOverviewService.queryTableState());
    }

    @GetMapping(value = "/jobs/state")
    @ConcurrentLimit()
    public BaseResponse queryJobState() {
        return BaseResponse.newOk(assetOverviewService.queryJobState());
    }

    @GetMapping(value = "/export")
    @ConcurrentLimit()
    @AuditLogTrack(operation = "export assets", operationObject = "assets list", isImportOrExport = true)
    public void exportAssetsList(HttpServletResponse response) {
        assetOverviewService.exportList(response);
    }
}