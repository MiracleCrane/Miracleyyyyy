/*
 * 文 件 名:  TableRowNum.java
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
 * 表排序结果
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/24]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class TableRowNumOrder {
    private List<RowNum> top = new ArrayList<>();

    public List<RowNum> getTop() {
        return top;
    }

    public void setTop(List<RowNum> top) {
        this.top = top;
    }

    public static class RowNum {
        private String tableName;

        private long rowNum;

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public long getRowNum() {
            return rowNum;
        }

        public void setRowNum(long rowNum) {
            this.rowNum = rowNum;
        }
    }
}