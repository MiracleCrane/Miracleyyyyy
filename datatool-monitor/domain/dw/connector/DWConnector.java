/*
 * 文 件 名:  DWConnector.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.connector;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.DWConnection;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.DWDataSourceHolder;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.DefaultSQLExecutorProvider;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.DruidDataSourceProvider;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.RealDataSourceProvider;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.SQLExecutorProvider;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.SysConfigGateWay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * 数据仓库的数据库连接器，用于提供连接数据仓库的connection
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/19]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(DWConnector.class);

    private SysConfigGateWay sysConfigGateway;

    /*
     * 提供默认的druid实现
     */
    private RealDataSourceProvider realDataSourceProvider = new DruidDataSourceProvider();

    /*
     * 提供默认的jdbc的sql操作实现
     */
    private SQLExecutorProvider sqlExecutorProvider = new DefaultSQLExecutorProvider();

    private DWDataSourceHolder sourceHolder;

    public DWConnector(SysConfigGateWay sysConfigGateway, DWDataSourceHolder sourceHolder) {
        this.sysConfigGateway = sysConfigGateway;
        this.sourceHolder = sourceHolder;
    }

    /**
     * 数据库的连接信息、概览配置的数据库信息都随时可能变更，该方法实时获取最新连接信息
     * 返回的对象经过脱敏，不会返回数据库用户的密码信息
     *
     * @return 数据库连接信息
     */
    public DWConnInfo getDwConnInfo() {
        return innerQueryConnInfo().desensitization();
    }

    private DWConnInfo innerQueryConnInfo() {
        DWConnInfo newConn = sysConfigGateway.getMonitorDBInfo();

        // 没有取到connId和database，说明没有配置要监控的数据仓库，需要抛出异常
        if (newConn == null || newConn.getConnId() == null || newConn.getDatabase() == null) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_CONFIG_OVERVIEW_MISSING);
        }

        // 如果有connId，但是根据connId没查到connName，说明数据连接被删除了，需要抛出异常
        if (newConn.getConnName() == null) {
            // 先销毁数据源
            sourceHolder.destroyDataSource(newConn);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECTION_NOT_EXIST);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("query dwconninfo is: {}", newConn);
        }
        // 在导入的场景下，数据连接的密码和host等信息是允许为空的，这里就不进行连接可用性检查了，name是必填的就行
        return newConn;
    }

    /**
     * 获取数据连接
     * 在获取连接前，会检查监控的数据仓库是否更改了，及时刷新连接信息
     * 以下两种情况下，监控的数仓可能变更
     * 数据连接信息可能是会被修改的，所以需要考虑连接信息的刷新
     * 监控概览里的数据库也可能会修改，需要实时获取刷新
     *
     * @return DWConnection
     */
    public DWConnection getDWConnection() {
        DWConnInfo connInfo = innerQueryConnInfo();
        tryUpdateDataSource(connInfo);
        return sourceHolder.getDataSource(connInfo).getDWConnection(sqlExecutorProvider);
    }

    /*
     * 判断是否需要更新DataSource
     * connId变了，表示要使用其他数据连接了
     * database变了，表示要监控其他数据库了
     * 数据连接的host、port、user、pwd变了，表示连接信息变了
     */
    private void tryUpdateDataSource(DWConnInfo connInfo) {
        if (sourceHolder.isDataSourceExists(connInfo)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("conninfo keep same, skip update datasource.");
            }
            return;
        }

        synchronized (DWConnector.class) {
            if (!sourceHolder.isDataSourceExists(connInfo)) {
                updateDataSource(connInfo);
            }
        }
    }

    private void updateDataSource(DWConnInfo newConnInfo) {
        try {
            sourceHolder.destroyDataSource(newConnInfo);
            sourceHolder.createDataSource(newConnInfo, realDataSourceProvider);
        } catch (SQLException e) {
            LOGGER.error("create datasource failed.");
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_DATABASE_CONNECTION_ERROR);
        }
    }

    public void setRealDataSourceProvider(RealDataSourceProvider realDataSourceProvider) {
        this.realDataSourceProvider = realDataSourceProvider;
    }

    public void setSqlExecutorProvider(SQLExecutorProvider sqlExecutorProvider) {
        this.sqlExecutorProvider = sqlExecutorProvider;
    }
}