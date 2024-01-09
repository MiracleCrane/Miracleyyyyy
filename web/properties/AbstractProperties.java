/*
 * 文 件 名:  AbstractProperties.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public abstract class AbstractProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProperties.class);
    private Properties properties = new Properties();

    private PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(SystemPropertyUtils.PLACEHOLDER_PREFIX,
            SystemPropertyUtils.PLACEHOLDER_SUFFIX, SystemPropertyUtils.VALUE_SEPARATOR, true);

    protected AbstractProperties(String filename) {
        load(filename);
    }

    private void load(String name) {
        ClassPathResource resource = new ClassPathResource(name);
        try (InputStream inputStream = resource.getInputStream()) {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.error("load {} input stream error", name);
        }
    }

    private String formatGet(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        return helper.replacePlaceholders(value, properties);
    }

    private String formatGet(String key) {
        String value = properties.getProperty(key);
        return helper.replacePlaceholders(value, properties);
    }

    /**
     * 返回String类型值
     *
     * @param key 配置项
     * @param defaultValue 默认值
     * @return 返回value
     */
    public String getString(String key, String defaultValue) {
        return formatGet(key, defaultValue);
    }

    /**
     * 返回String类型值
     *
     * @param key 配置项
     * @return 返回value
     */
    public String getString(String key) {
        return formatGet(key);
    }

    /**
     * 返回int类型值
     *
     * @param key 配置项
     * @param defaultValue 默认值
     * @return 返回value
     */
    public int getInt(String key, int defaultValue) {
        return Integer.parseInt(formatGet(key, String.valueOf(defaultValue)));
    }

    /**
     * 返回int类型值
     *
     * @param key 配置项
     * @return 返回value
     */
    public int getInt(String key) {
        return Integer.parseInt(formatGet(key));
    }

    /**
     * 返回long值
     *
     * @param key 配置项
     * @param defaultValue 默认值
     * @return 返回value
     */
    public long getLong(String key, long defaultValue) {
        return Long.parseLong(formatGet(key, String.valueOf(defaultValue)));
    }

    /**
     * 返回long值
     *
     * @param key 配置项
     * @return 返回value
     */
    public long getLong(String key) {
        return Long.parseLong(formatGet(key));
    }

    /**
     * 返回int类型值
     *
     * @param key 配置项
     * @param defaultValue 默认值
     * @return 返回value
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.valueOf(formatGet(key, String.valueOf(defaultValue)));
    }

    /**
     * 返回int类型值
     *
     * @param key 配置项
     * @return 返回value
     */
    public boolean getBoolean(String key) {
        return Boolean.valueOf(formatGet(key));
    }

    /**
     * 返回properties
     *
     * @return return properties
     */
    public Properties propertiesClone() {
        return (Properties) properties.clone();
    }
}