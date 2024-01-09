/*
 * 文 件 名:  KafkaMqsSourceFunction.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  dWX1154687
 * 修改时间： 2022/6/8
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.kafka.mqs.util;

import com.huawei.dataservice.sql.connector.kafka.mqs.config.KafkaMqsConfig;
import com.huawei.dataservice.sql.connector.kafka.mqs.enums.ConfigMode;
import com.huawei.dataservice.sql.connector.kafka.mqs.table.KafkaMqsDynamicTableFactory;
import com.huawei.dataservice.sql.connector.util.config.UtilCommonConstant;
import com.huawei.dataservice.sql.connector.util.processor.UtilMessageProcessor;
import com.huawei.seccomponent.common.SCException;
import com.huawei.seccomponent.crypt.CryptoAPI;
import com.huawei.seccomponent.crypt.CryptoFactory;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import org.apache.flink.api.java.utils.ParameterTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * kafkamqs消息处理工具类
 *
 * @author dWX1154687
 * @version [SmartCampus V100R001C00, 2022/6/8]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public final class KafkaMqsProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMqsProcessor.class);

    /**
     * 密文文件名
     */
    public static final String FILENAME_KEY = "key.pass";

    /**
     * 加解密工具对象
     */
    private static CryptoAPI api;

    static {
        try {
            api = CryptoFactory.getInstance(System.getenv("SCC_CONF"));
        } catch (SCException e) {
            LOGGER.error("initialize scc error.");
            throw new RuntimeException(e);
        }
    }

    private KafkaMqsProcessor() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 根据不同的参数配置方式，用不同的逻辑从所有配置参数中获取到字段的映射关系
     *
     * @param options 所有的配置参数
     * @param configMode 参数配置方式
     * @return 返回字段的映射关系集合，key 为表中的字段名，value 为消息中的字段名
     */
    public static Map<String, String> getColMappings(Map<String, String> options, ConfigMode configMode) {
        if (configMode.isParameter() && options.containsKey(UtilCommonConstant.PARAMETER_MODE)) {
            String[] args = options.get(UtilCommonConstant.PARAMETER_MODE).split(UtilCommonConstant.SPACE);
            Properties properties = ParameterTool.fromArgs(args).getProperties();
            String mappings = properties.getProperty(UtilCommonConstant.PARAMETER_MODE_MAPPINGS);
            return UtilMessageProcessor.parseMappings(mappings);
        } else if (configMode.isTile()) {
            return UtilMessageProcessor.getTileColMappings(options);
        } else {
            LOGGER.error("Cannot find field mapping, Please check your configuration.");
            throw new IllegalArgumentException("Cannot find field mapping, Please check your configuration.");
        }
    }

    /**
     * 根据是否包含特定配置参数来判断配置模式
     *
     * @param options 所有的配置项
     * @return 平铺模式或者 fieldMappings 模式或者 parameter 模式
     */
    public static ConfigMode getConfigMode(Map<String, String> options) {
        if (options.containsKey(UtilCommonConstant.PARAMETER_MODE)) {
            return ConfigMode.PARAMETER;
        } else {
            return ConfigMode.TILE;
        }
    }

    /**
     * 所有配置项中找到 Kafka 的连接配置项
     *
     * @param options 所有的配置项
     * @param configMode 参数的配置方式
     * @return 连接 Kafka 的连接信息对象
     */
    public static KafkaMqsConfig getKafkaConfig(Map<String, String> options, ConfigMode configMode) {
        KafkaMqsConfig kafkaConfig = new KafkaMqsConfig();

        if (configMode.equals(ConfigMode.TILE)) { // 打平配置方式直接获取
            kafkaConfig.setAppId(options.get(KafkaMqsDynamicTableFactory.APP_ID.key()));
            // 处理密码
            kafkaConfig.setAppSecret(
                    UtilMessageProcessor.processAppSecret(options.get(KafkaMqsDynamicTableFactory.APP_SECRET.key())));
            kafkaConfig.setServices(options.get(KafkaMqsDynamicTableFactory.SERVERS.key()));
            kafkaConfig.setTopic(options.get(KafkaMqsDynamicTableFactory.TOPIC.key()));
        } else { // parameter 配置方式从 parameter 参数中解析
            // 读parameter中的配置参数
            setParameterKafkaConfig(kafkaConfig, options.get(UtilCommonConstant.PARAMETER_MODE));
        }
        // 读剩余的配置参数
        kafkaConfig.setGroupId(options.get(KafkaMqsDynamicTableFactory.GROUP_ID.key()));
        kafkaConfig.setFormat(options.get(KafkaMqsDynamicTableFactory.FORMAT.key()));
        kafkaConfig.setLatest(options.get(KafkaMqsDynamicTableFactory.LATEST.key()));
        kafkaConfig.setSslTruststoreLocationConfig(
                options.get(KafkaMqsDynamicTableFactory.SSL_TRUSTSTORE_LOCATION_CONFIG.key()));
        kafkaConfig.setSslTruststorePasswordConfig(getSslTruststorePasswordConfig(options));
        kafkaConfig.setSecurityProtocolConfig(options.get(KafkaMqsDynamicTableFactory.SECURITY_PROTOCOL_CONFIG.key()));
        kafkaConfig.setSaslMechanism(options.get(KafkaMqsDynamicTableFactory.SASL_MECHANISM.key()));
        kafkaConfig.setStartupMode(options.get(KafkaMqsDynamicTableFactory.STARTUP_MODE.key()));
        kafkaConfig.setParseErrors(options.get(KafkaMqsDynamicTableFactory.PARSE_ERRORS.key()));
        kafkaConfig.setAlgorithm(options.get(KafkaMqsDynamicTableFactory.ALGORITHM.key()));
        return kafkaConfig;
    }

    /**
     * parameter 配置方式下，获取 Kafka 连接信息，返回 KafkaConfig 实例
     *
     * @param kafkaConfig 连接信息
     * @param parameter   parameter 配置参数的值
     */
    public static void setParameterKafkaConfig(KafkaMqsConfig kafkaConfig, String parameter) {
        String[] args = parameter.split(UtilCommonConstant.SPACE);
        Properties properties = ParameterTool.fromArgs(args).getProperties();
        kafkaConfig.setServices(properties.getProperty(KafkaMqsDynamicTableFactory.SERVERS.key()));
        kafkaConfig.setTopic(properties.getProperty(KafkaMqsDynamicTableFactory.TOPIC.key()));
        kafkaConfig.setAppId(properties.getProperty(KafkaMqsDynamicTableFactory.APP_ID.key()));
        // 处理密码
        kafkaConfig.setAppSecret(UtilMessageProcessor
                .processAppSecret(properties.getProperty(KafkaMqsDynamicTableFactory.APP_SECRET.key())));
    }

    /**
     * 检查 value 是否为空
     *
     * @param key 参数名
     * @param value 参数值
     */
    public static void validateConfig(String key, String value) {
        if (StringUtils.isBlank(value)) {
            String errorMsg = String.format(Locale.ROOT, "The config [%s] can not be empty.", key);
            LOGGER.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
    }

    private static String getSslTruststorePasswordConfig(Map<String, String> options) {
        Optional<String> keystorePwd = getKeyStorePwd(options.get(KafkaMqsDynamicTableFactory.CIPHER_BASE_PATH.key()));
        return keystorePwd.get();
    }

    private static Optional<String> getKeyStorePwd(String cipherBasePath) {
        Optional<String> key = getKey(cipherBasePath);

        if (key.isPresent()) {
            try {
                return Optional.of(api.decrypt(key.get()).getString());
            } catch (Exception exp) {
                LOGGER.error("Decrypt kafka keystore password failed!");
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    private static Optional<String> getKey(String cipherBasePath) {
        String path = cipherBasePath + File.separator + FILENAME_KEY;
        return readLine(path);
    }

    private static Optional<String> readLine(String filePath) {
        File file = new File(filePath);
        try (InputStream is = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(isr)) {
            String str = reader.readLine();
            if (str != null) {
                return Optional.of(str.trim());
            }
        } catch (FileNotFoundException exp) {
            LOGGER.error("Read kafka secret files failed!");
        } catch (IOException exp) {
            LOGGER.error("Read kafka secret files failed!", exp);
        }
        return Optional.empty();
    }

    /**
     * 创建连接信息Properties
     *
     * @param kafkaConfig 连接信息
     * @return Properties
     */
    public static Properties getProperties(KafkaMqsConfig kafkaConfig) {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaConfig.getServices());
        props.put("group.id", kafkaConfig.getAppId() + kafkaConfig.getTopic());
        props.put("auto.offset.reset", kafkaConfig.getLatest());
        props.put("ssl.truststore.location", kafkaConfig.getSslTruststoreLocationConfig());
        props.put("security.protocol", kafkaConfig.getSecurityProtocolConfig());
        props.put("sasl.mechanism", kafkaConfig.getSaslMechanism());
        props.put("sasl.jaas.config",
                "org.apache.kafka.common.security.plain.PlainLoginModule " + "required username=\""
                        + kafkaConfig.getAppId() + "\" password=\"" + kafkaConfig.getAppSecret() + "\";");
        props.put("ssl.truststore.password", kafkaConfig.getSslTruststorePasswordConfig());
        props.put("scan.startup.mode", kafkaConfig.getStartupMode());
        props.put("ssl.endpoint.identification.algorithm", kafkaConfig.getAlgorithm());
        return props;
    }
}
