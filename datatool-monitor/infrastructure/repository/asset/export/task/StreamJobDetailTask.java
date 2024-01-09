/*
 * 文 件 名:  StreamJobDetailTask.java
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
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job.StreamJobDetail;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.StreamJobGateway;
import com.huawei.smartcampus.datatool.monitor.domain.overview.GroupStatisticsEnum;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.ExcelStyleUtil;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column.StreamJobDetailColumn;
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
 * 流处理作业明细任务
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class StreamJobDetailTask implements ExportTask<List<StreamJobDetail>> {
    private final StreamJobGateway streamJobGateway = SpringContextHelper.getBean(StreamJobGateway.class);

    @Override
    public List<StreamJobDetail> call() {
        return streamJobGateway.getStreamJobDetail();
    }

    @Override
    public void fillSheetData(XSSFWorkbook workbook, Object data) {
        List<StreamJobDetail> streamJobDetails = TypeCastUtils.objectToList(data, StreamJobDetail.class);
        XSSFSheet detailSheet = workbook
                .createSheet(I18nUtils.getMessage(ExportI18nCode.DATATOOL_STREAM_JOB_DETAIL_SHEET_NAME));
        XSSFRow firstRow = detailSheet.createRow(0);
        for (int i = 0; i < StreamJobDetailColumn.values().length; i++) {
            XSSFCell cell = firstRow.createCell(i);
            cell.setCellValue(I18nUtils.getMessage(StreamJobDetailColumn.values()[i].i18nCode()));
        }
        int rowNum = 1;
        StreamJobDetailColumn[] streamJobDetailColumns = StreamJobDetailColumn.values();
        for (StreamJobDetail streamJobDetail : streamJobDetails) {
            XSSFRow row = detailSheet.createRow(rowNum);
            for (int i = 0; i < streamJobDetailColumns.length; i++) {
                XSSFCell cell = row.createCell(i);
                String fieldName = streamJobDetailColumns[i].value();
                String value = ClassUtils.getFieldValue(streamJobDetail, fieldName, String.class);
                if (value == null) {
                    cell.setCellValue(I18nUtils.getMessage(ExceptionCode.DATATOOL_NA));
                } else if (StreamJobDetailColumn.ORIGIN.equals(streamJobDetailColumns[i])) {
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
        // 设置流处理作业明细数据样式
        CellStyle dataCellStyle = workbook.createCellStyle();
        ExcelStyleUtil.setFullBorderStyle(dataCellStyle, true, true, true, true);
        ExcelStyleUtil.setFontStyle(workbook, dataCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 1, endRow - 1, 3, dataCellStyle);
        // 设置列名样式
        CellStyle columnCellStyle = workbook.createCellStyle();
        columnCellStyle.cloneStyleFrom(dataCellStyle);
        ExcelStyleUtil.setHeadAndColumnColorStyle(columnCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 0, 0, 3, columnCellStyle);
        ExcelStyleUtil.autoFitColumns(detailSheet, 0, 2);
    }
}