/*
 * 文 件 名:  DWTableRows.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.stat;

import com.huawei.smartcampus.datatool.monitor.domain.dw.DWTableRow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表的行数据统计结果
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/20]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWTableRows {
    private List<DWTableRow> dwTableRows = new ArrayList<>();

    public DWTableRows(List<DWTableRow> dwTableRows) {
        this.dwTableRows.addAll(dwTableRows);
    }

    public List<DWTableRow> getData() {
        return dwTableRows;
    }

    /**
     * 所有表的行数求和
     *
     * @return 总表行数
     */
    public long sum() {
        return dwTableRows.stream().mapToLong(DWTableRow::getRowNum).sum();
    }

    /**
     * 按表的行数降序排序
     *
     * @return 排序结果
     */
    public DWTableRows sortDesc() {
        // 先按照表行数排序，大小相同再按照tablename排序
        List<DWTableRow> sorted = dwTableRows.stream().sorted(Comparator.comparingLong(DWTableRow::getRowNum)
                .thenComparing(item -> item.getDwTable().getName()).reversed()).collect(Collectors.toList());
        return new DWTableRows(sorted);
    }

    /**
     * 获取前topN个元素数据
     *
     * @param topN 要返回前多少个元素
     * @return 前topN个元素
     */
    public DWTableRows limit(int topN) {
        List<DWTableRow> limit = dwTableRows.stream().limit(topN).collect(Collectors.toList());
        return new DWTableRows(limit);
    }

    /**
     * 排除某些符合条件的表
     *
     * @param filter 过滤器
     * @return 排除后的结果
     */
    public DWTableRows exclude(Filter<DWTableRow> filter) {
        List<DWTableRow> afterFilter = new ArrayList<>();
        for (DWTableRow item : dwTableRows) {
            if (filter.exclude(item)) {
                continue;
            }
            afterFilter.add(item);
        }
        return new DWTableRows(afterFilter);
    }
}