/*
 * 文 件 名:  MqsDynamicTableFactory.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2021/8/27
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.mqs.table;

import com.huawei.dataservice.sql.connector.mqs.config.MqsConfig;
import com.huawei.dataservice.sql.connector.mqs.enums.ConfigMode;
import com.huawei.dataservice.sql.connector.util.config.UtilCommonConstant;
import com.huawei.dataservice.sql.connector.util.encrypt.MqsConfigTool;
import com.huawei.dataservice.sql.connector.util.processor.UtilCheckProcessor;
import com.huawei.dataservice.sql.connector.util.processor.UtilMessageProcessor;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.table.connector.format.DecodingFormat;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.factories.DeserializationFormatFactory;
import org.apache.flink.table.factories.DynamicTableSinkFactory;
import org.apache.flink.table.factories.DynamicTableSourceFactory;
import org.apache.flink.table.factories.FactoryUtil;
import org.apache.flink.table.types.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Mqs connector factory
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2021/8/27]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class MqsDynamicTableFactory implements DynamicTableSourceFactory, DynamicTableSinkFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqsDynamicTableFactory.class);

    /**
     * Mqs connection url
     */
    public static final ConfigOption<String> URLS =
            ConfigOptions.key("namesrvUrls").stringType().noDefaultValue().withDescription("the mqs connection url");

    /**
     * Mqs app id
     */
    public static final ConfigOption<String> APP_ID =
            ConfigOptions.key("appId").stringType().noDefaultValue().withDescription("the mqs app id");

    /**
     * Mqs app secret
     */
    public static final ConfigOption<String> APP_SECRET =
            ConfigOptions.key("appSecret").stringType().noDefaultValue().withDescription("the mqs app secret");

    /**
     * Mqs connection topic
     */
    public static final ConfigOption<String> TOPIC =
            ConfigOptions.key("topic").stringType().noDefaultValue().withDescription("the mqs connection topic");

    /**
     * The mapping relationship of message field and table field
     */
    public static final ConfigOption<String> FIELD_MAPPINGS =
            ConfigOptions.key("fieldMappings")
                    .stringType()
                    .noDefaultValue()
                    .withDescription("the Mapping relationship of message field and table field");

    /**
     * Contains mqs connection parameters and field mappings
     */
    public static final ConfigOption<String> PARAMETER =
            ConfigOptions.key("parameter")
                    .stringType()
                    .noDefaultValue()
                    .withDescription("contains MQS connection parameters and field mappings");

    /**
     * Decoding format
     */
    public static final ConfigOption<String> FORMAT =
            ConfigOptions.key("format").stringType().defaultValue("json").withDescription("decoding format");

    /**
     * Mqs connector identifier
     */
    public static final String IDENTIFIER = "mqs";

    @Override
    public DynamicTableSource createDynamicTableSource(Context context) {
        // 用于发现格式和验证 MqsDynamicTableFactory
        final FactoryUtil.TableFactoryHelper helper = FactoryUtil.createTableFactoryHelper(this, context);

        // 校验format配置值，获取反序列化格式
        final DecodingFormat<DeserializationSchema<RowData>> decodingFormat =
                helper.discoverDecodingFormat(DeserializationFormatFactory.class, FORMAT);

        // 获取source的数据类型，不包括计算列
        final DataType dataType = context.getCatalogTable().getSchema().toPhysicalRowDataType();

        // 获取source的字段名，包括计算列字段名，不包括水位线
        final String[] fieldNames = context.getCatalogTable().getSchema().getFieldNames();

        // 获取source的所有配置项
        Map<String, String> options = context.getCatalogTable().getOptions();

        // 判断配置方式
        ConfigMode configMode = judgeConfigMode(options);

        // 获取对接mqs的配置信息
        MqsConfig mqsConfig = getMqsConfig(options, configMode);

        // 校验对接mqs的配置信息
        validateMqsConfig(mqsConfig);

        // 获得列的映射关系
        Map<String, String> colMappings = getColMappings(options, configMode);
        return new MqsDynamicTableSource(decodingFormat, dataType, fieldNames, configMode, mqsConfig, colMappings);
    }

    @Override
    public DynamicTableSink createDynamicTableSink(Context context) {
        // 获取sink的数据类型，不包括计算列
        final DataType dataType = context.getCatalogTable().getSchema().toPhysicalRowDataType();

        // 获取source的字段名，包括计算列字段名
        final String[] fieldNames = context.getCatalogTable().getSchema().getFieldNames();

        // 获取sink的所有配置项
        Map<String, String> options = context.getCatalogTable().getOptions();

        // 判断配置方式
        ConfigMode configMode = judgeConfigMode(options);

        // 获取对接mqs的配置信息
        MqsConfig mqsConfig = getMqsConfig(options, configMode);

        // 校验对接mqs的配置信息
        validateMqsConfig(mqsConfig);

        // 获得列的映射关系
        Map<String, String> colMappings = getColMappings(options, configMode);
        return new MqsDynamicTableSink(dataType, options, fieldNames, configMode, mqsConfig, colMappings);
    }

    @Override
    public String factoryIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        return Collections.emptySet();
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        Set<ConfigOption<?>> options = new HashSet<>();
        options.add(URLS);
        options.add(APP_ID);
        options.add(APP_SECRET);
        options.add(TOPIC);
        options.add(FIELD_MAPPINGS);
        options.add(PARAMETER);
        options.add(FORMAT);
        return options;
    }

    /**
     * 根据配置项来判断配置方式
     *
     * @param options 所有的配置项
     * @return 配置项
     */
    public ConfigMode judgeConfigMode(Map<String, String> options) {
        Set<String> keys = options.keySet();
        if (isFieldMappings(keys)) {
            LOGGER.info("Config mode is fieldMappings.");
            return ConfigMode.FIELD_MAPPINGS;
        } else if (isParameter(keys)) {
            LOGGER.info("Config mode is parameter.");
            return ConfigMode.PARAMETER;
        } else if(isTile(keys)) {
            LOGGER.info("Config mode is tile.");
            return ConfigMode.TILE;
        } else if (isTotalMappings(keys)) {
            LOGGER.info("Config mode is totalMappings.");
            return ConfigMode.TOTAL_MAPPINGS;
        } else {
            LOGGER.error("Unsupported config mode, please check you config.");
            throw new IllegalArgumentException("Unsupported config mode, please check you config.");
        }
    }

    /**
     * 判断是否为fieldMappings配置方式
     * 包含namesrvUrls、appId、appSecret、topic、fieldMappings字段
     *
     * @param keys 所有配置项的key
     * @return true/false
     */
    private boolean isFieldMappings(Set<String> keys) {
        return keys.contains(URLS.key()) && keys.contains(APP_ID.key())
                && keys.contains(APP_SECRET.key()) && keys.contains(TOPIC.key()) && keys.contains(FIELD_MAPPINGS.key());
    }

    /**
     * 判断是否为parameter配置方式
     * 包含parameter字段
     *
     * @param keys 所有配置项的key
     * @return true/false
     */
    private boolean isParameter(Set<String> keys) {
        return keys.contains(PARAMETER.key());
    }

    /**
     * 判断是否为tile配置方式
     * 包含namesrvUrls、appId、appSecret、topic、field.xxx.path字段
     *
     * @param keys 所有配置项的key
     * @return true/false
     */
    private boolean isTile(Set<String> keys) {
        return keys.contains(URLS.key()) && keys.contains(APP_ID.key())
                && keys.contains(APP_SECRET.key()) && keys.contains(TOPIC.key()) && containsTotalKey(keys);
    }

    /**
     * 判断是否为totalMappings配置方式
     * 包含namesrvUrls、appId、appSecret、topic字段，不包含field.xxx.path字段
     *
     * @param keys 所有配置项的key
     * @return true/false
     */
    private boolean isTotalMappings(Set<String> keys) {
        return keys.contains(APP_ID.key())
                && keys.contains(APP_SECRET.key()) && keys.contains(TOPIC.key()) && !containsTotalKey(keys);
    }

    /**
     * 判断是否包含打平配置方式的配置项
     * 包含返回true，不包含返回false
     *
     * @param keys 所有配置项
     * @return true/false
     */
    private boolean containsTotalKey(Set<String> keys) {
        for (String key : keys) {
            String[] keyPath = key.split(UtilCommonConstant.SPOT);
            if (keyPath.length == UtilCommonConstant.FIELD_MAPPING_LENGTH
                    && keyPath[0].equals(UtilCommonConstant.FIELD_MAPPING_PREFIX)
                    && keyPath[2].equals(UtilCommonConstant.FIELD_MAPPING_SUFFIX)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 所有配置项中找到 mqs 的连接配置项
     *
     * @param options    所有的配置项
     * @param configMode 配置方式
     * @return 连接 mqs 的连接信息对象
     */
    private MqsConfig getMqsConfig(Map<String, String> options, ConfigMode configMode) {
        MqsConfig mqsConfig = new MqsConfig();
        LOGGER.info("Do get mqs config, config mode is:{}", configMode.toString());
        if (ConfigMode.TILE.equals(configMode) || ConfigMode.FIELD_MAPPINGS.equals(configMode)) {
            mqsConfig.setNamesrvUrls(options.get(URLS.key()));
            mqsConfig.setAppId(options.get(APP_ID.key()));
            String appSecret = options.get(APP_SECRET.key());
            mqsConfig.setAppSecret(UtilMessageProcessor.processAppSecret(appSecret));
            mqsConfig.setTopic(options.get(TOPIC.key()));
            return mqsConfig;
        }

        if (ConfigMode.PARAMETER.equals(configMode)) {
            mqsConfig = getParameterMqsConfig(options.get(UtilCommonConstant.PARAMETER_MODE));
            return mqsConfig;
        }

        if (ConfigMode.TOTAL_MAPPINGS.equals(configMode)) {
            // namesrvUrls配置项如果没有配置则读取配置文件获取
            if (options.get(URLS.key()) == null || options.get(URLS.key()).isEmpty()) {
                LOGGER.info("Get namesrvUrls config from config file.");
                String romaMqsClientUrl = MqsConfigTool.getRomaMqsClientUrlFromConfigFile();
                mqsConfig.setNamesrvUrls(romaMqsClientUrl);
            } else {
                LOGGER.info("Get namesrvUrls config from options.");
                mqsConfig.setNamesrvUrls(options.get(URLS.key()));
            }

            mqsConfig.setAppId(options.get(APP_ID.key()));
            mqsConfig.setAppSecret(UtilMessageProcessor.processAppSecret(options.get(APP_SECRET.key())));
            mqsConfig.setTopic(options.get(TOPIC.key()));
            return mqsConfig;
        }
        LOGGER.info("Get mqs config success.");
        return mqsConfig;
    }

    /**
     * parameter 配置方式下，获取 mqs 连接信息，返回 MqsConfig 实例
     *
     * @param parameter parameter 配置参数的值
     * @return 返回 MqsConfig 实例， 实例中包含 mqs 连接信息
     */
    private MqsConfig getParameterMqsConfig(String parameter) {
        MqsConfig mqsConfig = new MqsConfig();
        String[] args = parameter.split(UtilCommonConstant.SPACE);
        Properties properties = ParameterTool.fromArgs(args).getProperties();
        mqsConfig.setNamesrvUrls(properties.getProperty(URLS.key()));
        mqsConfig.setAppId(properties.getProperty(APP_ID.key()));
        String appSecret = properties.getProperty(APP_SECRET.key());
        mqsConfig.setAppSecret(UtilMessageProcessor.processAppSecret(appSecret));
        mqsConfig.setTopic(properties.getProperty(TOPIC.key()));
        return mqsConfig;
    }

    /**
     * 检查 mqs 连接参数，所有的连接参数必须有值
     *
     * @param mqsConfig mqs 连接参数
     */
    private void validateMqsConfig(MqsConfig mqsConfig) {
        UtilCheckProcessor.validateConfig(URLS.key(), mqsConfig.getNamesrvUrls());
        UtilCheckProcessor.validateConfig(APP_ID.key(), mqsConfig.getAppId());
        UtilCheckProcessor.validateConfig(APP_SECRET.key(), mqsConfig.getAppSecret());
        UtilCheckProcessor.validateConfig(TOPIC.key(), mqsConfig.getTopic());
    }

    /**
     * 根据不同的参数配置方式，用不同的逻辑从所有配置参数中获取到字段的映射关系
     *
     * @param options    所有的配置参数
     * @param configMode 参数配置方式
     * @return 返回字段的映射关系集合，key 为表中的字段名，value 为消息中的字段名
     */
    private Map<String, String> getColMappings(Map<String, String> options, ConfigMode configMode) {
        if (ConfigMode.FIELD_MAPPINGS.equals(configMode)) {
            String mappings = options.get(FIELD_MAPPINGS.key());
            return UtilMessageProcessor.parseMappings(mappings);
        }

        if (ConfigMode.PARAMETER.equals(configMode)) {
            String[] args = options.get(UtilCommonConstant.PARAMETER_MODE).split(UtilCommonConstant.SPACE);
            Properties properties = ParameterTool.fromArgs(args).getProperties();
            String mappings = properties.getProperty(UtilCommonConstant.PARAMETER_MODE_MAPPINGS);
            return UtilMessageProcessor.parseMappings(mappings);
        }

        if (ConfigMode.TILE.equals(configMode)) {
            return UtilMessageProcessor.getTileColMappings(options);
        }

        if (ConfigMode.TOTAL_MAPPINGS.equals(configMode)) {
            return new HashMap<>();
        }
        LOGGER.error("Cannot find field mapping, Please check your configuration.");
        throw new IllegalArgumentException("Cannot find field mapping, Please check your configuration.");
    }
}
