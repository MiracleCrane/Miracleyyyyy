/*
 * 文 件 名:  DWDataSourcePool.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.datasource;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnInfo;
import com.huawei.smartcampus.datatool.utils.DataToolUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * 当前版本只支持监控一个数据仓库，所以提供的是一个单数据源缓存实现
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/20]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class SingleDWDataSourceHolder implements DWDataSourceHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleDWDataSourceHolder.class);

    private static final SingleDWDataSourceHolder INSTANCE = new SingleDWDataSourceHolder();

    /*
     * key is connId, value is DWDataSource
     */
    private DWDataSource cache;

    public static SingleDWDataSourceHolder getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean createDataSource(DWConnInfo connInfo, RealDataSourceProvider provider) throws SQLException {
        if (cache != null) {
            LOGGER.info("can not create dwdatasouce, cause datasource is exists.");
            return false;
        }
        if (connInfo == null) {
            LOGGER.info("can not create dwdatasouce, cause connInfo is null.");
            return false;
        }

        // 增加数据库黑名单校验
        if (DataToolUtils.isBlackListDataBase(connInfo.getDatabase())) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_CONNECTION_DATABASE);
        }

        cache = new DWDataSource(connInfo, provider);
        LOGGER.info("create dwdatasouce success, connname = {}, database = {}", connInfo.getConnName(),
                connInfo.getDatabase());
        return true;
    }

    @Override
    public void destroyDataSource(DWConnInfo connInfo) {
        if (cache == null) {
            return;
        }
        // 先关闭数据源
        cache.close();
        // 置空数据源
        cache = null;
        LOGGER.info("destroy dwdatasouce success.");
    }

    @Override
    public boolean isDataSourceExists(DWConnInfo connInfo) {
        if (cache == null) {
            return false;
        }

        return cache.isMatched(connInfo) ? true : false;
    }

    @Override
    public DWDataSource getDataSource(DWConnInfo connInfo) {
        return cache;
    }
}