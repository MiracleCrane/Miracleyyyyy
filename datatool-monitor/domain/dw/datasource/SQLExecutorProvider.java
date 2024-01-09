/*
 * 文 件 名:  SQLExecutorProvider.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.datasource;

import java.util.List;

import javax.sql.DataSource;

/**
 * 提供真正的SQL的执行能力，接口限定只能执行query方法
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/29]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public interface SQLExecutorProvider {
    /**
     * 方法封装了SQL执行的底层逻辑
     *
     * @param dataSource 数据源
     * @param querySql sql语句
     * @param params 参数
     * @return 查询结果集
     */
    DWRecordSet executeQuery(DataSource dataSource, String querySql, List<Object> params);
}