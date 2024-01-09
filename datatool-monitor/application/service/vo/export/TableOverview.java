/*
 * 文 件 名:  TableOverview.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/27
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export;

import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.table.DomainClassification;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.table.LayerAggregation;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.table.TableDataOverview;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.table.TableUsageState;

/**
 * 表概览
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/27]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class TableOverview {
    private TableDataOverview tableDataOverview;
    private LayerAggregation layerAggregation;
    private TableUsageState tableUsageState;
    private DomainClassification domainClassification;

    public TableDataOverview getTableDataOverview() {
        return tableDataOverview;
    }

    public void setTableDataOverview(TableDataOverview tableDataOverview) {
        this.tableDataOverview = tableDataOverview;
    }

    public LayerAggregation getLayerAggregation() {
        return layerAggregation;
    }

    public void setLayerAggregation(LayerAggregation layerAggregation) {
        this.layerAggregation = layerAggregation;
    }

    public TableUsageState getTableUsageState() {
        return tableUsageState;
    }

    public void setTableUsageState(TableUsageState tableUsageState) {
        this.tableUsageState = tableUsageState;
    }

    public DomainClassification getDomainClassification() {
        return domainClassification;
    }

    public void setDomainClassification(DomainClassification domainClassification) {
        this.domainClassification = domainClassification;
    }
}