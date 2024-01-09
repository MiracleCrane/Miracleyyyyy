/*
 * 文 件 名:  DWStatistics.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.stat;

import com.huawei.smartcampus.datatool.monitor.domain.dw.DWOperation;
import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnInfo;
import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnector;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.DBType;
import com.huawei.smartcampus.datatool.monitor.domain.factory.DWGatewayFactory;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.DWGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * 数据仓库的统计服务
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/19]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWStatistics {
    private static final Logger LOGGER = LoggerFactory.getLogger(DWStatistics.class);
    private DWConnector dwConnector;

    public DWStatistics(DWConnector dwConnector) {
        this.dwConnector = dwConnector;
    }

    public <T> T toCalculate(DWStatisticsable<T> stat) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Start to calculate: {}", stat.getClass().getSimpleName());
        }

        canOperater(stat, dwConnector);
        return stat.calculate(dwConnector);
    }

    private void canOperater(DWOperation dwOperation, DWConnector dwConnector) {
        DWConnInfo dwConnInfo = dwConnector.getDwConnInfo();
        DBType dbType = DBType.valueOf(dwConnInfo.getType().toUpperCase(Locale.ROOT));
        DWGateway dwGateway = DWGatewayFactory.create(dbType);
        dwOperation.setDWGateway(dwGateway);
    }
}