/*
 * 文 件 名:  DWStatisticsable.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.stat;

import com.huawei.smartcampus.datatool.monitor.domain.dw.DWOperation;
import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnector;

/**
 * 数据仓库提供统一的统计信息接口，不同统计数据需要创建各自的实现
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/19]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public interface DWStatisticsable<T> extends DWOperation {
    /**
     * 执行统计计算
     * 
     * @param dwConnector 数仓的连接器
     * @return 执行结果
     */
    T calculate(DWConnector dwConnector);
}