/*
 * 文 件 名:  GaussBaseRowDataLookupFunction.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  22.2.T18.B010
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2022/8/29
 * 修改内容:  与数据库断开连接后，增加重连逻辑
 */

package com.huawei.dataservice.sql.connector.gaussdb.table;

import com.huawei.dataservice.sql.connector.gaussdb.exp.GaussBaseConnectorRuntimeException;
import com.huawei.dataservice.sql.connector.jdbc.connection.DataToolJdbcConnectionProvider;
import com.huawei.smartcampus.datatool.utils.ArrayUtils;

import org.apache.flink.connector.jdbc.converter.JdbcRowConverter;
import org.apache.flink.connector.jdbc.dialect.JdbcDialect;
import org.apache.flink.connector.jdbc.internal.connection.JdbcConnectionProvider;
import org.apache.flink.connector.jdbc.internal.options.JdbcConnectorOptions;
import org.apache.flink.connector.jdbc.internal.options.JdbcLookupOptions;
import org.apache.flink.connector.jdbc.statement.FieldNamedPreparedStatement;
import org.apache.flink.shaded.guava30.com.google.common.cache.Cache;
import org.apache.flink.shaded.guava30.com.google.common.cache.CacheBuilder;
import org.apache.flink.table.data.GenericRowData;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.functions.FunctionContext;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.logical.LogicalType;
import org.apache.flink.table.types.logical.RowType;
import org.apache.flink.util.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * gaussBase connector data look up function
 * 关联维表数据处理
 *
 * @author l30009142
 * @version 22.2.T18.B010
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class GaussBaseRowDataLookupFunction extends TableFunction<RowData> {
    private static final Logger LOG = LoggerFactory.getLogger(GaussBaseRowDataLookupFunction.class);

    // 连接检查超时时间
    private static final int CONNECTION_CHECK_TIMEOUT_SECONDS = 60;

    // 重试时间间隔
    private static final Long RETRY_INTERVAL = 1000L;

    private static final long serialVersionUID = 1L;

    private final String query;
    private final DataType[] keyTypes;
    private final String[] keyNames;
    private final long cacheMaxSize;
    private final long cacheExpireMs;
    private final int maxRetryTimes;
    private final JdbcRowConverter jdbcRowConverter;
    private final JdbcRowConverter lookupKeyRowConverter;
    private final JdbcConnectionProvider connectionProvider;
    private transient FieldNamedPreparedStatement statement;
    private transient Cache<RowData, List<RowData>> cache;

    /**
     * GaussBase row data look up function
     *
     * @param options options
     * @param lookupOptions lookupOptions
     * @param fieldNames fieldNames
     * @param fieldTypes fieldTypes
     * @param keyNames keyNames
     * @param rowType rowType
     * @param jdbcDialect jdbcDialect
     */
    public GaussBaseRowDataLookupFunction(JdbcConnectorOptions options, JdbcLookupOptions lookupOptions,
            String[] fieldNames, DataType[] fieldTypes, String[] keyNames, RowType rowType, JdbcDialect jdbcDialect) {
        Preconditions.checkNotNull(options, "No JdbcOptions supplied.");
        Preconditions.checkNotNull(fieldNames, "No fieldNames supplied.");
        Preconditions.checkNotNull(fieldTypes, "No fieldTypes supplied.");
        Preconditions.checkNotNull(keyNames, "No keyNames supplied.");
        this.connectionProvider = new DataToolJdbcConnectionProvider(options);
        this.keyNames = ArrayUtils.isNotEmpty(keyNames)
                ? Arrays.copyOf(keyNames, keyNames.length)
                : ArrayUtils.EMPTY_STRING_ARRAY;
        List<String> nameList = Arrays.asList(fieldNames);
        this.keyTypes = Arrays.stream(keyNames).map(keyName -> {
            Preconditions.checkArgument(nameList.contains(keyName), "keyName %s can't find in fieldNames %s.", keyName,
                    nameList);
            return fieldTypes[nameList.indexOf(keyName)];
        }).toArray(DataType[]::new);
        this.cacheMaxSize = lookupOptions.getCacheMaxSize();
        this.cacheExpireMs = lookupOptions.getCacheExpireMs();
        this.maxRetryTimes = lookupOptions.getMaxRetryTimes();
        this.query = options.getDialect().getSelectFromStatement(options.getTableName(), fieldNames, keyNames);
        this.jdbcRowConverter = jdbcDialect.getRowConverter(rowType);
        this.lookupKeyRowConverter = jdbcDialect.getRowConverter(
                RowType.of(Arrays.stream(keyTypes).map(DataType::getLogicalType).toArray(LogicalType[]::new)));
    }

    @Override
    public void open(FunctionContext context) throws SQLException {
        try {
            // 建立数据库连接
            establishConnectionAndStatement();
        } catch (ClassNotFoundException e) {
            LOG.error("Failed to establish JDBC connection.");
            throw new IllegalArgumentException("Failed to establish JDBC connection.", e);
        }

        // 初始化缓存
        this.cache = cacheMaxSize == -1 || cacheExpireMs == -1
                ? null
                : CacheBuilder.newBuilder().expireAfterWrite(cacheExpireMs, TimeUnit.MILLISECONDS)
                        .maximumSize(cacheMaxSize).build();
    }

    /**
     * This is a lookup method which is called by Flink framework in runtime.
     *
     * @param keys lookup keys
     * @throws InterruptedException InterruptedException
     */
    public void eval(Object... keys) throws InterruptedException {
        RowData keyRow = GenericRowData.of(keys);

        // 缓存中不为空则先在缓存中查找数据
        if (cache != null) {
            List<RowData> cachedRows = cache.getIfPresent(keyRow);
            if (cachedRows != null) {
                for (RowData cachedRow : cachedRows) {
                    collect(cachedRow);
                }
                // 缓存中已经查找到则返回
                return;
            }
        }

        for (int retry = 0; retry <= maxRetryTimes; retry++) {
            try {
                statement.clearParameters();
                statement = lookupKeyRowConverter.toExternal(keyRow, statement);
                try (ResultSet resultSet = statement.executeQuery()) {
                    reduceKeyRow(resultSet, keyRow);
                }
                break;
            } catch (Exception e) {
                LOG.error("JDBC executeBatch error, retry times = {}", retry);
                if (retry >= maxRetryTimes) {
                    LOG.error("Execution of JDBC statement failed.");
                    throw new GaussBaseConnectorRuntimeException("Execution of JDBC statement failed.");
                }
                try {
                    reestablishConnection();
                } catch (SQLException | ClassNotFoundException exception) {
                    LOG.error("JDBC connection is not valid, and reestablish connection failed.");
                    throw new GaussBaseConnectorRuntimeException("Reestablish JDBC connection failed");
                }
                Thread.sleep(RETRY_INTERVAL);
            }
        }
    }

    private void reestablishConnection() throws SQLException, ClassNotFoundException {
        if (!checkConnectionValid()) {
            statement.close();
            connectionProvider.closeConnection();
            establishConnectionAndStatement();
            LOG.info("Reconnect gauss base lookup success!");
        }
    }

    /**
     * 检查连接是否有效
     *
     * @return 有效返回true，无效返回false
     * @throws SQLException SQLException
     * @throws ClassNotFoundException ClassNotFoundException
     */
    private boolean checkConnectionValid() throws SQLException, ClassNotFoundException {
        Connection dbConn = connectionProvider.getOrEstablishConnection();
        if (dbConn.isClosed()) {
            return false;
        }

        try (Statement stmt = dbConn.createStatement()) {
            stmt.setQueryTimeout(CONNECTION_CHECK_TIMEOUT_SECONDS);
            try (ResultSet resultSet = stmt.executeQuery("SELECT 1");) {
                return true;
            }
        } catch (SQLException exception) {
            return false;
        }
    }

    private void reduceKeyRow(ResultSet resultSet, RowData keyRow) throws SQLException {
        if (cache == null) {
            while (resultSet.next()) {
                collect(jdbcRowConverter.toInternal(resultSet));
            }
        } else {
            ArrayList<RowData> rows = new ArrayList<>();
            while (resultSet.next()) {
                RowData row = jdbcRowConverter.toInternal(resultSet);
                rows.add(row);
                collect(row);
            }
            rows.trimToSize();
            cache.put(keyRow, rows);
        }
    }

    private void establishConnectionAndStatement() throws SQLException, ClassNotFoundException {
        Connection dbConn = connectionProvider.getOrEstablishConnection();
        statement = FieldNamedPreparedStatement.prepareStatement(dbConn, query, keyNames);
    }

    @Override
    public void close() throws SQLException {
        if (cache != null) {
            cache.cleanUp();
            cache = null;
        }
        if (statement != null) {
            try {
                statement.close();
            } finally {
                statement = null;
            }
        }
        connectionProvider.closeConnection();
    }
}
