/*
 * 文 件 名:  TableAmountItem.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview;

import java.util.List;

/**
 * 资产数量汇总
 * 
 * @author s30009470
 * @version [Campus Core 23.1, 2023/11/2]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class TableAmountItem {
    private int schemaNum;
    private int tableNum;

    private List<SchemaData> detail;

    public int getSchemaNum() {
        return schemaNum;
    }

    public void setSchemaNum(int schemaNum) {
        this.schemaNum = schemaNum;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }

    public List<SchemaData> getDetail() {
        return detail;
    }

    public void setDetail(List<SchemaData> detail) {
        this.detail = detail;
    }

    public TableAmountItem(int schemaNum, int tableNum) {
        this.schemaNum = schemaNum;
        this.tableNum = tableNum;
    }

    public TableAmountItem() {
    }
}