/*
 * 文 件 名:  DefaultSQLExecutorProvider.java
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * 提供了默认的sql执行方法，测试时可以进行替换提升代码可测试性
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/29]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DefaultSQLExecutorProvider implements SQLExecutorProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSQLExecutorProvider.class);

    @Override
    public DWRecordSet executeQuery(DataSource dataSource, String querySql, List<Object> params) {
        List<Map<String, Object>> recordList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(querySql);
            // 填参数，index从1开始
            for (int index = 0; index < params.size(); index++) {
                preparedStatement.setObject(index + 1, params.get(index));
            }
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                int columnCount = metaData.getColumnCount();
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    // 字段名称
                    String columnName = metaData.getColumnName(i);
                    // 字段值
                    Object columnValue = resultSet.getObject(columnName);
                    row.put(columnName, columnValue);
                }
                recordList.add(row);
            }

            return new DWRecordSet(recordList);
        } catch (SQLException e) {
            LOGGER.error("connect to database error.");
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_DATABASE_CONNECTION_ERROR);
        } finally {
            closeResource(connection, preparedStatement, resultSet);
        }
    }

    private void closeResource(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error("close datasource resource error.");
        }
    }
}