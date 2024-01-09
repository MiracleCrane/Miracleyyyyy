/*
 * 文 件 名:  ExportTask.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.concurrent.Callable;

/**
 * 导出任务
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public interface ExportTask<T> extends Callable<T> {
    void fillSheetData(XSSFWorkbook workbook, Object data);
}