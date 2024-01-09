/*
 * 文 件 名:  DWConnection.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.datasource;

import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnInfo;

import java.util.List;

import javax.sql.DataSource;

/**
 * 提供数据库连接执行sql操作
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/25]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWConnection {
    private SQLExecutorProvider provider;

    private DataSource realDataSource;

    private DWConnInfo dwConnInfo;

    public DWConnection(DWConnInfo dwConnInfo, DataSource realDataSource, SQLExecutorProvider provider) {
        this.provider = provider;
        this.realDataSource = realDataSource;
        this.dwConnInfo = dwConnInfo;
    }

    /**
     * 只提供数据仓库的查询方法
     *
     * @param querySql 查询sql
     * @param params sql参数
     * @return 查询结果
     */
    public DWRecordSet executeQuery(String querySql, List<Object> params) {
        return provider.executeQuery(realDataSource, querySql, params);
    }

    public DWConnInfo getDwConnInfo() {
        return dwConnInfo;
    }
}