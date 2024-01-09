/*
 * 文 件 名:  AssetOverviewService.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service;

import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.JobAmountResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.JobStateAmountResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.OverviewResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.TableAmountData;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.TableAmountItem;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview.UsageData;

import javax.servlet.http.HttpServletResponse;

/**
 * 资产概览服务
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/19]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public interface AssetOverviewService {
    JobAmountResponse queryJobAmount();

    JobStateAmountResponse queryJobState();

    OverviewResponse<TableAmountItem, TableAmountData<TableAmountItem>> queryTableAmount();

    OverviewResponse<UsageData, TableAmountData<UsageData>> queryTableState();

    void exportList(HttpServletResponse response);
}