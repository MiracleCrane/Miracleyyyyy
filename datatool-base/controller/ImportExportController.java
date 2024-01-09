/*
 * 文 件 名:  ImportExportController.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/9
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.controller;

import com.huawei.smartcampus.datatool.auditlog.AuditLogTrack;
import com.huawei.smartcampus.datatool.base.service.ImportExportService;
import com.huawei.smartcampus.datatool.base.vo.req.ExportReq;
import com.huawei.smartcampus.datatool.base.vo.req.ImportHistoryReq;
import com.huawei.smartcampus.datatool.base.vo.req.ImportReq;
import com.huawei.smartcampus.datatool.model.BaseResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 导入导出模块接口
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/9]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@RestController
public class ImportExportController {
    @Autowired
    private ImportExportService importExportService;

    /**
     * 导入
     *
     * @param importReq 导入入参类
     * @param request 请求
     * @return BaseResponse
     */
    @PostMapping(value = "/v1/resources/import")
    @AuditLogTrack(operation = "import resources", isImportOrExport = true)
    public BaseResponse importResources(@Valid ImportReq importReq, HttpServletRequest request) {
        return importExportService.importAsync(importReq, request);
    }

    /**
     * 导出
     *
     * @param exportReq exportReq
     * @param response 响应
     */
    @PostMapping(value = "/v1/resources/export")
    @AuditLogTrack(operation = "export resources", isImportOrExport = true)
    public void exportResources(@RequestBody ExportReq exportReq, HttpServletResponse response) {
        importExportService.export(exportReq, response);
    }

    /**
     * 获取导入历史
     *
     * @param importHistoryReq 获取导入历史接口入参
     * @return BaseResponse
     */
    @PostMapping(value = "/v1/resources/import/history")
    public BaseResponse getImportHistory(@RequestBody @Valid ImportHistoryReq importHistoryReq) {
        return BaseResponse.newOk(importExportService.getImportHistory(importHistoryReq));
    }

    /**
     * 获取导入详情
     *
     * @param id 导入任务的id
     * @return BaseResponse
     */
    @GetMapping(value = "/v1/resources/{id}/import/detail")
    public BaseResponse getImportDetail(@PathVariable String id) {
        return BaseResponse.newOk(importExportService.getImportDetail(id));
    }
}