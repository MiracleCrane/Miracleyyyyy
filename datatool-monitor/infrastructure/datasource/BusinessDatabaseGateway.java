/*
 * 文 件 名:  BusinessDatabaseGateway.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/14
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.datasource;

import com.huawei.smartcampus.datatool.entity.DtConnectionEntity;

import java.sql.Connection;

/**
 * 业务库访问接口
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/11/14]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public interface BusinessDatabaseGateway {
    Connection getConnection(DtConnectionEntity connectionEntity, String database);

    void shutdown(Connection conn);
}