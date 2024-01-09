/*
 * 文 件 名:  DWTableStorage.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.stat;

import com.huawei.smartcampus.datatool.monitor.domain.dw.DWTableSize;
import com.huawei.smartcampus.datatool.monitor.domain.dw.DWTier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 表的存储大小统计信息
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/19]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWTableStorage {
    private List<DWTableSize> dwTablesSizes = new ArrayList<>();

    public DWTableStorage(List<DWTableSize> dwTablesSizes) {
        this.dwTablesSizes.addAll(dwTablesSizes);
    }

    /**
     * 返回原始统计信息
     * 
     * @return 所有表大小信息
     */
    public List<DWTableSize> getData() {
        return dwTablesSizes;
    }

    /**
     * 按数仓的分层汇总
     *
     * @return 分层汇总结果
     */
    public Map<DWTier, DWTableStorage> groupByTier() {
        Map<DWTier, List<DWTableSize>> grouped = dwTablesSizes.stream()
                .collect(Collectors.groupingBy(item -> item.getDwTable().getTier()));

        Map<DWTier, DWTableStorage> result = new HashMap<>();
        for (DWTier tier : DWTier.values()) {
            List<DWTableSize> dwTableSizeList = grouped.getOrDefault(tier, new ArrayList<>());
            result.put(tier, new DWTableStorage(dwTableSizeList));
        }
        return result;
    }

    /**
     * 所有表的大小求和
     * 
     * @return 表大小之和
     */
    public long sum() {
        return dwTablesSizes.stream().mapToLong(DWTableSize::getSize).sum();
    }

    /**
     * 按表的大小降序排序
     * 
     * @return 排序结果
     */
    public DWTableStorage sortDesc() {
        // 先按照表大小排序，大小相同再按照tablename排序
        List<DWTableSize> sorted = dwTablesSizes.stream().sorted(Comparator.comparingLong(DWTableSize::getSize)
                .thenComparing(item -> item.getDwTable().getName()).reversed()).collect(Collectors.toList());
        return new DWTableStorage(sorted);
    }

    /**
     * 获取前topN个元素数据
     * 
     * @param topN 要返回前多少个元素
     * @return 前topN个元素
     */
    public DWTableStorage limit(int topN) {
        List<DWTableSize> limit = dwTablesSizes.stream().limit(topN).collect(Collectors.toList());
        return new DWTableStorage(limit);
    }

    /**
     * 排除某些符合条件的表
     *
     * @param filter 过滤器
     * @return 排除后的结果
     */
    public DWTableStorage exclude(Filter<DWTableSize> filter) {
        List<DWTableSize> afterFilter = new ArrayList<>();
        for (DWTableSize item : dwTablesSizes) {
            if (filter.exclude(item)) {
                continue;
            }
            afterFilter.add(item);
        }
        return new DWTableStorage(afterFilter);
    }
}