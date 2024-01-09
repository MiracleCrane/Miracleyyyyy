/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.datasource;

import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnInfo;

import java.sql.SQLException;

/**
 * 数据源的持有者，提供和维护数据源。
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/12/4]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public interface DWDataSourceHolder {
    /**
     * 创建数据源，放入缓存池
     *
     * @param connInfo 数据连接信息
     * @param provider 提供真水数据源
     * @return 创建结果
     * @throws SQLException sql异常
     */
    boolean createDataSource(DWConnInfo connInfo, RealDataSourceProvider provider) throws SQLException;

    /**
     * 关闭数据源，并从缓存池移除数据源
     *
     * @param connInfo 数据连接信息
     */
    void destroyDataSource(DWConnInfo connInfo);

    /**
     * 查询数据源是否已经存在
     *
     * @param connInfo 数据连接信息
     * @return true/false
     */
    boolean isDataSourceExists(DWConnInfo connInfo);

    /**
     * 返回数据源
     *
     * @param connInfo 连接信息
     * @return 相应数据源
     */
    DWDataSource getDataSource(DWConnInfo connInfo);
}