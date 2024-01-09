/*
 * 文 件 名:  BusinessDbInfoGateway.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.gateway;

import com.huawei.smartcampus.datatool.monitor.domain.overview.TableDetailVo;

import java.sql.Connection;
import java.util.List;

/**
 * 获取数据库信息接口
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/11/16]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public interface BusinessDbInfoGateway {
    List<TableDetailVo> getAllTableSchema();

    List<TableDetailVo> getTableDetail();

    void processTableState(List<TableDetailVo> details);

    Connection getConnBySysConfig();

    void recordRealTimeTableRecord();
}