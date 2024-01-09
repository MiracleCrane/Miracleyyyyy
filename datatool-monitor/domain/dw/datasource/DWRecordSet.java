/*
 * 文 件 名:  DWRecordSet.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库查询结果集
 * 简单封装了必要方法，后续按需扩展
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/28]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWRecordSet {
    /*
     * 存放数据查询结果
     * Map<列名，列值>
     */
    private List<Map<String, Object>> records = new ArrayList<>();

    private int rowIndex = -1;

    public DWRecordSet(List<Map<String, Object>> records) {
        this.records.addAll(records);
    }

    public int rowCount() {
        return records.size();
    }

    public boolean hasNext() {
        if (rowIndex < records.size() - 1) {
            return true;
        }
        return false;
    }

    public void next() {
        if (rowIndex < records.size() - 1) {
            rowIndex++;
        }
    }

    public Object getColumnsValue(String colName) {
        Map<String, Object> row = records.get(rowIndex);
        return row.get(colName);
    }
}