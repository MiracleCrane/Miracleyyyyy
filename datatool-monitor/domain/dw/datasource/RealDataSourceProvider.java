/*
 * 文 件 名:  RealDataSourceProvider.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.datasource;

import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnInfo;

import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 真实数据源提供者
 * 通过这个系统扩展点，可以设置其他数据源
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/27]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public interface RealDataSourceProvider {
    DataSource createDataSource(DWConnInfo connInfo) throws SQLException;
}