/*
 * 文 件 名:  AssetStatisticsStrategy.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/10/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.gateway;

/**
 * 资产概览统计策略
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/23]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public interface AssetStatisticsStrategy {
    int countNum();

    int countStateNum(boolean state);
}