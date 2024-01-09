/*
 * 文 件 名:  TableStorageOrder.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/24
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.dw;

import java.util.ArrayList;
import java.util.List;

/**
 * 表存储排序结果
 * 
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/24]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class TableStorageOrder {
    private List<TableSize> top = new ArrayList<>();

    public List<TableSize> getTop() {
        return top;
    }

    public void setTop(List<TableSize> top) {
        this.top = top;
    }

    public static class TableSize {
        private String tableName;

        private long size;

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }
}