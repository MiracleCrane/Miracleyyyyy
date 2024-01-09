/*
 * 文 件 名:  ExcelStyleUtil.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/22
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.export;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * excel样式工具类
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/22]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public final class ExcelStyleUtil {
    /**
     * 设置边框样式
     *
     * @param cellStyle 单元格样式
     * @param left 左边框
     * @param right 有边框
     * @param bottom 下边框
     * @param top 上边框
     */
    public static void setFullBorderStyle(CellStyle cellStyle, boolean left, boolean right, boolean bottom,
            boolean top) {
        cellStyle.setBorderBottom(bottom ? BorderStyle.THIN : BorderStyle.NONE);
        cellStyle.setBorderLeft(left ? BorderStyle.THIN : BorderStyle.NONE);
        cellStyle.setBorderRight(right ? BorderStyle.THIN : BorderStyle.NONE);
        cellStyle.setBorderTop(top ? BorderStyle.THIN : BorderStyle.NONE);
    }

    /**
     * 设置表头和列样式
     *
     * @param cellStyle 单元格样式
     */
    public static void setHeadAndColumnColorStyle(CellStyle cellStyle) {
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
    }

    /**
     * 设置字体样式
     *
     * @param workbook 工作簿
     * @param cellStyle 单元格样式
     */
    public static void setFontStyle(Workbook workbook, CellStyle cellStyle) {
        Font font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 11);
        cellStyle.setFont(font);
    }

    /**
     * 区域内应用单元格格式
     *
     * @param sheet 工作簿
     * @param startRow 开始行
     * @param endRow 结束行
     * @param colLength 列长度
     * @param cellStyle 需要应用的单元格格式
     */
    public static void applyAreaCellStyle(XSSFSheet sheet, int startRow, int endRow, int colLength,
            CellStyle cellStyle) {
        for (int i = startRow; i <= endRow; i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) {
                row = sheet.createRow(i);
            }
            for (int j = 0; j < colLength; j++) {
                XSSFCell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                }
                cell.setCellStyle(cellStyle);
            }
        }
    }

    /**
     * 自动适应列单元格大小
     *
     * @param sheet 工作表
     * @param startCol 开始列
     * @param endCol 结束列
     */
    public static void autoFitColumns(XSSFSheet sheet, int startCol, int endCol) {
        for (int i = startCol; i <= endCol; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * 设置表头合并单元格的样式
     *
     * @param workbook 工作簿
     * @param headRow 表头行
     * @param colLength 列长度
     */
    public static void setHeadMergeRowStyle(XSSFWorkbook workbook, XSSFRow headRow, int colLength) {
        CellStyle headCommonCellStyle = workbook.createCellStyle();
        setFontStyle(workbook, headCommonCellStyle);
        setHeadAndColumnColorStyle(headCommonCellStyle);
        for (int i = 0; i < colLength; i++) {
            CellStyle headCellStyle = workbook.createCellStyle();
            headCellStyle.cloneStyleFrom(headCommonCellStyle);
            if (i == 0) { // 设置最左侧单元格样式
                setFullBorderStyle(headCellStyle, true, false, true, true);
                headRow.getCell(i).setCellStyle(headCellStyle);
            } else if (i == colLength - 1) { // 设置最右侧单元格样式
                setFullBorderStyle(headCellStyle, false, true, true, true);
                headRow.createCell(i).setCellStyle(headCellStyle);
            } else { // 设置中部单元格样式
                setFullBorderStyle(headCellStyle, false, false, true, true);
                headRow.createCell(i).setCellStyle(headCellStyle);
            }
        }
    }
}