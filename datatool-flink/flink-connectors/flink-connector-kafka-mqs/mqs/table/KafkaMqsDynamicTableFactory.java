/*
 * 文 件 名:  KafkaMqsDynamicTableFactory.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  dWX1154687
 * 修改时间： 2022/6/8
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.kafka.mqs.table;

import com.huawei.dataservice.sql.connector.kafka.mqs.config.KafkaMqsConfig;
import com.huawei.dataservice.sql.connector.kafka.mqs.util.KafkaMqsProcessor;
import com.huawei.dataservice.sql.connector.util.config.UtilCommonConstant;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.connector.format.DecodingFormat;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.factories.DeserializationFormatFactory;
import org.apache.flink.table.factories.DynamicTableSinkFactory;
import org.apache.flink.table.factories.DynamicTableSourceFactory;
import org.apache.flink.table.factories.FactoryUtil;
import org.apache.flink.table.types.DataType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * kafkamqs连接工厂类connector factory
 *
 * @author dWX1154687
 * @version [SmartCampus V100R001C00, 2022/6/8]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class KafkaMqsDynamicTableFactory implements DynamicTableSourceFactory, DynamicTableSinkFactory {
    /**
     * connector IDENTIFIER
     */
    public static final String IDENTIFIER = "kafka-mqs";

    /**
     * connector CIPHER_BASE_PATH_DEFAULTVALUE
     */
    public static final String CIPHER_BASE_PATH_DEFAULTVALUE = "/opt/flink/certificate/mqs";

    /**
     * 消息集成 appId
     */
    public static final ConfigOption<String> APP_ID = ConfigOptions.key("appId").stringType().noDefaultValue();

    /**
     * 消息集成 appSecret
     */
    public static final ConfigOption<String> APP_SECRET = ConfigOptions.key("appSecret").stringType().noDefaultValue();

    /**
     * 消息集成 topic
     */
    public static final ConfigOption<String> TOPIC = ConfigOptions.key("topic").stringType().noDefaultValue();

    /**
     * 消息集成 MQS内网连接地址
     */
    public static final ConfigOption<String> SERVERS = ConfigOptions.key("namesrvUrls").stringType().noDefaultValue();

    /**
     * 消费组，自定义
     */
    public static final ConfigOption<String> GROUP_ID = ConfigOptions.key("properties.group.id").stringType()
            .noDefaultValue();

    /**
     * 从最新开始消费
     */
    public static final ConfigOption<String> LATEST = ConfigOptions.key("properties.auto.offset.reset").stringType()
            .noDefaultValue();

    /**
     * 格式
     */
    public static final ConfigOption<String> FORMAT = ConfigOptions.key("format").stringType().defaultValue("json")
            .withDescription("decoding format");

    /**
     * 设置SSL根证书的路径，请记得将XXX修改为自己的路径
     */
    public static final ConfigOption<String> SSL_TRUSTSTORE_LOCATION_CONFIG = ConfigOptions
            .key("properties.ssl.truststore.location").stringType()
            .defaultValue("/opt/flink/certificate/mqs/client.truststore.jks");

    /**
     * 设置SSL根证书的目录
     */
    public static final ConfigOption<String> CIPHER_BASE_PATH = ConfigOptions.key("cipher.base.path").stringType()
            .defaultValue(CIPHER_BASE_PATH_DEFAULTVALUE);

    /**
     * 根证书store的密码，保持不变
     */
    public static final ConfigOption<String> SSL_TRUSTSTORE_PASSWORD_CONFIG = ConfigOptions
            .key("properties.ssl.truststore.password").stringType().noDefaultValue();

    /**
     * 接入协议，目前支持使用SASL_SSL协议接入"SASL_SSL"
     */
    public static final ConfigOption<String> SECURITY_PROTOCOL_CONFIG = ConfigOptions
            .key("properties.security.protocol").stringType().noDefaultValue();

    /**
     * SASL鉴权方式，保持不变 "PLAIN"
     */
    public static final ConfigOption<String> SASL_MECHANISM = ConfigOptions.key("properties.sasl.mechanism")
            .stringType().noDefaultValue();

    /**
     * 从最新开始消费
     */
    public static final ConfigOption<String> STARTUP_MODE = ConfigOptions.key("scan.startup.mode").stringType()
            .noDefaultValue();

    /**
     * json.ignore-parse-errors
     */
    public static final ConfigOption<String> PARSE_ERRORS = ConfigOptions.key("json.ignore-parse-errors").stringType()
            .noDefaultValue();

    /**
     * ssl证书固定写法
     */
    public static final ConfigOption<String> ALGORITHM = ConfigOptions
            .key("properties.ssl.endpoint.identification.algorithm").stringType().noDefaultValue();

    @Override
    public DynamicTableSource createDynamicTableSource(Context context) {
        // 用于发现格式和验证 KafkaDynamicTableFactory
        final FactoryUtil.TableFactoryHelper helper = FactoryUtil.createTableFactoryHelper(this, context);
        // discover a suitable decoding format
        final DecodingFormat<DeserializationSchema<RowData>> decodingFormat = helper
                .discoverDecodingFormat(DeserializationFormatFactory.class, FORMAT);
        // table
        final DataType dataType = context.getCatalogTable().getSchema().toPhysicalRowDataType();
        // 获取所有字段的字段名
        final String[] fieldNames = context.getCatalogTable().getSchema().getFieldNames();
        // 获取所有配置项
        Map<String, String> options = context.getCatalogTable().getOptions();
        // 校验配置项
        validateFactoryOptions(options);
        // 判断options有没有存在默认值的配置项，如果有则跳过，没有则添加上
        setDefaultConfig(options);
        return new KafkaMqsDynamicTableSource(decodingFormat, dataType, fieldNames, options);
    }

    @Override
    public String factoryIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        Set<ConfigOption<?>> requiredOptions = new HashSet<>();
        requiredOptions.add(TOPIC);
        requiredOptions.add(SERVERS);
        requiredOptions.add(LATEST);
        requiredOptions.add(SECURITY_PROTOCOL_CONFIG);
        requiredOptions.add(SASL_MECHANISM);
        requiredOptions.add(APP_ID);
        requiredOptions.add(APP_SECRET);
        requiredOptions.add(STARTUP_MODE);
        requiredOptions.add(PARSE_ERRORS);
        requiredOptions.add(ALGORITHM);
        return requiredOptions;
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        Set<ConfigOption<?>> options = new HashSet<>();
        options.add(FORMAT);
        options.add(SSL_TRUSTSTORE_LOCATION_CONFIG);
        options.add(SSL_TRUSTSTORE_PASSWORD_CONFIG);
        options.add(CIPHER_BASE_PATH);
        return options;
    }

    @Override
    public DynamicTableSink createDynamicTableSink(Context context) {
        // table
        final DataType dataType = context.getCatalogTable().getSchema().toPhysicalRowDataType();
        // 获取所有配置项
        Map<String, String> options = context.getCatalogTable().getOptions();
        // 校验配置项
        validateFactoryOptions(options);
        // 判断options有没有存在默认值的配置项，如果有则跳过，没有则添加上
        setDefaultConfig(options);
        return new KafkaMqsDynamicTableSink(dataType, options);
    }

    /**
     * 校验准则
     *
     * @param options 连接信息
     */
    public void validateFactoryOptions(Map<String, String> options) {
        // 校验配置参数
        Configuration option = new Configuration();
        // 获取table的所有配置选项填入Configuration
        options.forEach(option::setString);
        if (options.containsKey(UtilCommonConstant.PARAMETER_MODE)
                && StringUtils.isNotEmpty(options.get(UtilCommonConstant.PARAMETER_MODE))) {
            // 获取parameter里面配置项
            KafkaMqsConfig kafkaMqsConfig = new KafkaMqsConfig();
            KafkaMqsProcessor.setParameterKafkaConfig(kafkaMqsConfig, options.get(UtilCommonConstant.PARAMETER_MODE));
            Map<String, String> map = new HashMap<String, String>();
            map.put(APP_ID.key(), kafkaMqsConfig.getAppId());
            map.put(APP_SECRET.key(), kafkaMqsConfig.getAppSecret());
            map.put(SERVERS.key(), kafkaMqsConfig.getServices());
            map.put(TOPIC.key(), kafkaMqsConfig.getTopic());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                option.setString(entry.getKey(), entry.getValue());
            }
        }
        FactoryUtil.validateFactoryOptions(requiredOptions(), optionalOptions(), option);
    }

    /**
     * 判断options有没有存在默认值的配置项，如果有则跳过，没有则添加上
     *
     * @param options 连接信息
     */
    public void setDefaultConfig(Map<String, String> options) {
        Map<String, String> configs = new HashMap<>();
        configs.put(FORMAT.key(), FORMAT.defaultValue());
        configs.put(SSL_TRUSTSTORE_LOCATION_CONFIG.key(), SSL_TRUSTSTORE_LOCATION_CONFIG.defaultValue());
        configs.put(CIPHER_BASE_PATH.key(), CIPHER_BASE_PATH.defaultValue());
        for (String key : options.keySet()) {
            if (key.equals(FORMAT.key()) || key.equals(SSL_TRUSTSTORE_LOCATION_CONFIG.key())
                    || key.equals(CIPHER_BASE_PATH.key())) {
                configs.remove(key);
            }
        }
        options.putAll(configs);
    }
}
