/*
 * 文 件 名:  ImportExportService.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/9
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.service;

import com.huawei.smartcampus.datatool.base.vo.req.ExportReq;
import com.huawei.smartcampus.datatool.base.vo.req.ImportHistoryReq;
import com.huawei.smartcampus.datatool.base.vo.req.ImportReq;
import com.huawei.smartcampus.datatool.base.vo.resp.ImportDetailResp;
import com.huawei.smartcampus.datatool.base.vo.resp.ImportHistoryResp;
import com.huawei.smartcampus.datatool.model.BaseResponse;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 导入导出接口
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/9]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public interface ImportExportService {
    /**
     * 异步导入
     *
     * @param importReq 导入入参
     * @param request 请求
     * @return 基本响应结构
     */
    BaseResponse importAsync(ImportReq importReq, HttpServletRequest request);

    /**
     * 导出
     *
     * @param exportReq 导出入参
     * @param response 响应
     */
    void export(ExportReq exportReq, HttpServletResponse response);

    /**
     * 获取导入历史
     *
     * @param importHistoryReq 获取导入历史接口入参
     * @return ImportHistoryResp
     */
    ImportHistoryResp getImportHistory(ImportHistoryReq importHistoryReq);

    /**
     * 查看导入详情
     *
     * @param id 导入任务的id
     * @return ImportDetailResp
     */
    ImportDetailResp getImportDetail(String id);
}
