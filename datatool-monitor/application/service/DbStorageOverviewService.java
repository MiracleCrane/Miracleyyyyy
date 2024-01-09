/*
 * 文 件 名:  DbStorageOverviewService.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service;

import com.huawei.smartcampus.datatool.monitor.application.service.vo.dw.DBStorage;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.dw.TableRowNumOrder;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.dw.TableStorageOrder;
import com.huawei.smartcampus.datatool.monitor.common.util.StorageFormatterUtil;
import com.huawei.smartcampus.datatool.monitor.domain.dw.DWTableRow;
import com.huawei.smartcampus.datatool.monitor.domain.dw.DWTableSize;
import com.huawei.smartcampus.datatool.monitor.domain.dw.DWTier;
import com.huawei.smartcampus.datatool.monitor.domain.dw.DataWareHouse;
import com.huawei.smartcampus.datatool.monitor.domain.dw.stat.DWStatistics;
import com.huawei.smartcampus.datatool.monitor.domain.dw.stat.DWStorage;
import com.huawei.smartcampus.datatool.monitor.domain.dw.stat.DWStorageStat;
import com.huawei.smartcampus.datatool.monitor.domain.dw.stat.DWTableRowStat;
import com.huawei.smartcampus.datatool.monitor.domain.dw.stat.DWTableRows;
import com.huawei.smartcampus.datatool.monitor.domain.dw.stat.DWTableStorage;
import com.huawei.smartcampus.datatool.monitor.domain.dw.stat.DWTableStorageStat;
import com.huawei.smartcampus.datatool.monitor.domain.factory.DWFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据存储概览服务
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
@Service
public class DbStorageOverviewService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbStorageOverviewService.class);

    @Autowired
    private DWFactory dwFactory;

    /**
     * 查询数据库的存储信息
     * 
     * @return 查询结果
     */
    public DBStorage queryDBStorage() {
        DataWareHouse dataWareHouse = dwFactory.create();
        DWStatistics statistics = dataWareHouse.getStatistics();
        // 获取数仓存储信息
        DWStorage dwStorage = statistics.toCalculate(new DWStorageStat());
        // 最大存储空间
        Long maximumSize = dwStorage.getMaximumSize();
        // 已使用存储
        Long usedSize = dwStorage.getUsedSize();
        // 空闲存储空间 maximumSize和 usedSize 可能返回null，要特殊处理
        Long idleSize = (maximumSize == null || usedSize == null) ? null : maximumSize - usedSize;
        // 计算分层存储空间
        DWTableStorage dwTableStorage = statistics.toCalculate(new DWTableStorageStat());
        Map<DWTier, DWTableStorage> group = dwTableStorage.groupByTier();
        long dwiSize = group.get(DWTier.DWI).sum();
        long dwrSize = group.get(DWTier.DWR).sum();
        long dmSize = group.get(DWTier.DM).sum();

        // 组装返回对象
        DBStorage dbStorage = new DBStorage();
        dbStorage.setMaximumSize(processSize(maximumSize));
        dbStorage.setUsedSize(processSize(usedSize));
        dbStorage.setIdleSize(processSize(idleSize));
        dbStorage.setDwiUsedSize(processSize(dwiSize));
        dbStorage.setDwrUsedSize(processSize(dwrSize));
        dbStorage.setDmUsedSize(processSize(dmSize));
        return dbStorage;
    }

    /**
     * 查询表行数 top5
     * 
     * @return 排序结果
     */
    public TableRowNumOrder queryTableRowNumOrder() {
        DataWareHouse dataWareHouse = dwFactory.create();
        DWStatistics statistics = dataWareHouse.getStatistics();
        DWTableRows dwTableRows = statistics.toCalculate(new DWTableRowStat());
        // 排除不属于 dm、dwr、dwi分层的表
        DWTableRows excluded = dwTableRows.exclude(item -> item.getDwTable().getTier().equals(DWTier.OTHER));
        // 排序取结果
        DWTableRows limit = excluded.sortDesc().limit(5);

        List<TableRowNumOrder.RowNum> top = new ArrayList<>();
        for (DWTableRow dwTableRow : limit.getData()) {
            TableRowNumOrder.RowNum item = new TableRowNumOrder.RowNum();
            item.setTableName(dwTableRow.getDwTable().getName());
            item.setRowNum(dwTableRow.getRowNum());
            top.add(item);
        }
        TableRowNumOrder result = new TableRowNumOrder();
        result.setTop(top);
        return result;
    }

    /**
     * 查询表存储 top5
     *
     * @return 排序结果
     */
    public TableStorageOrder queryTableStorageOrder() {
        DataWareHouse dataWareHouse = dwFactory.create();
        DWStatistics statistics = dataWareHouse.getStatistics();
        DWTableStorage dwTableStorage = statistics.toCalculate(new DWTableStorageStat());
        // 排除不属于 dm、dwr、dwi分层的表
        DWTableStorage excluded = dwTableStorage.exclude(item -> item.getDwTable().getTier().equals(DWTier.OTHER));
        // 排序取结果
        DWTableStorage limit = excluded.sortDesc().limit(5);

        List<TableStorageOrder.TableSize> top = new ArrayList<>();
        for (DWTableSize dwTableSize : limit.getData()) {
            TableStorageOrder.TableSize item = new TableStorageOrder.TableSize();
            item.setTableName(dwTableSize.getDwTable().getName());
            item.setSize(dwTableSize.getSize());
            top.add(item);
        }
        TableStorageOrder result = new TableStorageOrder();
        result.setTop(top);
        return result;
    }

    private String processSize(Long size) {
        if (size == null) {
            return "--";
        }
        Float toMB = StorageFormatterUtil.bytesToMB(size);
        Float format = StorageFormatterUtil.formatNum(toMB);
        return String.valueOf(format);
    }
}