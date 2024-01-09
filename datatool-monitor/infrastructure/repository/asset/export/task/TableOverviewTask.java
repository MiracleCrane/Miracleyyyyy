/*
 * 文 件 名:  TableOverviewTask.java
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
import com.huawei.smartcampus.datatool.monitor.application.service.AssetOverviewService;
import com.huawei.smartcampus.datatool.monitor.application.service.ExportTask;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.OverviewResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.SchemaData;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.TableAmountData;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.TableAmountItem;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.UsageData;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.ExportI18nCode;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.GroupAmount;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.TableOverview;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.UsageAmount;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.table.DomainClassification;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.table.DomainInfo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.table.LayerAggregation;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.table.TableDataOverview;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.table.TableUsageState;
import com.huawei.smartcampus.datatool.monitor.domain.dw.DWTier;
import com.huawei.smartcampus.datatool.monitor.domain.dw.DataWareHouse;
import com.huawei.smartcampus.datatool.monitor.domain.dw.stat.DWStatistics;
import com.huawei.smartcampus.datatool.monitor.domain.dw.stat.DWTableRowStat;
import com.huawei.smartcampus.datatool.monitor.domain.dw.stat.DWTableRows;
import com.huawei.smartcampus.datatool.monitor.domain.factory.DWFactory;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.BusinessDbInfoGateway;
import com.huawei.smartcampus.datatool.monitor.domain.overview.GroupStatisticsEnum;
import com.huawei.smartcampus.datatool.monitor.domain.overview.ModelLayerEnum;
import com.huawei.smartcampus.datatool.monitor.domain.overview.TableDetailVo;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.DataSizeUnit;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.ExcelStyleUtil;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column.DomainClassificationColumn;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column.LayerAmountColumn;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column.TableDataOverviewColumn;
import com.huawei.smartcampus.datatool.utils.ClassUtils;
import com.huawei.smartcampus.datatool.utils.MathUtils;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.i18n.LocaleContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 表概览任务
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class TableOverviewTask implements ExportTask<TableOverview> {
    private final AssetOverviewService assetOverviewService = SpringContextHelper.getBean(AssetOverviewService.class);
    private final BusinessDbInfoGateway businessDbInfoGateway = SpringContextHelper
            .getBean(BusinessDbInfoGateway.class);
    private final DWFactory dwFactory = SpringContextHelper.getBean(DWFactory.class);

    @Override
    public TableOverview call() {
        TableOverview tableOverview = new TableOverview();
        OverviewResponse<TableAmountItem, TableAmountData<TableAmountItem>> tableAmountItem = assetOverviewService
                .queryTableAmount();
        OverviewResponse<UsageData, TableAmountData<UsageData>> usageData = assetOverviewService.queryTableState();
        List<TableDetailVo> tableDetailVoList = businessDbInfoGateway.getAllTableSchema();
        tableOverview.setTableDataOverview(
                getTableOverviewData(tableAmountItem.getSummary().getTableNum(), usageData.getSummary().getUsed()));
        tableOverview.setLayerAggregation(getLayerAggregation(tableAmountItem));
        tableOverview.setTableUsageState(getTableUsageState(usageData));
        tableOverview.setDomainClassification(getDomainClassification(tableDetailVoList, tableAmountItem));
        return tableOverview;
    }

    @Override
    public void fillSheetData(XSSFWorkbook workbook, Object data) {
        if (!(data instanceof TableOverview)) {
            return;
        }
        TableOverview tableDataOverview = (TableOverview) data;
        XSSFSheet tableOverviewSheet = workbook
                .createSheet(I18nUtils.getMessage(ExportI18nCode.DATATOOL_TABLE_OVERVIEW_SHEET_NAME));
        int currentRow = 0;
        // 填充表数据总览表
        currentRow = fillTableDataOverview(tableOverviewSheet, tableDataOverview.getTableDataOverview(), currentRow);
        // 填充分层汇总表
        currentRow = fillLayerAggregation(tableOverviewSheet, tableDataOverview.getLayerAggregation(), currentRow + 1);
        // 填充表示用状态表
        currentRow = fillUsageStateTable(tableOverviewSheet, tableDataOverview.getTableUsageState(), currentRow + 1);
        // 填充领域分类汇总表
        fillDomainClassificationTable(tableOverviewSheet, tableDataOverview.getDomainClassification(), currentRow + 1);
        ExcelStyleUtil.autoFitColumns(tableOverviewSheet, 1, 6);
        // 首列采用固定宽度
        tableOverviewSheet.setColumnWidth(0, 20 * 256);
    }

    private TableDataOverview getTableOverviewData(int totalTableNum, int usedTableNum) {
        TableDataOverview tableDataOverview = new TableDataOverview();
        tableDataOverview.setTotalTableNum(totalTableNum);
        tableDataOverview.setUsedTableNum(usedTableNum);
        tableDataOverview.setUtilizationRate(MathUtils.getQuotient(usedTableNum, totalTableNum));
        tableDataOverview.setTotalDataSize(getTotalDataSize());
        return tableDataOverview;
    }

    private LayerAggregation getLayerAggregation(
            OverviewResponse<TableAmountItem, TableAmountData<TableAmountItem>> tableAmountItem) {
        LayerAggregation layerAggregation = new LayerAggregation();
        TableAmountData<TableAmountItem> allTableNum = tableAmountItem.getAll();
        TableAmountData<TableAmountItem> baselineTableNum = tableAmountItem.getBaseline();
        TableAmountData<TableAmountItem> customTableNum = tableAmountItem.getCustom();
        layerAggregation.setDm(new GroupAmount(allTableNum.getDm().getTableNum(),
                baselineTableNum.getDm().getTableNum(), customTableNum.getDm().getTableNum()));
        layerAggregation.setDwr(new GroupAmount(allTableNum.getDwr().getTableNum(),
                baselineTableNum.getDwr().getTableNum(), customTableNum.getDwr().getTableNum()));
        layerAggregation.setDwi(new GroupAmount(allTableNum.getDwi().getTableNum(),
                baselineTableNum.getDwi().getTableNum(), customTableNum.getDwi().getTableNum()));
        int baselineTotal = baselineTableNum.getDm().getTableNum() + baselineTableNum.getDwr().getTableNum()
                + baselineTableNum.getDwi().getTableNum();
        int customTotal = customTableNum.getDm().getTableNum() + customTableNum.getDwr().getTableNum()
                + customTableNum.getDwi().getTableNum();
        layerAggregation
                .setTotal(new GroupAmount(tableAmountItem.getSummary().getTableNum(), baselineTotal, customTotal));
        return layerAggregation;
    }

    private TableUsageState getTableUsageState(OverviewResponse<UsageData, TableAmountData<UsageData>> usageData) {
        TableUsageState tableUsageState = new TableUsageState();
        TableAmountData<UsageData> allUsageNum = usageData.getAll();
        tableUsageState.setDm(new UsageAmount(allUsageNum.getDm().getUsed(), allUsageNum.getDm().getUnused()));
        tableUsageState.setDwr(new UsageAmount(allUsageNum.getDwr().getUsed(), allUsageNum.getDwr().getUnused()));
        tableUsageState.setDwi(new UsageAmount(allUsageNum.getDwi().getUsed(), allUsageNum.getDwi().getUnused()));
        int usedTotal = allUsageNum.getDm().getUsed() + allUsageNum.getDwr().getUsed() + allUsageNum.getDwi().getUsed();
        int unusedTotal = allUsageNum.getDm().getUnused() + allUsageNum.getDwr().getUnused()
                + allUsageNum.getDwi().getUnused();
        tableUsageState.setTotal(new UsageAmount(usedTotal, unusedTotal));
        return tableUsageState;
    }

    private DomainClassification getDomainClassification(List<TableDetailVo> tableDetailVoList,
            OverviewResponse<TableAmountItem, TableAmountData<TableAmountItem>> tableAmountItem) {
        Map<String, Integer> baselineSchemaDataMap = getSchemaDataMap(tableAmountItem.getBaseline().getDm().getDetail(),
                tableAmountItem.getBaseline().getDwr().getDetail(), tableAmountItem.getBaseline().getDwi().getDetail());
        Map<String, Integer> customSchemaDataMap = getSchemaDataMap(tableAmountItem.getCustom().getDm().getDetail(),
                tableAmountItem.getCustom().getDwr().getDetail(), tableAmountItem.getCustom().getDwi().getDetail());
        List<DomainInfo> dm = new ArrayList<>();
        List<DomainInfo> dwr = new ArrayList<>();
        List<DomainInfo> dwi = new ArrayList<>();
        for (TableDetailVo tableDetailVo : tableDetailVoList) {
            String schemaFullName = tableDetailVo.getSchema();
            String layer = tableDetailVo.getModelLayering();
            String abbreviation = schemaFullName.replaceFirst(layer + "_", "");
            DomainInfo domainInfo = new DomainInfo(layer, tableDetailVo.getDomain(), abbreviation,
                    tableDetailVo.getSource(), baselineSchemaDataMap.getOrDefault(schemaFullName, 0),
                    customSchemaDataMap.getOrDefault(schemaFullName, 0));
            addLayerList(layer, dm, dwr, dwi, domainInfo);
        }
        return new DomainClassification(dm, dwr, dwi);
    }

    private Map<String, Integer> getSchemaDataMap(List<SchemaData> dmSchemaDataList, List<SchemaData> dwrSchemaDataList,
            List<SchemaData> dwiSchemaDataList) {
        List<SchemaData> schemaDataList = new ArrayList<>();
        schemaDataList.addAll(Optional.ofNullable(dmSchemaDataList).orElse(new ArrayList<>()));
        schemaDataList.addAll(Optional.ofNullable(dwrSchemaDataList).orElse(new ArrayList<>()));
        schemaDataList.addAll(Optional.ofNullable(dwiSchemaDataList).orElse(new ArrayList<>()));
        return schemaDataList.stream().collect(Collectors.toMap(SchemaData::getSchemaName, SchemaData::getTableNum));
    }

    private void addLayerList(String layer, List<DomainInfo> dm, List<DomainInfo> dwr, List<DomainInfo> dwi,
            DomainInfo domainInfo) {
        switch (ModelLayerEnum.valueOf(layer.toUpperCase(Locale.ROOT))) {
            case DM:
                dm.add(domainInfo);
                break;
            case DWR:
                dwr.add(domainInfo);
                break;
            case DWI:
                dwi.add(domainInfo);
                break;
            default:
                break;
        }
    }

    private int fillTableDataOverview(XSSFSheet tableOverviewSheet, TableDataOverview tableDataOverview,
            int currentRowNum) {
        // 表数据总览表占1-5行，A-B列
        XSSFRow tableFirstRow = tableOverviewSheet.createRow(currentRowNum);
        // 合并A-B列
        tableOverviewSheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum, 0, 1));
        tableFirstRow.createCell(0)
                .setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_TABLE_DATA_OVERVIEW_TABLE_NAME));
        currentRowNum = currentRowNum + 1;
        TableDataOverviewColumn[] tableDataOverviewColumns = TableDataOverviewColumn.values();
        for (TableDataOverviewColumn tableDataOverviewColumn : tableDataOverviewColumns) {
            String fieldName = tableDataOverviewColumn.value();
            XSSFRow row = tableOverviewSheet.createRow(currentRowNum);
            row.createCell(0).setCellValue(I18nUtils.getMessage(tableDataOverviewColumn.i18nCode()));
            // 每个属性增加一行
            currentRowNum = currentRowNum + 1;
            Class<?> fieldType = ClassUtils.getFieldType(tableDataOverview, fieldName);
            Object value = ClassUtils.getFieldValue(tableDataOverview, fieldName, fieldType);
            if (value instanceof Double) {
                row.createCell(1).setCellValue((Double) value);
            } else if (value instanceof Integer) {
                row.createCell(1).setCellValue((Integer) value);
            } else if (value instanceof Long) {
                row.createCell(1).setCellValue(getTotalRowSizeData((Long) value));
            }
        }
        setTableDataOverviewStyle(tableOverviewSheet.getWorkbook(), tableOverviewSheet, tableFirstRow);
        return currentRowNum;
    }

    private String getTotalRowSizeData(Long rowSize) {
        BigDecimal rowSizeDecimal = new BigDecimal(rowSize);
        long currentSize = rowSize;
        int index = -1;
        double finalNum;
        // 英文单位支持K、M、B、T
        if (Locale.US.equals(LocaleContextHolder.getLocale())) {
            while (currentSize > 0) {
                if (index >= 4) {
                    break;
                }
                currentSize = currentSize / 1000;
                index++;
            }
            finalNum = rowSizeDecimal.divide(BigDecimal.valueOf(Math.pow(1000, index)), 2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
        } else { // 中文单位支持万、亿、万亿
            while (currentSize > 0) {
                if (index >= 3) {
                    break;
                }
                currentSize = currentSize / 10000;
                index++;
            }
            finalNum = rowSizeDecimal.divide(BigDecimal.valueOf(Math.pow(10000, index)), 2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
        }
        // 小于1000或者10000直接返回该整数，不需要单位
        if (index <= 0) {
            return (int) finalNum + "";
        }
        return finalNum + I18nUtils.getMessage(DataSizeUnit.getI18nCodeByValue(index));
    }

    private int fillLayerAggregation(XSSFSheet tableOverviewSheet, LayerAggregation layerAggregation,
            int currentRowNum) {
        // 表数据总览表占7-12行，A-D列
        XSSFRow tableFirstRow = tableOverviewSheet.createRow(currentRowNum);
        // 合并A-D列
        tableOverviewSheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum, 0, 3));
        tableFirstRow.createCell(0)
                .setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_LAYER_AGGREGATION_TABLE_NAME));
        currentRowNum = currentRowNum + 1;
        XSSFRow colNameRow = tableOverviewSheet.createRow(currentRowNum);
        colNameRow.createCell(0).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_LAYER));
        colNameRow.createCell(1).setCellValue(I18nUtils.getMessage(GroupStatisticsEnum.ALL.i18nCode()));
        colNameRow.createCell(2).setCellValue(I18nUtils.getMessage(GroupStatisticsEnum.BASELINE.i18nCode()));
        colNameRow.createCell(3).setCellValue(I18nUtils.getMessage(GroupStatisticsEnum.CUSTOM.i18nCode()));
        currentRowNum = currentRowNum + 1;
        List<XSSFRow> layerRowList = new ArrayList<>();
        LayerAmountColumn[] layerAmountColumns = LayerAmountColumn.values();
        currentRowNum = createLayerRow(layerRowList, tableOverviewSheet, currentRowNum);
        // 分层汇总表的B-D列
        for (int i = 0; i < 4; i++) {
            String fieldName = layerAmountColumns[i].value();
            GroupAmount fieldValue = ClassUtils.getFieldValue(layerAggregation, fieldName, GroupAmount.class);
            XSSFRow tableRow = layerRowList.get(i);
            // 如果值为null，则该行不填充数据
            if (fieldValue == null) {
                continue;
            }
            tableRow.createCell(1).setCellValue(fieldValue.getAll());
            tableRow.createCell(2).setCellValue(fieldValue.getBaseline());
            tableRow.createCell(3).setCellValue(fieldValue.getCustom());
        }
        setLayerAggregationStyle(tableOverviewSheet.getWorkbook(), tableOverviewSheet, tableFirstRow);
        return currentRowNum;
    }

    private int fillUsageStateTable(XSSFSheet tableOverviewSheet, TableUsageState tableUsageState, int currentRowNum) {
        // 表使用状态表占14-19行，A-C列
        XSSFRow tableFirstRow = tableOverviewSheet.createRow(currentRowNum);
        // 合并A-C列
        tableOverviewSheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum, 0, 2));
        tableFirstRow.createCell(0)
                .setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_TABLE_USAGE_STATE_TABLE_NAME));
        currentRowNum = currentRowNum + 1;
        XSSFRow colNameRow = tableOverviewSheet.createRow(currentRowNum);
        colNameRow.createCell(0).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_LAYER));
        colNameRow.createCell(1).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_USED));
        colNameRow.createCell(2).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_UNUSED));
        currentRowNum = currentRowNum + 1;
        List<XSSFRow> layerRowList = new ArrayList<>();
        LayerAmountColumn[] layerAmountColumns = LayerAmountColumn.values();
        currentRowNum = createLayerRow(layerRowList, tableOverviewSheet, currentRowNum);
        // 分层汇总表的B-D列
        for (int i = 0; i < 4; i++) {
            String fieldName = layerAmountColumns[i].value();
            UsageAmount fieldValue = ClassUtils.getFieldValue(tableUsageState, fieldName, UsageAmount.class);
            XSSFRow tableRow = layerRowList.get(i);
            // 如果值为null，则该行不填充数据
            if (fieldValue == null) {
                continue;
            }
            tableRow.createCell(1).setCellValue(fieldValue.getUsed());
            tableRow.createCell(2).setCellValue(fieldValue.getUnused());
        }
        setUsageStateTableStyle(tableOverviewSheet.getWorkbook(), tableOverviewSheet, tableFirstRow);
        return currentRowNum;
    }

    private void fillDomainClassificationTable(XSSFSheet tableOverviewSheet, DomainClassification domainClassification,
            int currentRowNum) {
        // 领域分类汇总表21行开始，A-E列
        XSSFRow tableFirstRow = tableOverviewSheet.createRow(currentRowNum);
        // 合并A-E列
        tableOverviewSheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum, 0, 4));
        tableFirstRow.createCell(0)
                .setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_DOAMIN_CLASSIFICATION_TABLE_NAME));
        currentRowNum = currentRowNum + 1;
        XSSFRow fieldRow = tableOverviewSheet.createRow(currentRowNum);
        for (int i = 0; i < DomainClassificationColumn.values().length; i++) {
            XSSFCell cell = fieldRow.createCell(i);
            cell.setCellValue(I18nUtils.getMessage(DomainClassificationColumn.values()[i].i18nCode()));
        }
        currentRowNum = currentRowNum + 1;
        if (domainClassification == null) {
            return;
        }
        DomainClassificationColumn[] domainClassificationColumns = DomainClassificationColumn.values();
        // 专题库层
        currentRowNum = fillLayerData(tableOverviewSheet, domainClassification.getDm(), domainClassificationColumns,
                currentRowNum);
        // 主题库层
        currentRowNum = fillLayerData(tableOverviewSheet, domainClassification.getDwr(), domainClassificationColumns,
                currentRowNum);
        // 贴源层
        currentRowNum = fillLayerData(tableOverviewSheet, domainClassification.getDwi(), domainClassificationColumns,
                currentRowNum);
        setDomainClassificationStyle(tableOverviewSheet.getWorkbook(), tableOverviewSheet, tableFirstRow,
                currentRowNum);
    }

    private int createLayerRow(List<XSSFRow> layerRowList, XSSFSheet tableSheet, int currentRowNum) {
        for (int i = 0; i < 4; i++) {
            layerRowList.add(tableSheet.createRow(currentRowNum));
            currentRowNum = currentRowNum + 1;
        }
        // 设置表的第一列，分别是专题库、主题库、贴源层、汇总
        layerRowList.get(0).createCell(0).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_DM));
        layerRowList.get(1).createCell(0).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_DWR));
        layerRowList.get(2).createCell(0).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_DWI));
        layerRowList.get(3).createCell(0).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_SUMMARY));
        return currentRowNum;
    }

    private int fillLayerData(XSSFSheet tableOverviewSheet, List<DomainInfo> domainInfoList,
            DomainClassificationColumn[] domainClassificationColumns, int currentRowNum) {
        if (domainInfoList == null || domainInfoList.isEmpty()) {
            return currentRowNum;
        }
        boolean isLayerSet = false;
        for (DomainInfo domainInfo : domainInfoList) {
            XSSFRow fieldRow = tableOverviewSheet.createRow(currentRowNum);
            currentRowNum = currentRowNum + 1;
            if (!isLayerSet) {
                isLayerSet = true;
                String layerValue = ClassUtils.getFieldValue(domainInfo, domainClassificationColumns[0].value(),
                        String.class);
                fieldRow.createCell(0).setCellValue(I18nUtils.getMessage(ModelLayerEnum.getI18nCode(layerValue)));
            }
            for (int i = 1; i < domainClassificationColumns.length; i++) {
                String fieldName = domainClassificationColumns[i].value();
                Class<?> fieldType = ClassUtils.getFieldType(domainInfo, fieldName);
                Object value = ClassUtils.getFieldValue(domainInfo, fieldName, fieldType);
                if (value == null) {
                    fieldRow.createCell(i).setCellValue(I18nUtils.getMessage(ExceptionCode.DATATOOL_NA));
                    continue;
                }
                // 来源字段需要国际化
                if (DomainClassificationColumn.ORIGIN.value().equals(fieldName)) {
                    fieldRow.createCell(i).setCellValue(
                            I18nUtils.getMessage(GroupStatisticsEnum.getI18nCodeByValue(value.toString())));
                } else if (value instanceof Integer) {
                    fieldRow.createCell(i).setCellValue((Integer) value);
                } else {
                    fieldRow.createCell(i).setCellValue(value.toString());
                }
            }
        }
        return currentRowNum;
    }

    private Long getTotalDataSize() {
        DataWareHouse dataWareHouse = dwFactory.create();
        DWStatistics statistics = dataWareHouse.getStatistics();
        DWTableRows dwTableRows = statistics.toCalculate(new DWTableRowStat());
        // 排除不属于 dm、dwr、dwi分层的表
        DWTableRows excluded = dwTableRows.exclude(item -> item.getDwTable().getTier().equals(DWTier.OTHER));
        return excluded.sum();
    }

    private void setTableDataOverviewStyle(XSSFWorkbook workbook, XSSFSheet detailSheet, XSSFRow headRow) {
        ExcelStyleUtil.setHeadMergeRowStyle(workbook, headRow, 2);
        CellStyle dataCellStyle = workbook.createCellStyle();
        ExcelStyleUtil.setFullBorderStyle(dataCellStyle, true, true, true, true);
        ExcelStyleUtil.setFontStyle(workbook, dataCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 1, 4, 2, dataCellStyle);
        XSSFCell utilizationRateCell = detailSheet.getRow(3).getCell(1);
        CellStyle utilizationRateCellStyle = workbook.createCellStyle();
        utilizationRateCellStyle.cloneStyleFrom(utilizationRateCell.getCellStyle());
        // 设置单元格样式位百分比
        utilizationRateCellStyle.setDataFormat(workbook.createDataFormat().getFormat("0%"));
        utilizationRateCell.setCellStyle(utilizationRateCellStyle);
        XSSFCell totalDataSizeCell = detailSheet.getRow(4).getCell(1);
        CellStyle totalDataSizeCellStyle = workbook.createCellStyle();
        totalDataSizeCellStyle.cloneStyleFrom(totalDataSizeCell.getCellStyle());
        // 设置总数据量右对齐
        totalDataSizeCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        totalDataSizeCell.setCellStyle(totalDataSizeCellStyle);
    }

    private void setLayerAggregationStyle(XSSFWorkbook workbook, XSSFSheet detailSheet, XSSFRow headRow) {
        ExcelStyleUtil.setHeadMergeRowStyle(workbook, headRow, 4);
        // 设置分层汇总数据样式
        CellStyle dataCellStyle = workbook.createCellStyle();
        ExcelStyleUtil.setFullBorderStyle(dataCellStyle, true, true, true, true);
        ExcelStyleUtil.setFontStyle(workbook, dataCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 8, 11, 4, dataCellStyle);
        // 设置列名样式
        CellStyle columnCellStyle = workbook.createCellStyle();
        columnCellStyle.cloneStyleFrom(dataCellStyle);
        ExcelStyleUtil.setHeadAndColumnColorStyle(columnCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 7, 7, 4, columnCellStyle);
    }

    private void setUsageStateTableStyle(XSSFWorkbook workbook, XSSFSheet detailSheet, XSSFRow headRow) {
        ExcelStyleUtil.setHeadMergeRowStyle(workbook, headRow, 3);
        // 设置表使用状态数据样式
        CellStyle dataCellStyle = workbook.createCellStyle();
        ExcelStyleUtil.setFullBorderStyle(dataCellStyle, true, true, true, true);
        ExcelStyleUtil.setFontStyle(workbook, dataCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 15, 18, 3, dataCellStyle);
        // 设置列名样式
        CellStyle columnCellStyle = workbook.createCellStyle();
        columnCellStyle.cloneStyleFrom(dataCellStyle);
        ExcelStyleUtil.setHeadAndColumnColorStyle(columnCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 14, 14, 3, columnCellStyle);
    }

    private void setDomainClassificationStyle(XSSFWorkbook workbook, XSSFSheet detailSheet, XSSFRow headRow,
            int endRow) {
        ExcelStyleUtil.setHeadMergeRowStyle(workbook, headRow, 6);
        // 设置领域分类汇总数据样式
        CellStyle dataCellStyle = workbook.createCellStyle();
        ExcelStyleUtil.setFullBorderStyle(dataCellStyle, true, true, true, true);
        ExcelStyleUtil.setFontStyle(workbook, dataCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 22, endRow - 1, 6, dataCellStyle);
        // 设置列名样式
        CellStyle columnCellStyle = workbook.createCellStyle();
        columnCellStyle.cloneStyleFrom(dataCellStyle);
        ExcelStyleUtil.setHeadAndColumnColorStyle(columnCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 21, 21, 6, columnCellStyle);
    }
}