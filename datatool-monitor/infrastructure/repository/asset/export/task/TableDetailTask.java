/*
 * 文 件 名:  TableDetailTask.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.task;

import com.huawei.smartcampus.datatool.enums.BooleanType;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.i18n.I18nUtils;
import com.huawei.smartcampus.datatool.monitor.application.service.ExportTask;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.ExportI18nCode;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.BusinessDbInfoGateway;
import com.huawei.smartcampus.datatool.monitor.domain.overview.GroupStatisticsEnum;
import com.huawei.smartcampus.datatool.monitor.domain.overview.TableDetailVo;
import com.huawei.smartcampus.datatool.monitor.domain.overview.TableStateEnum;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.ExcelStyleUtil;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column.TableDetailColumn;
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
 * 表明细任务
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class TableDetailTask implements ExportTask<List<TableDetailVo>> {
    private final BusinessDbInfoGateway businessDbInfoGateway = SpringContextHelper
            .getBean(BusinessDbInfoGateway.class);

    @Override
    public List<TableDetailVo> call() {
        List<TableDetailVo> tableDetailVoList = businessDbInfoGateway.getTableDetail();
        businessDbInfoGateway.processTableState(tableDetailVoList);
        for (TableDetailVo tableDetailVo : tableDetailVoList) {
            String schemaFullName = tableDetailVo.getSchema();
            String layer = schemaFullName.split("_")[0];
            String abbreviation = schemaFullName.replaceFirst(layer + "_", "");
            // 接口返回的domain为领域名称，需要修改为领域缩写
            tableDetailVo.setDomain(abbreviation);
        }
        return tableDetailVoList;
    }

    @Override
    public void fillSheetData(XSSFWorkbook workbook, Object data) {
        List<TableDetailVo> tableDetailVoList = TypeCastUtils.objectToList(data, TableDetailVo.class);
        TableDetailColumn[] tableDetailColumns = TableDetailColumn.values();
        XSSFSheet detailSheet = workbook
                .createSheet(I18nUtils.getMessage(ExportI18nCode.DATATOOL_TABLE_DETAIL_SHEET_NAME));
        XSSFRow firstRow = detailSheet.createRow(0);
        for (int i = 0; i < tableDetailColumns.length; i++) {
            XSSFCell cell = firstRow.createCell(i);
            cell.setCellValue(I18nUtils.getMessage(tableDetailColumns[i].i18nCode()));
        }
        int rowNum = 1;
        for (TableDetailVo tableDetailVo : tableDetailVoList) {
            XSSFRow row = detailSheet.createRow(rowNum);
            for (int i = 0; i < tableDetailColumns.length; i++) {
                XSSFCell cell = row.createCell(i);
                String fieldName = tableDetailColumns[i].value();
                Class<?> fieldType = ClassUtils.getFieldType(tableDetailVo, fieldName);
                Object value = ClassUtils.getFieldValue(tableDetailVo, fieldName, fieldType);
                if (value != null) {
                    cell.setCellValue(i18nFormat(value.toString(), fieldName));
                } else {
                    // 值不存在则用NA替代
                    cell.setCellValue(I18nUtils.getMessage(ExceptionCode.DATATOOL_NA));
                }
            }
            rowNum = rowNum + 1;
        }
        setStyle(workbook, detailSheet, rowNum);
    }

    private String i18nFormat(String value, String fieldName) {
        TableDetailColumn tableDetailColumn = TableDetailColumn.getTableDetailColumnByValue(fieldName);
        switch (tableDetailColumn) {
            case SOURCE:
                return I18nUtils.getMessage(GroupStatisticsEnum.getI18nCodeByValue(value));
            case USED:
                return I18nUtils.getMessage(TableStateEnum.valueOf(value).i18nCode());
            case IS_PART_KEY:
            case IS_PRIMARY_KEY:
                return I18nUtils.getMessage(BooleanType.getI18nCodeByValue(Boolean.parseBoolean(value)));
            default:
                return value;
        }
    }

    private void setStyle(XSSFWorkbook workbook, XSSFSheet detailSheet, int endRow) {
        // 设置表明细数据样式
        CellStyle dataCellStyle = workbook.createCellStyle();
        ExcelStyleUtil.setFullBorderStyle(dataCellStyle, true, true, true, true);
        ExcelStyleUtil.setFontStyle(workbook, dataCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 1, endRow - 1, 12, dataCellStyle);
        // 设置列名样式
        CellStyle columnCellStyle = workbook.createCellStyle();
        columnCellStyle.cloneStyleFrom(dataCellStyle);
        ExcelStyleUtil.setHeadAndColumnColorStyle(columnCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 0, 0, 12, columnCellStyle);
        ExcelStyleUtil.autoFitColumns(detailSheet, 0, 5);
        // 表中文名列采用固定宽度
        detailSheet.setColumnWidth(6, 50 * 256);
        ExcelStyleUtil.autoFitColumns(detailSheet, 7, 7);
        ExcelStyleUtil.autoFitColumns(detailSheet, 9, 11);
        // 字段说明列采用固定宽度
        detailSheet.setColumnWidth(8, 50 * 256);
    }
}