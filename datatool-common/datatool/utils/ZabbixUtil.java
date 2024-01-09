/*
 * 文 件 名:  ZabbixUtil.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  cWX630741
 * 修改时间： 2021/3/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

import com.huawei.smartcampus.datatool.constant.ZabbixConstant;
import com.huawei.smartcampus.datatool.pojo.AlarmInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * zabbix工具类
 *
 * @author cWX630741
 * @version [SmartCampus V100R001C00, 2021/3/18]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public final class ZabbixUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZabbixUtil.class);

    private ZabbixUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取主机的hostname值
     *
     * @return String: hostname值
     */
    public static String getHostname() {
        Path path = Paths.get(ZabbixConstant.HOST_NAME_PATHS);
        try (Stream<String> lines = Files.lines(path)) {
            Optional<String> first = lines.findFirst();
            return first.orElse(null);
        } catch (IOException e) {
            LOGGER.error("get hostname failed ", e);
            return Optional.empty().toString();
        }
    }

    /**
     * 告警及恢复告警
     *
     * @param alarmInfo 告警对象
     */
    public static void alarmOrResume(AlarmInfo alarmInfo) {
        LOGGER.info(alarmInfo.toString());
    }
}
