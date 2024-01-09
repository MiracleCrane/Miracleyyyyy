/*
 * 文 件 名:  UtilCommonConstant.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  dWX1154687
 * 修改时间： 2022/6/17
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.util.config;

/**
 * 工具类常量
 *
 * @author dWX1154687
 * @version [SmartCampus V100R001C00, 2022/6/17]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public final class UtilCommonConstant {
    /**
     * 字段的默认类型
     */
    public static final String DEFAULT_TYPE = "String";

    /**
     * 点字符
     */
    public static final String SPOT = "\\.";

    /**
     * 默认标签
     */
    public static final String TAGS_DEFAULT = "*";

    /**
     * 设备历史消息标识
     */
    public static final String DEVICE_HISTORY_TYPE = "HistoryService";

    /**
     * 设备数据变化消息
     */
    public static final String DEVICE_DATA_CHANGED_TYPE = "deviceDataChanged";

    /**
     * 设备在线状态消息
     */
    public static final String ONLINE_STATUS_TYPE = "onlineStatus";

    /**
     * 字段映射的配置项长度
     */
    public static final int FIELD_MAPPING_LENGTH = 3;

    /**
     * 字段映射配置前缀
     */
    public static final String FIELD_MAPPING_PREFIX = "field";

    /**
     * 字段映射配置后缀
     */
    public static final String FIELD_MAPPING_SUFFIX = "path";

    /**
     * parameter 配置方式
     */
    public static final String PARAMETER_MODE = "parameter";

    /**
     * parameter 配置方式下的字段名，此字段值存放字段的映射关系
     */
    public static final String PARAMETER_MODE_MAPPINGS = "mappings";

    /**
     * data 路径
     */
    public static final String DATA = "data";

    /**
     * 分号
     */
    public static final String SEMICOLON = ";";

    /**
     * 等于号
     */
    public static final String EQUAL = "=";

    /**
     * 数组标记
     */
    public static final String ARRAY_MARK = "[i]";

    /**
     * 空格
     */
    public static final String SPACE = " ";

    /**
     * content数组字段在设备历史消息消息中的路径
     */
    public static final String HISTORY_CONTENT_PATH = "$.data.content";

    /**
     * field serviceId
     */
    public static final String SERVICE_ID = "serviceId";

    /**
     * field thingsModelData
     */
    public static final String THINGS_MODEL_DATA = "thingsModelData";

    /**
     * field thingsModelCode
     */
    public static final String THINGS_MODEL_CODE = "thingsModelCode";

    /**
     * field thingsModelName
     */
    public static final String THINGS_MODEL_NAME = "thingsModelName";

    /**
     * field eventTime
     */
    public static final String EVENT_TIME = "eventTime";

    /**
     * field eventTimeUTC
     */
    public static final String EVENT_TIME_UTC = "eventTimeUTC";

    /**
     * field deviceId
     */
    public static final String DEVICE_ID = "deviceId";

    /**
     * field notifyType
     */
    public static final String NOTIFY_TYPE = "notifyType";

    /**
     * field platformId
     */
    public static final String PLAT_FORM_ID = "platformId";

    /**
     * 字段值 NULLVALUE
     */
    public static final String NULL_VALUE = "NULLVALUE";

    private UtilCommonConstant() {
        throw new IllegalStateException("Utility class");
    }
}
