/*
 * 文 件 名:  EncryptionTool.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  23.0.0
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2023/3/25
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.util.encrypt;

import com.huawei.dataservice.sql.connector.util.exp.ConnectorRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2023/3/25]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class MqsConfigTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqsConfigTool.class);

    // 配置文件默认路径
    private static final String CONFIGURATION_FILE_PATH = "/opt/flink/datastream.cfg";

    // 配置文件中的配置项
    private static final Properties PROPERTIES = readConfigsFromConfigFile(CONFIGURATION_FILE_PATH);

    // mqs客户端连接地址字段名
    private static final String MQS_URL_FIELD_NAME = "link.topic.address";

    private MqsConfigTool() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * 获取mqs客户端连接地址
     *
     * @return mqs客户端连接地址
     */
    public static String getRomaMqsClientUrlFromConfigFile() {
        return PROPERTIES.getProperty(MQS_URL_FIELD_NAME);
    }


    /**
     * 获取配置文件中的配置信息
     *
     * @param filePath 配置文件路径
     * @return 配置信息
     */
    private static Properties readConfigsFromConfigFile(String filePath) {
        File file = new File(filePath);
        Properties prop = new Properties();
        try (InputStream inputStream = new FileInputStream(file);) {
            prop.load(inputStream);
            return prop;
        } catch (IOException e) {
            LOGGER.error("Load config File input stream error.", e);
            throw new ConnectorRuntimeException("Load config File input stream error.");
        }
    }
}