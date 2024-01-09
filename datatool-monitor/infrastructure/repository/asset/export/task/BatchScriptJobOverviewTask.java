/*
 * 文 件 名:  BatchScriptJobOverviewTask.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.task;

import com.huawei.smartcampus.datatool.i18n.I18nUtils;
import com.huawei.smartcampus.datatool.monitor.application.service.AssetOverviewService;
import com.huawei.smartcampus.datatool.monitor.application.service.ExportTask;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.JobAmountResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.JobStateAmountResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.ExportI18nCode;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.GroupAmount;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job.BatchScriptJobOverview;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job.JobAmountClassification;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job.JobStateAmount;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job.JobStateAmountClassification;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job.JobSummary;
import com.huawei.smartcampus.datatool.monitor.domain.overview.GroupStatisticsEnum;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.ExcelStyleUtil;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column.AggregationColumn;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column.JobOverviewColumn;
import com.huawei.smartcampus.datatool.utils.ClassUtils;
import com.huawei.smartcampus.datatool.utils.MathUtils;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 批处理作业脚本概览任务
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class BatchScriptJobOverviewTask implements ExportTask<BatchScriptJobOverview> {
    private final AssetOverviewService assetOverviewService = SpringContextHelper.getBean(AssetOverviewService.class);

    @Override
    public BatchScriptJobOverview call() {
        BatchScriptJobOverview batchScriptJobOverview = new BatchScriptJobOverview();
        JobAmountResponse jobAmountResponse = assetOverviewService.queryJobAmount();
        JobStateAmountResponse jobStateAmountResponse = assetOverviewService.queryJobState();
        batchScriptJobOverview.setJobSummary(getJobSummary(jobStateAmountResponse.getSummary().getTotal(),
                jobStateAmountResponse.getSummary().getUsed()));
        batchScriptJobOverview.setJobAmountClassification(getJobAmountClassification(jobAmountResponse));
        batchScriptJobOverview.setJobState(getJobStateAmount(jobStateAmountResponse));
        return batchScriptJobOverview;
    }

    @Override
    public void fillSheetData(XSSFWorkbook workbook, Object data) {
        if (!(data instanceof BatchScriptJobOverview)) {
            return;
        }
        BatchScriptJobOverview batchScriptJobOverview = (BatchScriptJobOverview) data;
        XSSFSheet batchScriptJobOverviewSheet = workbook
                .createSheet(I18nUtils.getMessage(ExportI18nCode.DATATOOL_BATCH_SCRIPT_JOB_OVERVIEW_SHEET_NAME));
        int currentRowNum = 0;
        currentRowNum = fillJobOverview(batchScriptJobOverviewSheet, currentRowNum,
                batchScriptJobOverview.getJobSummary());
        currentRowNum = fillAggregationTable(batchScriptJobOverviewSheet, currentRowNum + 1,
                batchScriptJobOverview.getJobAmountClassification());
        fillJobStateTable(batchScriptJobOverviewSheet, currentRowNum + 1, batchScriptJobOverview.getJobState());
        // 首列采用固定宽度
        batchScriptJobOverviewSheet.setColumnWidth(0, 20 * 256);
    }

    private JobSummary getJobSummary(int totalJobNum, int usedJobNum) {
        JobSummary jobSummary = new JobSummary();
        jobSummary.setTotalJobNum(totalJobNum);
        jobSummary.setTotalRunningJobNum(usedJobNum);
        jobSummary.setUtilizationRate(MathUtils.getQuotient(usedJobNum, totalJobNum));
        return jobSummary;
    }

    private JobAmountClassification getJobAmountClassification(JobAmountResponse jobAmountResponse) {
        JobAmountClassification jobAmountClassification = new JobAmountClassification();
        jobAmountClassification.setBatchJob(new GroupAmount(jobAmountResponse.getAll().getBatchJobNum(),
                jobAmountResponse.getBaseline().getBatchJobNum(), jobAmountResponse.getCustom().getBatchJobNum()));
        jobAmountClassification.setBatchScript(new GroupAmount(jobAmountResponse.getAll().getBatchScriptNum(),
                jobAmountResponse.getBaseline().getBatchScriptNum(),
                jobAmountResponse.getCustom().getBatchScriptNum()));
        jobAmountClassification.setStreamJob(new GroupAmount(jobAmountResponse.getAll().getStreamJobNum(),
                jobAmountResponse.getBaseline().getStreamJobNum(), jobAmountResponse.getCustom().getStreamJobNum()));
        int totalBaselineJobNum = jobAmountResponse.getBaseline().getBatchJobNum()
                + jobAmountResponse.getBaseline().getBatchScriptNum()
                + jobAmountResponse.getBaseline().getStreamJobNum();
        int totalCustomJobNum = jobAmountResponse.getCustom().getBatchJobNum()
                + jobAmountResponse.getCustom().getBatchScriptNum() + jobAmountResponse.getCustom().getStreamJobNum();
        jobAmountClassification.setTotal(
                new GroupAmount(jobAmountResponse.getSummary() + jobAmountResponse.getAll().getBatchScriptNum(),
                        totalBaselineJobNum, totalCustomJobNum));
        return jobAmountClassification;
    }

    private JobStateAmountClassification getJobStateAmount(JobStateAmountResponse jobStateAmountResponse) {
        JobStateAmountClassification jobStateAmountClassification = new JobStateAmountClassification();
        int usedBatchJob = jobStateAmountResponse.getAll().getBatchJob().getUsed();
        int unusedBatchJob = jobStateAmountResponse.getAll().getBatchJob().getUnused();
        int usedStreamJob = jobStateAmountResponse.getAll().getStreamJob().getUsed();
        int unusedStreamJob = jobStateAmountResponse.getAll().getStreamJob().getUnused();
        jobStateAmountClassification.setBatchJob(new JobStateAmount(usedBatchJob, unusedBatchJob));
        jobStateAmountClassification.setStreamJob(new JobStateAmount(usedStreamJob, unusedStreamJob));
        jobStateAmountClassification
                .setTotal(new JobStateAmount(usedBatchJob + usedStreamJob, unusedBatchJob + unusedStreamJob));
        return jobStateAmountClassification;
    }

    private int fillJobOverview(XSSFSheet batchScriptJobOverviewSheet, int currentRowNum, JobSummary jobSummary) {
        XSSFRow tableFirstRow = batchScriptJobOverviewSheet.createRow(currentRowNum);
        // 合并A-B列
        batchScriptJobOverviewSheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum, 0, 1));
        tableFirstRow.createCell(0).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_JOB_OVERVIEW_TABLE_NAME));
        currentRowNum = currentRowNum + 1;
        JobOverviewColumn[] jobOverviewColumns = JobOverviewColumn.values();
        for (JobOverviewColumn jobOverviewColumn : jobOverviewColumns) {
            String fieldName = jobOverviewColumn.value();
            XSSFRow row = batchScriptJobOverviewSheet.createRow(currentRowNum);
            // 每个属性增加一行
            currentRowNum = currentRowNum + 1;
            row.createCell(0).setCellValue(I18nUtils.getMessage(jobOverviewColumn.i18nCode()));
            Class<?> fieldType = ClassUtils.getFieldType(jobSummary, fieldName);
            Object value = ClassUtils.getFieldValue(jobSummary, fieldName, fieldType);
            if (value instanceof Double) {
                row.createCell(1).setCellValue((Double) value);
            } else if (value instanceof Integer) {
                row.createCell(1).setCellValue((Integer) value);
            }
        }
        setJobOverviewStyle(batchScriptJobOverviewSheet.getWorkbook(), batchScriptJobOverviewSheet, tableFirstRow);
        return currentRowNum;
    }

    private int fillAggregationTable(XSSFSheet batchScriptJobOverviewSheet, int currentRowNum,
            JobAmountClassification jobAmountClassification) {
        // 分类汇总表占6-11行，A-D列
        XSSFRow tableFirstRow = batchScriptJobOverviewSheet.createRow(currentRowNum);
        // 合并A-D列
        batchScriptJobOverviewSheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum, 0, 3));
        tableFirstRow.createCell(0).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_AGGREGATION_TABLE_NAME));
        currentRowNum = currentRowNum + 1;
        XSSFRow colNameRow = batchScriptJobOverviewSheet.createRow(currentRowNum);
        colNameRow.createCell(0).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_CLASSIFICATION));
        colNameRow.createCell(1).setCellValue(I18nUtils.getMessage(GroupStatisticsEnum.ALL.i18nCode()));
        colNameRow.createCell(2).setCellValue(I18nUtils.getMessage(GroupStatisticsEnum.BASELINE.i18nCode()));
        colNameRow.createCell(3).setCellValue(I18nUtils.getMessage(GroupStatisticsEnum.CUSTOM.i18nCode()));
        currentRowNum = currentRowNum + 1;
        AggregationColumn[] aggregationColumns = AggregationColumn.values();
        for (AggregationColumn aggregationColumn : aggregationColumns) {
            String fieldName = aggregationColumn.value();
            XSSFRow row = batchScriptJobOverviewSheet.createRow(currentRowNum);
            currentRowNum = currentRowNum + 1;
            row.createCell(0).setCellValue(I18nUtils.getMessage(aggregationColumn.i18nCode()));
            GroupAmount value = ClassUtils.getFieldValue(jobAmountClassification, fieldName, GroupAmount.class);
            if (value == null) {
                continue;
            }
            row.createCell(1).setCellValue(value.getAll());
            row.createCell(2).setCellValue(value.getBaseline());
            row.createCell(3).setCellValue(value.getCustom());
        }
        setAggregationStyle(batchScriptJobOverviewSheet.getWorkbook(), batchScriptJobOverviewSheet, tableFirstRow);
        return currentRowNum;
    }

    private void fillJobStateTable(XSSFSheet batchScriptJobOverviewSheet, int currentRowNum,
            JobStateAmountClassification jobStateAmountClassification) {
        // 作业状态表占13-17行，A-C列
        XSSFRow tableFirstRow = batchScriptJobOverviewSheet.createRow(currentRowNum);
        // 合并A-C列
        batchScriptJobOverviewSheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum, 0, 2));
        tableFirstRow.createCell(0).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_JOB_STATE_TABLE_NAME));
        currentRowNum = currentRowNum + 1;
        XSSFRow colNameRow = batchScriptJobOverviewSheet.createRow(currentRowNum);
        colNameRow.createCell(0).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_CLASSIFICATION));
        colNameRow.createCell(1).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_START));
        colNameRow.createCell(2).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_STOP));
        currentRowNum = currentRowNum + 1;
        AggregationColumn[] aggregationColumns = AggregationColumn.values();
        for (AggregationColumn aggregationColumn : aggregationColumns) {
            String fieldName = aggregationColumn.value();
            // 作业状态不统计批处理脚本
            if (AggregationColumn.BATCH_SCRIPT.equals(aggregationColumn)) {
                continue;
            }
            XSSFRow row = batchScriptJobOverviewSheet.createRow(currentRowNum);
            currentRowNum = currentRowNum + 1;
            row.createCell(0).setCellValue(I18nUtils.getMessage(aggregationColumn.i18nCode()));
            JobStateAmount value = ClassUtils.getFieldValue(jobStateAmountClassification, fieldName,
                    JobStateAmount.class);
            if (value == null) {
                continue;
            }
            row.createCell(1).setCellValue(value.getRunning());
            row.createCell(2).setCellValue(value.getStopped());
        }
        setJobStateStyle(batchScriptJobOverviewSheet.getWorkbook(), batchScriptJobOverviewSheet, tableFirstRow);
    }

    private void setJobOverviewStyle(XSSFWorkbook workbook, XSSFSheet detailSheet, XSSFRow headRow) {
        ExcelStyleUtil.setHeadMergeRowStyle(workbook, headRow, 2);
        // 设置作业总览表样式
        CellStyle dataCellStyle = workbook.createCellStyle();
        ExcelStyleUtil.setFullBorderStyle(dataCellStyle, true, true, true, true);
        ExcelStyleUtil.setFontStyle(workbook, dataCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 1, 3, 2, dataCellStyle);
        XSSFCell utilizationRateCell = detailSheet.getRow(3).getCell(1);
        CellStyle utilizationRateCellStyle = workbook.createCellStyle();
        utilizationRateCellStyle.cloneStyleFrom(utilizationRateCell.getCellStyle());
        // 设置单元格样式为百分比
        utilizationRateCellStyle.setDataFormat(workbook.createDataFormat().getFormat("0%"));
        utilizationRateCell.setCellStyle(utilizationRateCellStyle);
    }

    private void setAggregationStyle(XSSFWorkbook workbook, XSSFSheet detailSheet, XSSFRow headRow) {
        ExcelStyleUtil.setHeadMergeRowStyle(workbook, headRow, 4);
        // 设置分类汇总表数据样式
        CellStyle dataCellStyle = workbook.createCellStyle();
        ExcelStyleUtil.setFullBorderStyle(dataCellStyle, true, true, true, true);
        ExcelStyleUtil.setFontStyle(workbook, dataCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 7, 10, 4, dataCellStyle);
        // 设置列名样式
        CellStyle columnCellStyle = workbook.createCellStyle();
        columnCellStyle.cloneStyleFrom(dataCellStyle);
        ExcelStyleUtil.setHeadAndColumnColorStyle(columnCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 6, 6, 4, columnCellStyle);
    }

    private void setJobStateStyle(XSSFWorkbook workbook, XSSFSheet detailSheet, XSSFRow headRow) {
        ExcelStyleUtil.setHeadMergeRowStyle(workbook, headRow, 3);
        // 设置作业状态表数据样式
        CellStyle dataCellStyle = workbook.createCellStyle();
        ExcelStyleUtil.setFullBorderStyle(dataCellStyle, true, true, true, true);
        ExcelStyleUtil.setFontStyle(workbook, dataCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 14, 16, 3, dataCellStyle);
        // 设置列名样式
        CellStyle columnCellStyle = workbook.createCellStyle();
        columnCellStyle.cloneStyleFrom(dataCellStyle);
        ExcelStyleUtil.setHeadAndColumnColorStyle(columnCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 13, 13, 3, columnCellStyle);
    }
}