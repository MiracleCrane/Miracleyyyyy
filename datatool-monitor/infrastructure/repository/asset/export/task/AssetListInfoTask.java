/*
 * 文 件 名:  AssetListInfoTask.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.task;

import com.huawei.smartcampus.datatool.entity.DtConnectionEntity;
import com.huawei.smartcampus.datatool.enums.OverviewDbKeysEnum;
import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.i18n.I18nUtils;
import com.huawei.smartcampus.datatool.monitor.application.service.ExportTask;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.AssetListInfo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.ExportI18nCode;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.SysConfigGateWay;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.ExcelStyleUtil;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export.column.AssetListInfoColumn;
import com.huawei.smartcampus.datatool.utils.ClassUtils;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;
import com.huawei.smartcampus.datatool.utils.TimeUtil;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 资产清单信息任务
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class AssetListInfoTask implements ExportTask<AssetListInfo> {
    private final SysConfigGateWay sysConfigGateWay = SpringContextHelper.getBean(SysConfigGateWay.class);

    @Override
    public AssetListInfo call() {
        String database = sysConfigGateWay.getConfig(SysConfigNamesEnum.OVERVIEW_DB.value(),
                OverviewDbKeysEnum.DATABASE.value(), String.class);
        // 查询配置是否存在
        if (database == null) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_CONFIG_OVERVIEW_MISSING);
        }
        String connId = sysConfigGateWay.getConfig(SysConfigNamesEnum.OVERVIEW_DB.value(),
                OverviewDbKeysEnum.CONNID.value(), String.class);
        // 查询数据连接是否存在
        if (connId == null) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_CONFIG_OVERVIEW_MISSING);
        }
        // 没有查到数据连接信息。
        DtConnectionEntity connEntity = sysConfigGateWay.getConnInfo(connId);
        if (connEntity == null) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECTION_NOT_EXIST);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(TimeUtil.COMMON_DATE_FORMAT);
        String exportDate = sdf.format(new Date());
        return new AssetListInfo(connEntity.getName(), database, exportDate);
    }

    @Override
    public void fillSheetData(XSSFWorkbook workbook, Object data) {
        if (!(data instanceof AssetListInfo)) {
            return;
        }
        AssetListInfo assetListInfo = (AssetListInfo) data;
        XSSFSheet detailSheet = workbook
                .createSheet(I18nUtils.getMessage(ExportI18nCode.DATATOOL_ASSET_LIST_INFO_SHEET_NAME));
        XSSFRow firstRow = detailSheet.createRow(0);
        // 表头占2列
        detailSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
        firstRow.createCell(0).setCellValue(I18nUtils.getMessage(ExportI18nCode.DATATOOL_ASSET_LIST_INFO_TABLE_NAME));
        AssetListInfoColumn[] assetListInfoColumns = AssetListInfoColumn.values();
        for (int i = 0; i < assetListInfoColumns.length; i++) {
            String fieldName = assetListInfoColumns[i].value();
            XSSFRow row = detailSheet.createRow(i + 1);
            row.createCell(0).setCellValue(I18nUtils.getMessage(AssetListInfoColumn.getI18nCodeByValue(fieldName)));
            row.createCell(1).setCellValue(ClassUtils.getFieldValue(assetListInfo, fieldName, String.class));
        }
        // 设置样式
        setStyle(workbook, detailSheet, firstRow);
    }

    private void setStyle(XSSFWorkbook workbook, XSSFSheet detailSheet, XSSFRow headRow) {
        CellStyle headLeftCellStyle = workbook.createCellStyle();
        ExcelStyleUtil.setFontStyle(workbook, headLeftCellStyle);
        ExcelStyleUtil.setHeadAndColumnColorStyle(headLeftCellStyle);
        // 设置合并单元格的左侧单元格样式
        ExcelStyleUtil.setFullBorderStyle(headLeftCellStyle, true, false, true, true);
        headRow.getCell(0).setCellStyle(headLeftCellStyle);
        CellStyle headRightCellStyle = workbook.createCellStyle();
        headRightCellStyle.cloneStyleFrom(headLeftCellStyle);
        // 设置合并单元格的右侧单元格样式
        ExcelStyleUtil.setFullBorderStyle(headRightCellStyle, false, true, true, true);
        headRow.createCell(1).setCellStyle(headRightCellStyle);
        CellStyle dataCellStyle = workbook.createCellStyle();
        ExcelStyleUtil.setFullBorderStyle(dataCellStyle, true, true, true, true);
        ExcelStyleUtil.setFontStyle(workbook, dataCellStyle);
        ExcelStyleUtil.applyAreaCellStyle(detailSheet, 1, 3, 2, dataCellStyle);
        ExcelStyleUtil.autoFitColumns(detailSheet, 0, 1);
    }
}