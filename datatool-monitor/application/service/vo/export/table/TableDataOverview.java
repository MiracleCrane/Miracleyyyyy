/*
 * 文 件 名:  TableDataOverview.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/27
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export.table;

/**
 * 表数据总览
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/27]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class TableDataOverview {
    private Integer totalTableNum;
    private Integer usedTableNum;
    private Double utilizationRate;
    private Long totalDataSize;

    public Integer getTotalTableNum() {
        return totalTableNum;
    }

    public void setTotalTableNum(Integer totalTableNum) {
        this.totalTableNum = totalTableNum;
    }

    public Integer getUsedTableNum() {
        return usedTableNum;
    }

    public void setUsedTableNum(Integer usedTableNum) {
        this.usedTableNum = usedTableNum;
    }

    public Double getUtilizationRate() {
        return utilizationRate;
    }

    public void setUtilizationRate(Double utilizationRate) {
        this.utilizationRate = utilizationRate;
    }

    public Long getTotalDataSize() {
        return totalDataSize;
    }

    public void setTotalDataSize(Long totalDataSize) {
        this.totalDataSize = totalDataSize;
    }
}