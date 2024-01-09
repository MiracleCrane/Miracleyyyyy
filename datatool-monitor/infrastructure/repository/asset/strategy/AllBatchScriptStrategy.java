/*
 * 文 件 名:  AllBatchScriptStrategy.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/17
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.strategy;

import com.huawei.smartcampus.datatool.repository.DtScriptRepository;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

/**
 * 全量脚本策略
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class AllBatchScriptStrategy implements AllStatisticsStrategy {
    private final DtScriptRepository dtScriptRepository = SpringContextHelper.getBean(DtScriptRepository.class);

    @Override
    public int countNum() {
        return (int) dtScriptRepository.count();
    }

    @Override
    public int countStateNum(boolean state) {
        // 脚本状态数量不统计，直接返回0即可
        return 0;
    }
}