/*
 * 文 件 名:  BatchScriptDetailTask.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.task;

import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.i18n.I18nUtils;
import com.huawei.smartcampus.datatool.monitor.application.service.ExportTask;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.ExportI18nCode;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job.BatchScriptDetail;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.BatchScriptGateWay;
import com.huawei.smartcampus.datatool.monitor.domain.overview.GroupStatisticsEnum;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.ExcelStyleUtil;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column.BatchScriptDetailColumn;
import com.huawei.smartcampus.datatool.utils.ClassUtils;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;
import com.huawei.smartcampus.datatool.utils.TypeCastUtils;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * 批处理脚本明细任务
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class BatchScriptDetailTask implements ExportTask<List<BatchScriptDetail>> {
    private final BatchScriptGateWay batchScriptGateWay = SpringContextHelper.getBean(BatchScriptGateWay.class);

    @Override
    public List<BatchScriptDetail> call() {
        return batchScriptGateWay.getScriptDetail();
    }

    @Override
    public void fillSheetData(XSSFWorkbook workbook, Object data) {
        List<BatchScriptDetail> batchScriptDetails = TypeCastUtils.objectToList(data, BatchScriptDetail.class);
        XSSFSheet detailSheet = workbook
                .createSheet(I18nUtils.getMessage(ExportI18nCode.DATATOOL_BATCH_SCRIPT_DETAIL_SHEET_NAME));
        XSSFRow firstRow = detailSheet.createRow(0);
        for (int i = 0; i < BatchScriptDetailColumn.values().length; i++) {
            XSSFCell cell = firstRow.createCell(i);
            cell.setCellValue(I18nUtils.getMessage(BatchScriptDetailColumn.values()[i].i18nCode()));
        }
        int rowNum = 1;
        BatchScriptDetailColumn[] batchScriptDetailColumns = BatchScriptDetailColumn.values();
        for (BatchScriptDetail batchScriptDetail : batchScriptDetails) {
            XSSFRow row = detailSheet.createRow(rowNum);
            for (int i = 0; i < batchScriptDetailColumns.length; i++) {
                XSSFCell cell = row.createCell(i);
                String fieldName = batchScriptDetailColumns[i].value();
                String value = ClassUtils.getFieldValue(batchScriptDetail, fieldName, String.class);
                if (value == null) {
                    cell.setCellValue(I18nUtils.getMessage(ExceptionCode.DATATOOL_NA));
                } else if (BatchScriptDetailColumn.ORIGIN.equals(batchScriptDetailColumns[i])) {
                    cell.setCellValue(I18nUtils.getMessage(GroupStatisticsEnum.getI18nCodeByValue(value)));
                } else {
                    cell.setCellValue(value);
                }
            }
            rowNum = rowNum + 1;
        }
        setStyle(workbook, detailSheet, rowNum);
    }

    private void setStyle(XSSFWorkbook workbook, XSSFSheet detailSheet, int endRow) {
        // 设置批处理脚本明细数据样式
        CellStyle dataCellStyle = workbook.createCellStyle();
        ExcelStyleUtil.setFullBorderStyle(dataCellStyle, true, true, true, true);
        ExcelStyleUtil.setFontStyle(workbook, dataCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 1, endRow - 1, 4, dataCellStyle);
        // 设置列名样式
        CellStyle columnCellStyle = workbook.createCellStyle();
        columnCellStyle.cloneStyleFrom(dataCellStyle);
        ExcelStyleUtil.setHeadAndColumnColorStyle(columnCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 0, 0, 4, columnCellStyle);
        ExcelStyleUtil.autoFitColumns(detailSheet, 0, 2);
        // 关联作业列采用固定宽度
        detailSheet.setColumnWidth(3, 50 * 256);
    }
}