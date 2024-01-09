/*
 * 文 件 名:  DWTable.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw;

/**
 * 数仓的表模型
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/16]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWTable {
    /*
     * 表名
     */
    private String name;

    /*
     * 表的schema
     * 这个属性有个问题是目前pg数据是有schema的概念的，如果是其他的数仓可能没有这个属性，这个在模型是不稳定点
     */
    private DWSchema schema;

    public DWTier getTier() {
        return schema.getTier();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DWSchema getSchema() {
        return schema;
    }

    public void setSchema(DWSchema schema) {
        this.schema = schema;
    }

    public static DWTable from(String tableName, DWSchema schema) {
        DWTable table = new DWTable();
        table.setSchema(schema);
        table.setName(tableName);
        return table;
    }
}