/*
 * 文 件 名:  DataToolDatabaseDynamicTableSinkFactory.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/9/6
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.jdbc.table.factory;

import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.DRIVER;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.LOOKUP_CACHE_MAX_ROWS;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.LOOKUP_CACHE_MISSING_KEY;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.LOOKUP_CACHE_TTL;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.LOOKUP_MAX_RETRIES;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.MAX_RETRY_TIMEOUT;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.PASSWORD;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.SCAN_AUTO_COMMIT;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.SCAN_FETCH_SIZE;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.SCAN_PARTITION_COLUMN;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.SCAN_PARTITION_LOWER_BOUND;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.SCAN_PARTITION_NUM;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.SCAN_PARTITION_UPPER_BOUND;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.SINK_BUFFER_FLUSH_INTERVAL;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.SINK_BUFFER_FLUSH_MAX_ROWS;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.SINK_MAX_RETRIES;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.SINK_PARALLELISM;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.TABLE_NAME;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.URL;
import static org.apache.flink.connector.jdbc.table.JdbcConnectorOptions.USERNAME;

import com.huawei.dataservice.sql.connector.gaussdb.options.JdbcConnectorOptionsBuilder;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.dialect.JdbcDialect;
import org.apache.flink.connector.jdbc.internal.options.JdbcConnectorOptions;
import org.apache.flink.connector.jdbc.internal.options.JdbcDmlOptions;
import org.apache.flink.connector.jdbc.internal.options.JdbcLookupOptions;
import org.apache.flink.connector.jdbc.internal.options.JdbcReadOptions;
import org.apache.flink.table.factories.FactoryUtil;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.logical.RowType;
import org.apache.flink.util.Preconditions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * DataTool使用的JDBCDynamicTableFactory基类，所有数据库类型的DynamicTableFactory需要继承该类
 * 该类提供了统一的数据库访问权限校验
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/9/6]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public abstract class DataToolJDBCDynamicTableFactory extends DataToolDynamicTableFactory {

    public static void validateDataTypeWithJdbcDialect(DataType dataType, JdbcDialect dialect) {
        dialect.validate((RowType) dataType.getLogicalType());
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        Set<ConfigOption<?>> requiredOptions = new HashSet<>();
        requiredOptions.add(URL);
        requiredOptions.add(TABLE_NAME);
        return requiredOptions;
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        Set<ConfigOption<?>> optionalOptions = new HashSet<>();
        optionalOptions.add(DRIVER);
        optionalOptions.add(USERNAME);
        optionalOptions.add(PASSWORD);
        optionalOptions.add(SCAN_PARTITION_COLUMN);
        optionalOptions.add(SCAN_PARTITION_LOWER_BOUND);
        optionalOptions.add(SCAN_PARTITION_UPPER_BOUND);
        optionalOptions.add(SCAN_PARTITION_NUM);
        optionalOptions.add(SCAN_FETCH_SIZE);
        optionalOptions.add(SCAN_AUTO_COMMIT);
        optionalOptions.add(LOOKUP_CACHE_MAX_ROWS);
        optionalOptions.add(LOOKUP_CACHE_TTL);
        optionalOptions.add(LOOKUP_MAX_RETRIES);
        optionalOptions.add(LOOKUP_CACHE_MISSING_KEY);
        optionalOptions.add(SINK_BUFFER_FLUSH_MAX_ROWS);
        optionalOptions.add(SINK_BUFFER_FLUSH_INTERVAL);
        optionalOptions.add(SINK_MAX_RETRIES);
        optionalOptions.add(SINK_PARALLELISM);
        optionalOptions.add(MAX_RETRY_TIMEOUT);
        return optionalOptions;
    }

    /**
     * get jdbc url
     *
     * @param config config
     * @return jdbc url
     */
    public String getJdbcUrl(ReadableConfig config) {
        return config.get(URL);
    }

    /**
     * 获取和验证配置
     *
     * @param helper helper
     * @return 连接器的各项配置参数
     */
    public ReadableConfig getAndValidateReadableConfig(FactoryUtil.TableFactoryHelper helper) {
        helper.validate();
        ReadableConfig config = helper.getOptions();
        // 校验其它参数
        validateConfigOptions(config);
        return config;
    }

    /**
     * 验证配置方法
     *
     * @param config 配置
     */
    public void validateConfigOptions(ReadableConfig config) {
        checkAllOrNone(config, new ConfigOption[]{USERNAME, PASSWORD});
        ConfigOption[] scanArray = {SCAN_PARTITION_COLUMN, SCAN_PARTITION_NUM, SCAN_PARTITION_LOWER_BOUND,
                SCAN_PARTITION_UPPER_BOUND};
        checkAllOrNone(config, scanArray);
        if (config.getOptional(SCAN_PARTITION_LOWER_BOUND).isPresent()
                && config.getOptional(SCAN_PARTITION_UPPER_BOUND).isPresent()) {
            long lowerBound = config.get(SCAN_PARTITION_LOWER_BOUND);
            long upperBound = config.get(SCAN_PARTITION_UPPER_BOUND);
            if (lowerBound > upperBound) {
                throw new IllegalArgumentException(String.format(Locale.ROOT,
                        "'%s'='%s' must not be larger than '%s'='%s'.", SCAN_PARTITION_LOWER_BOUND.key(), lowerBound,
                        SCAN_PARTITION_UPPER_BOUND.key(), upperBound));
            }
        }
        checkAllOrNone(config, new ConfigOption[]{LOOKUP_CACHE_MAX_ROWS, LOOKUP_CACHE_TTL});
        if (config.get(LOOKUP_MAX_RETRIES) < 0) {
            throw new IllegalArgumentException(
                    String.format("The value of '%s' option shouldn't be negative, but is %s.",
                            LOOKUP_MAX_RETRIES.key(), config.get(LOOKUP_MAX_RETRIES)));
        }

        if (config.get(SINK_MAX_RETRIES) < 0) {
            throw new IllegalArgumentException(
                    String.format("The value of '%s' option shouldn't be negative, but is %s.", SINK_MAX_RETRIES.key(),
                            config.get(SINK_MAX_RETRIES)));
        }

        if (config.get(MAX_RETRY_TIMEOUT).getSeconds() <= 0) {
            throw new IllegalArgumentException(String.format(
                    "The value of '%s' option must be in second granularity and shouldn't be smaller than 1"
                            + " second, but is %s.",
                    MAX_RETRY_TIMEOUT.key(),
                    config.get(ConfigOptions.key(MAX_RETRY_TIMEOUT.key()).stringType().noDefaultValue())));
        }
    }

    private void checkAllOrNone(ReadableConfig config, ConfigOption<?>[] configOptions) {
        int presentCount = 0;
        for (ConfigOption configOption : configOptions) {
            if (config.getOptional(configOption).isPresent()) {
                presentCount++;
            }
        }
        String[] propertyNames = Arrays.stream(configOptions).map(ConfigOption::key).toArray(String[]::new);
        Preconditions.checkArgument(configOptions.length == presentCount || presentCount == 0,
                "Either all or none of the following options should be provided:"
                        + String.join(System.lineSeparator(), propertyNames));
    }

    /**
     * 获取JDBC连接参数
     * 这个地方要报dialect传入进来
     *
     * @param readableConfig 连接配置
     * @param dialect 方言
     * @return JDBC连接参数
     */
    public JdbcConnectorOptions getJdbcOptions(ReadableConfig readableConfig, JdbcDialect dialect) {
        final String url = readableConfig.get(URL);
        // 这个地方不设置Dialect
        final JdbcConnectorOptionsBuilder builder = JdbcConnectorOptionsBuilder.builder().setDBUrl(url)
                .setTableName(readableConfig.get(TABLE_NAME)).setDialect(dialect)
                .setParallelism(readableConfig.getOptional(SINK_PARALLELISM).orElse(null))
                .setConnectionCheckTimeoutSeconds((int) readableConfig.get(MAX_RETRY_TIMEOUT).getSeconds());
        readableConfig.getOptional(DRIVER).ifPresent(builder::setDriverName);
        readableConfig.getOptional(USERNAME).ifPresent(builder::setUsername);
        readableConfig.getOptional(PASSWORD).ifPresent(builder::setPassword);
        return builder.build();
    }

    /**
     * get jdbc execution options
     *
     * @param config table config
     * @return jdbc execution options
     */
    public JdbcExecutionOptions getJdbcExecutionOptions(ReadableConfig config) {
        final JdbcExecutionOptions.Builder builder = new JdbcExecutionOptions.Builder();
        builder.withBatchSize(config.get(SINK_BUFFER_FLUSH_MAX_ROWS));
        builder.withBatchIntervalMs(config.get(SINK_BUFFER_FLUSH_INTERVAL).toMillis());
        builder.withMaxRetries(config.get(SINK_MAX_RETRIES));
        return builder.build();
    }

    /**
     * jdbc dml配置
     *
     * @param jdbcOptions jdbcOptions
     * @param dataType table schema
     * @param primaryKeyIndexes 主键索引
     * @return jdbc dml options
     */
    public JdbcDmlOptions getJdbcDmlOptions(JdbcConnectorOptions jdbcOptions, DataType dataType,
            int[] primaryKeyIndexes) {
        String[] keyFields = Arrays.stream(primaryKeyIndexes).mapToObj(i -> DataType.getFieldNames(dataType).get(i))
                .toArray(String[]::new);
        return JdbcDmlOptions.builder().withTableName(jdbcOptions.getTableName()).withDialect(jdbcOptions.getDialect())
                .withFieldNames(DataType.getFieldNames(dataType).toArray(new String[0]))
                .withKeyFields(keyFields.length > 0 ? keyFields : null).build();
    }

    /**
     * get jdbc read options
     *
     * @param readableConfig readableConfig
     * @return jdbc read options
     */
    public JdbcReadOptions getJdbcReadOptions(ReadableConfig readableConfig) {
        final Optional<String> partitionColumnName = readableConfig.getOptional(SCAN_PARTITION_COLUMN);
        final JdbcReadOptions.Builder builder = JdbcReadOptions.builder();
        if (partitionColumnName.isPresent()) {
            builder.setPartitionColumnName(partitionColumnName.get());
            builder.setPartitionLowerBound(readableConfig.get(SCAN_PARTITION_LOWER_BOUND));
            builder.setPartitionUpperBound(readableConfig.get(SCAN_PARTITION_UPPER_BOUND));
            builder.setNumPartitions(readableConfig.get(SCAN_PARTITION_NUM));
        }
        readableConfig.getOptional(SCAN_FETCH_SIZE).ifPresent(builder::setFetchSize);
        builder.setAutoCommit(readableConfig.get(SCAN_AUTO_COMMIT));
        return builder.build();
    }

    /**
     * get jdbc lookup options
     *
     * @param readableConfig table config
     * @return jdbc lookup options
     */
    public JdbcLookupOptions getJdbcLookupOptions(ReadableConfig readableConfig) {
        return new JdbcLookupOptions(readableConfig.get(LOOKUP_CACHE_MAX_ROWS),
                readableConfig.get(LOOKUP_CACHE_TTL).toMillis(), readableConfig.get(LOOKUP_MAX_RETRIES),
                readableConfig.get(LOOKUP_CACHE_MISSING_KEY));
    }
}