/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw;

/**
 * 数据仓库整体存储空间信息
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/20]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWTableRow {
    private DWTable dwTable;

    private long rowNum;

    public long getRowNum() {
        return rowNum;
    }

    public void setRowNum(long rowNum) {
        this.rowNum = rowNum;
    }

    public DWTable getDwTable() {
        return dwTable;
    }

    public void setDwTable(DWTable dwTable) {
        this.dwTable = dwTable;
    }

    public static DWTableRow from(DWTable table, long rowNum) {
        DWTableRow tableRow = new DWTableRow();
        tableRow.setDwTable(table);
        tableRow.setRowNum(rowNum);
        return tableRow;
    }
}