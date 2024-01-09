/*
 * 文 件 名:  DWDataSource.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.datasource;

import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 数据仓库数据源
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/26]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DWDataSource.class);

    private DataSource realDataSource;

    private DWConnInfo dwConnInfo;

    public DWDataSource(DWConnInfo dwConnInfo, RealDataSourceProvider provider) throws SQLException {
        this.realDataSource = provider.createDataSource(dwConnInfo);
        this.dwConnInfo = dwConnInfo;
    }

    public DWConnection getDWConnection(SQLExecutorProvider provider) {
        return new DWConnection(dwConnInfo.desensitization(), realDataSource, provider);
    }

    /**
     * 判断连接信息是否与本数据源匹配
     *
     * @param dwConnInfo 连接信息
     * @return true/false
     */
    public boolean isMatched(DWConnInfo dwConnInfo) {
        return this.dwConnInfo.sameWith(dwConnInfo);
    }

    public void close() {
        if (realDataSource instanceof Closeable) {
            Closeable closeable = (Closeable) realDataSource;
            try {
                closeable.close();
            } catch (IOException e) {
                LOGGER.error("datasource close error.");
            }
        }
    }
}