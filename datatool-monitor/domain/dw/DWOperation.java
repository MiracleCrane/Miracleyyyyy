/*
 * 文 件 名:  DWOperation.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw;

import com.huawei.smartcampus.datatool.monitor.domain.gateway.DWGateway;

/**
 * 可以连接数据仓库接口
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/19]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public interface DWOperation {
    /**
     * 设置数仓的连机器
     * 
     * @param dwGateway 访问数仓的接口
     */
    void setDWGateway(DWGateway dwGateway);
}