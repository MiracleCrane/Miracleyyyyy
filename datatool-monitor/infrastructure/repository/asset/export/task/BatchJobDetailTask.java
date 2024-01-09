/*
 * 文 件 名:  BatchJobDetailTask.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.task;

import com.huawei.smartcampus.datatool.enums.BooleanType;
import com.huawei.smartcampus.datatool.enums.PeriodUnit;
import com.huawei.smartcampus.datatool.enums.ScheduleType;
import com.huawei.smartcampus.datatool.enums.Week;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.i18n.I18nUtils;
import com.huawei.smartcampus.datatool.monitor.application.service.ExportTask;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.BatchJobDetail;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.ExportI18nCode;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.BatchJobGateway;
import com.huawei.smartcampus.datatool.monitor.domain.overview.GroupStatisticsEnum;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.ExcelStyleUtil;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column.BatchJobDetailColumn;
import com.huawei.smartcampus.datatool.utils.ClassUtils;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;
import com.huawei.smartcampus.datatool.utils.StringUtils;
import com.huawei.smartcampus.datatool.utils.TimeUtil;
import com.huawei.smartcampus.datatool.utils.TypeCastUtils;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 批处理作业明细任务
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class BatchJobDetailTask implements ExportTask<List<BatchJobDetail>> {
    private final BatchJobGateway batchJobGateway = SpringContextHelper.getBean(BatchJobGateway.class);
    private static final String COMMA = ",";

    @Override
    public List<BatchJobDetail> call() {
        return batchJobGateway.getBatchJobDetail();
    }

    @Override
    public void fillSheetData(XSSFWorkbook workbook, Object data) {
        List<BatchJobDetail> batchJobDetailList = TypeCastUtils.objectToList(data, BatchJobDetail.class);
        XSSFSheet detailSheet = workbook
                .createSheet(I18nUtils.getMessage(ExportI18nCode.DATATOOL_BATCH_JOB_DETAIL_SHEET_NAME));
        XSSFRow firstRow = detailSheet.createRow(0);
        for (int i = 0; i < BatchJobDetailColumn.values().length; i++) {
            XSSFCell cell = firstRow.createCell(i);
            cell.setCellValue(I18nUtils.getMessage(BatchJobDetailColumn.values()[i].i18nCode()));
        }
        int rowNum = 1;
        BatchJobDetailColumn[] batchJobDetailColumns = BatchJobDetailColumn.values();
        for (BatchJobDetail batchJobDetail : batchJobDetailList) {
            XSSFRow row = detailSheet.createRow(rowNum);
            for (int i = 0; i < batchJobDetailColumns.length; i++) {
                XSSFCell cell = row.createCell(i);
                String fieldName = batchJobDetailColumns[i].value();
                Class<?> fieldType = ClassUtils.getFieldType(batchJobDetail, fieldName);
                Object value = ClassUtils.getFieldValue(batchJobDetail, fieldName, fieldType);
                if (value != null) {
                    cell.setCellValue(i18nFormat(value.toString(), fieldName, batchJobDetail));
                } else {
                    cell.setCellValue(I18nUtils.getMessage(ExceptionCode.DATATOOL_NA));
                }
            }
            rowNum = rowNum + 1;
        }
        setStyle(workbook, detailSheet, rowNum);
    }

    private String i18nFormat(String originValue, String fieldName, BatchJobDetail batchJobDetail) {
        BatchJobDetailColumn batchJobDetailColumn = BatchJobDetailColumn.getBatchJobDetailColumnByValue(fieldName);
        switch (batchJobDetailColumn) {
            case ORIGIN:
                return I18nUtils.getMessage(GroupStatisticsEnum.getI18nCodeByValue(originValue));
            case SCHEDULE_TYPE:
                return I18nUtils.getMessage(ScheduleType.getI18nCodeByValue(originValue));
            case PERIOD_UNIT:
                return StringUtils.isEmpty(originValue)
                        ? I18nUtils.getMessage(ExceptionCode.DATATOOL_NA)
                        : I18nUtils.getMessage(PeriodUnit.getI18nCodeByValue(originValue));
            case PERIOD_INTERVAL:
                return StringUtils.isEmpty(originValue)
                        ? I18nUtils.getMessage(ExceptionCode.DATATOOL_NA)
                        : handlePeriodInterval(batchJobDetail.getPeriodUnit(), originValue);
            case SCHEDULE_TIME_PER_DAY:
            case DEPEND_JOBS:
                return StringUtils.isEmpty(originValue) ? I18nUtils.getMessage(ExceptionCode.DATATOOL_NA) : originValue;
            case SELF_DEPENDENT:
            case RUNNING:
                return StringUtils.isEmpty(originValue)
                        ? I18nUtils.getMessage(ExceptionCode.DATATOOL_NA)
                        : I18nUtils.getMessage(BooleanType.getI18nCodeByValue(Boolean.parseBoolean(originValue)));
            default:
                return originValue;
        }
    }

    private String handlePeriodInterval(String periodUnit, String originPeriodInterval) {
        String[] periodNumber;
        List<String> periodInterval = new ArrayList<>();
        switch (PeriodUnit.getPeriodUnitByValue(periodUnit)) {
            case MINUTE:
            case HOUR:
            case DAY:
                return originPeriodInterval + I18nUtils.getMessage(PeriodUnit.getI18nCodeByValue(periodUnit));
            case WEEK:
                periodNumber = originPeriodInterval.split(COMMA);
                for (String number : periodNumber) {
                    periodInterval.add(I18nUtils.getMessage(Week.getI18nCodeByValue(Integer.parseInt(number))));
                }
                // 中文处理成周一,周二，英文处理成Monday,Tuesday
                return String.join(COMMA, periodInterval);
            case MONTH:
                periodNumber = originPeriodInterval.split(COMMA);
                for (String number : periodNumber) {
                    if (LocaleContextHolder.getLocale().equals(Locale.US)) {
                        periodInterval.add(TimeUtil.getDateOfMonthWithSuffix(Integer.parseInt(number)));
                    } else {
                        periodInterval.add(number + "号");
                    }
                }
                return String.join(COMMA, periodInterval);
            default:
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PERIOD_NOT_SUPPORT, periodUnit);
        }
    }

    private void setStyle(XSSFWorkbook workbook, XSSFSheet detailSheet, int endRow) {
        // 设置批处理作业明细数据样式
        CellStyle dataCellStyle = workbook.createCellStyle();
        ExcelStyleUtil.setFullBorderStyle(dataCellStyle, true, true, true, true);
        ExcelStyleUtil.setFontStyle(workbook, dataCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 1, endRow - 1, 11, dataCellStyle);
        // 设置列名样式
        CellStyle columnCellStyle = workbook.createCellStyle();
        columnCellStyle.cloneStyleFrom(dataCellStyle);
        ExcelStyleUtil.setHeadAndColumnColorStyle(columnCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 0, 0, 11, columnCellStyle);
        ExcelStyleUtil.autoFitColumns(detailSheet, 0, 10);
    }
}