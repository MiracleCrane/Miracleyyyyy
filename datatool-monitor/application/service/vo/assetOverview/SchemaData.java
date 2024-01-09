/*
 * 文 件 名:  TableAmountSummaryVo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview;

/**
 * 表数量查询响应-概览
 * 
 * @author s30009470
 * @version [Campus Core 23.1, 2023/11/2]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class SchemaData {
    private String schemaName;
    private int tableNum;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }
}