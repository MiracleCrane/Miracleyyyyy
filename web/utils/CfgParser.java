/*
 * 文 件 名:  CfgParser.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2021/2/9
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件类
 *
 * @author z00569896
 * @version [SmartCampus V100R001C00, 2021/2/9]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public final class CfgParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(CfgParser.class);
    private static Properties applicationProperties = new Properties();

    static {
        ClassPathResource applicationResource = new ClassPathResource("application.properties");
        try (InputStream applicationIs = applicationResource.getInputStream()) {
            applicationProperties.load(applicationIs);
        } catch (FileNotFoundException fe) {
            LOGGER.error("config file does not exit. please check!");
        } catch (IOException e) {
            LOGGER.error("get input stream error: {}", e);
        }
    }

    private CfgParser() {
        throw new IllegalStateException("Utility class");
    }

    public static Properties getApplicationProperties() {
        return (Properties) applicationProperties.clone();
    }
}
