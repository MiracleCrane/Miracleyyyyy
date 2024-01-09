/*
 * 文 件 名:  UtilCheckProcessor.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  dWX1154687
 * 修改时间： 2022/6/24
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.util.processor;

import com.huawei.dataservice.sql.connector.util.config.UtilCommonConstant;
import com.huawei.dataservice.sql.connector.util.enums.DataTypes;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;

/**
 * 消息检查校验工具类
 *
 * @author dWX1154687
 * @version [SmartCampus V100R001C00, 2022/6/24]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public final class UtilCheckProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(UtilCheckProcessor.class);

    private UtilCheckProcessor() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 检查字段类型
     *
     * @param field 字段名
     * @param type 类型
     * @throws IllegalStateException 异常
     */
    public static void checkType(String field, String type) {
        try {
            DataTypes dataTypes = DataTypes.valueOf(type.toUpperCase(Locale.ROOT));
            switch (dataTypes) {
                case INTEGER:
                case INT:
                    Integer.parseInt(field);
                    break;
                case FLOAT:
                case REAL:
                    Float.parseFloat(field);
                    break;
                case DOUBLE:
                    Double.parseDouble(field);
                    break;
                case TINYINT:
                    Byte.parseByte(field);
                    break;
                case SMALLINT:
                    Short.parseShort(field);
                    break;
                case STRING:
                case VARCHAR:
                    break;
                default:
                    checkOtherType(field, dataTypes);
            }
        } catch (ParseException e) {
            throw new IllegalStateException(String.format(Locale.ROOT, "Unexpected value: %s", field), e);
        }
    }

    /**
     * 检查字段类型
     *
     * @param fieldValue 字段值
     * @param dataTypes 类型
     * @throws ParseException 异常
     */
    private static void checkOtherType(String fieldValue, DataTypes dataTypes) throws ParseException {
        switch (dataTypes) {
            case BIGINT:
                Long.valueOf(fieldValue);
                break;
            case BOOLEAN:
                Boolean.parseBoolean(fieldValue);
                break;
            case DECIMAL:
                new BigDecimal(fieldValue);
                break;
            case DATE:
                new SimpleDateFormat("yyyy-MM-dd").parse(fieldValue);
                break;
            case TIME:
                new SimpleDateFormat("HH:mm:ss").parse(fieldValue);
                break;
            case TIMESTAMP:
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fieldValue);
                break;
            case MAP:
                JSONObject.parseObject(fieldValue);
                break;
            default:
                throw new IllegalStateException(
                        String.format(Locale.ROOT, "Unexpected value: %s", dataTypes.toString()));
        }
    }

    /**
     * 判断是否是设备数据变化消息
     * 判断条件为：notifyType = deviceDataChanged
     *
     * @param jsonMsg 接收到的消息
     * @return 是否是设备数据变化消息
     */
    public static boolean isDeviceDataChangedMsg(JSONObject jsonMsg) {
        return UtilCommonConstant.DEVICE_DATA_CHANGED_TYPE.equals(jsonMsg.getString(UtilCommonConstant.NOTIFY_TYPE));
    }

    /**
     * 判断是否是设备上下线状态消息
     * 判断条件为：notifyType = onlineStatus
     *
     * @param jsonMsg 接收到的消息
     * @return 是否是设备上下线状态消息
     */
    public static boolean isOnlineStatusMsg(JSONObject jsonMsg) {
        return UtilCommonConstant.ONLINE_STATUS_TYPE.equals(jsonMsg.getString(UtilCommonConstant.NOTIFY_TYPE));
    }

    /**
     * 判断字段类型是否解析错误
     *
     * @param value 字段值
     * @param type 字段类型
     * @param jsonObject jsonObject
     * @return true表示解析错误，false表示解析成功
     */
    public static boolean isCheckTypeError(Optional<Object> value, String type, JSONObject jsonObject) {
        try {
            value.ifPresent(fieldValue -> UtilCheckProcessor.checkType(fieldValue.toString(), type));
        } catch (Exception e) {
            LOGGER.error("Type parse error: {}.", e.toString());
            // jsonObject.toJSONString() 不存在日志注入问题
            LOGGER.error("Error message: {}", jsonObject.toJSONString());
            return true;
        }
        return false;
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
}
