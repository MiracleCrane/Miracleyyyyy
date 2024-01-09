/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw;

/**
 * 单表存储大小信息
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/19]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWTableSize {
    private DWTable dwTable;

    /*
     * 单位：Byte
     */
    private long size;

    public DWTable getDwTable() {
        return dwTable;
    }

    public void setDwTable(DWTable dwTable) {
        this.dwTable = dwTable;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public static DWTableSize from(DWTable table, long size) {
        DWTableSize tableSize = new DWTableSize();
        tableSize.setSize(size);
        tableSize.setDwTable(table);
        return tableSize;
    }
}