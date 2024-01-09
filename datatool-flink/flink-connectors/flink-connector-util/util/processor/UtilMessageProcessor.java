/*
 * 文 件 名:  UtilMessageProcessor.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  dWX1154687
 * 修改时间： 2022/6/17
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.util.processor;

import com.huawei.dataservice.sql.connector.util.config.UtilCommonConstant;
import com.huawei.dataservice.sql.connector.util.enums.DataTypes;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import org.apache.flink.table.data.GenericMapData;
import org.apache.flink.table.data.GenericRowData;
import org.apache.flink.table.data.StringData;
import org.apache.flink.types.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 消息处理工具类
 *
 * @author dWX1154687
 * @version [SmartCampus V100R001C00, 2022/6/17]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public final class UtilMessageProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(UtilMessageProcessor.class);

    private UtilMessageProcessor() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 适配密码以[]括起来的场景
     *
     * @param appSecret 不同格式的密码
     * @return 返回真实密码
     */
    public static String processAppSecret(String appSecret) {
        if (appSecret.startsWith("[") && appSecret.endsWith("]")) {
            return appSecret.substring(1, appSecret.length() - 1);
        } else {
            return appSecret;
        }
    }

    /**
     * 拆分历史消息
     *
     * @param historyMsgObj 历史消息
     * @return 拆分后的多条消息
     */
    public static List<JSONObject> splitDeviceHistoryMsg(JSONObject historyMsgObj) {
        // 获取content数组，如果解析报错这个数组会为空
        JSONArray contentArray = UtilMessageProcessor.getContentArrayFromHistoryMsg(historyMsgObj);
        List<JSONObject> jsonObjects = new ArrayList<>();
        // 拆分历史消息
        for (int i = 0; i < contentArray.size(); i++) {
            // 拆分后的消息
            JSONObject newMsg = new JSONObject();
            // platformId位置：platformId
            newMsg.put(UtilCommonConstant.PLAT_FORM_ID, historyMsgObj.getString(UtilCommonConstant.PLAT_FORM_ID));
            // deviceId位置：deviceId
            newMsg.put(UtilCommonConstant.DEVICE_ID, historyMsgObj.getString(UtilCommonConstant.DEVICE_ID));

            // 得到content对象
            JSONObject content = contentArray.getJSONObject(i);
            // serviceId位置：content--serviceId
            newMsg.put(UtilCommonConstant.SERVICE_ID, content.getString(UtilCommonConstant.SERVICE_ID));
            // eventTimeUTC位置：content--eventTime
            newMsg.put(UtilCommonConstant.EVENT_TIME_UTC, content.getString(UtilCommonConstant.EVENT_TIME));

            // 得到data对象
            JSONObject data = content.getJSONObject(UtilCommonConstant.DATA);
            // data位置：service--data--thingsModelData
            newMsg.put(UtilCommonConstant.DATA, data.getJSONObject(UtilCommonConstant.THINGS_MODEL_DATA));
            // thingsModelCode位置：service--data--thingsModelCode
            newMsg.put(UtilCommonConstant.THINGS_MODEL_CODE, data.getString(UtilCommonConstant.THINGS_MODEL_CODE));
            // thingsModelName位置：service--data--thingsModelName
            newMsg.put(UtilCommonConstant.THINGS_MODEL_NAME, data.getString(UtilCommonConstant.THINGS_MODEL_NAME));
            jsonObjects.add(newMsg);
        }
        return jsonObjects;
    }

    /**
     * 获取历史消息中的 content 数组
     *
     * @param historyMsgObj 历史消息
     * @return services 数组
     */
    public static JSONArray getContentArrayFromHistoryMsg(JSONObject historyMsgObj) {
        try {
            Object contentArray = JSONPath.read(historyMsgObj.toJSONString(), UtilCommonConstant.HISTORY_CONTENT_PATH);
            if (contentArray instanceof String && (contentArray.toString().startsWith("["))
                    && (contentArray.toString().endsWith("]"))) {
                return JSONObject.parseArray(contentArray.toString());
            }
            // 其它情况返回一个空数组
            return new JSONArray();
        } catch (ArithmeticException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("history message parse field error, field is {}", UtilCommonConstant.HISTORY_CONTENT_PATH);
            }
            return new JSONArray();
        }
    }

    /**
     * 从消息中解析列值存入list中
     *
     * @param colMappings 表字段与消息的映射关系
     * @param jsonObject 消息
     * @param index 索引
     * @param targetColValues 目标列值
     */
    public static void parseValues(Map<String, String> colMappings, JSONObject jsonObject, int index,
            List<Object> targetColValues) {
        for (Map.Entry<String, String> entry : colMappings.entrySet()) {
            String sourceCol = entry.getValue();
            String[] source = sourceCol.split(":");
            // JSONPath
            String path = null;
            if (sourceCol.contains(UtilCommonConstant.ARRAY_MARK)) {
                path = "$." + source[0].replace("[i]", "[" + index + "]");
            } else {
                path = "$." + source[0];
            }
            Optional<Object> fieldValue = replaceFieldValue(JSONPath.eval(jsonObject, path));
            // 类型判断
            fieldValue.ifPresent(value -> UtilCheckProcessor.checkType(value.toString(), getFieldType(source)));
            targetColValues.add(fieldValue.map(field -> StringData.fromString(field.toString())).orElse(null));
        }
    }

    /**
     * 返回rows列表
     *
     * @param colMappings 表字段与消息的映射关系
     * @param jsonObject 消息
     * @return rows列表
     */
    public static List<GenericRowData> map(Map<String, String> colMappings, JSONObject jsonObject) {
        // 获取targetCol的值
        List<Object> targetColValues = new ArrayList<>();
        for (Map.Entry<String, String> entry : colMappings.entrySet()) {
            String sourceCol = entry.getValue();
            String[] source = sourceCol.split(":");
            Optional<Object> fieldValue = Optional.empty();
            if (!sourceCol.contains(UtilCommonConstant.ARRAY_MARK)) {
                String path = "$." + source[0];
                fieldValue = replaceFieldValue(JSONPath.eval(jsonObject, path));
            }
            boolean isTypeError = UtilCheckProcessor.isCheckTypeError(fieldValue,
                    UtilMessageProcessor.getFieldType(source), jsonObject);
            // 如果发生错误，则返回空集合
            if (isTypeError) {
                return Collections.emptyList();
            }
            getTargetColValues(source, targetColValues, fieldValue);
        }
        // 如果发生错误，则返回 null
        List<GenericRowData> rows = new ArrayList<>();
        rows.add(GenericRowData.of(targetColValues.toArray(new Object[0])));
        return rows;
    }

    /**
     * 把row类型转换为json格式的String
     *
     * @param row row
     * @param colMappings 表字段与消息的映射关系
     * @return JSONString消息
     */
    public static String transToJson(Row row, Map<String, String> colMappings) {
        JSONObject jsonObject = new JSONObject();
        int index = 0;
        // key里存的是目标字段
        Iterator iterator = colMappings.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            // 从 row 中获取字段值
            Object value = row.getField(index);
            index++;
            putJsonObject(jsonObject, key, value);
        }
        return jsonObject.toJSONString();
    }

    /**
     * 把字段名，字段值存入 jsonObject
     *
     * @param jsonObject jsonObject
     * @param key 字段名
     * @param value 字段值
     */
    public static void putJsonObject(JSONObject jsonObject, String key, Object value) {
        // 没有层级就是子属性了
        if (!key.contains(UtilCommonConstant.SPOT)) {
            jsonObject.put(key, value);
            return;
        }
        // 有层级就要递归，直到没有层级
        String[] keys = key.split("\\.", 2);
        String parentKey = keys[0];
        String subKey = keys[1];
        JSONObject parentJsonObject = jsonObject.getJSONObject(parentKey);
        if (parentJsonObject == null) {
            parentJsonObject = new JSONObject();
            jsonObject.put(parentKey, parentJsonObject);
        }
        putJsonObject(parentJsonObject, subKey, value);
    }

    /**
     * 获取数组大小
     *
     * @param colMappings 列映射关系
     * @param jsonObject 消息
     * @return 数组大小
     */
    public static int getArraySize(Map<String, String> colMappings, JSONObject jsonObject) {
        int arraySize = 0;
        for (Map.Entry<String, String> entry : colMappings.entrySet()) {
            String sourceCol = entry.getValue();
            if (sourceCol.contains(UtilCommonConstant.ARRAY_MARK)) {
                arraySize = JSONPath.size(jsonObject, "$." + sourceCol.substring(0, sourceCol.indexOf('[')));
                break;
            }
        }
        return arraySize;
    }

    /**
     * 获取字段类型
     * 不填写默认类型为String
     *
     * @param source 字段类型数组
     * @return 字符安类型
     */
    public static String getFieldType(String[] source) {
        String fieldType = UtilCommonConstant.DEFAULT_TYPE;
        if (source.length == 2) {
            fieldType = source[1];
        }
        return fieldType;
    }

    /**
     * 获取字段类型
     *
     * @param jsonMsg jsonMsg
     * @return boolean 是否
     */
    public static boolean isDeviceHistoryMsg(JSONObject jsonMsg) {
        return UtilCommonConstant.DEVICE_HISTORY_TYPE.equals(jsonMsg.getString("notifyType"));
    }

    /**
     * 根据不同的参数配置方式，用不同的逻辑从所有配置参数中获取到字段的映射关系
     *
     * @param options 所有的配置参数
     * @return 返回字段的映射关系集合
     */
    public static Map<String, String> getTileColMappings(Map<String, String> options) {
        Map<String, String> fieldOptions = new HashMap<>();
        for (Map.Entry<String, String> entry : options.entrySet()) {
            String[] keyPath = entry.getKey().split(UtilCommonConstant.SPOT);
            // 根据前缀、后缀来识别是否是字段映射的配置项
            if (keyPath.length == UtilCommonConstant.FIELD_MAPPING_LENGTH
                    && keyPath[0].equals(UtilCommonConstant.FIELD_MAPPING_PREFIX)
                    && keyPath[2].equals(UtilCommonConstant.FIELD_MAPPING_SUFFIX)) {
                fieldOptions.put(keyPath[1], entry.getValue());
            }
        }
        return fieldOptions;
    }

    /**
     * 解析 mappings 配置，获得字段映射关系
     *
     * @param mappings 定义的配置
     * @return 返回一个有序集合，集合表示字段映射关系，key为数据源的表字段名，value为消息的字段名
     */
    public static Map<String, String> parseMappings(String mappings) {
        // 分号分割
        String[] mappingArrays = mappings.split(UtilCommonConstant.SEMICOLON);
        // 这里必须使用 LinkedHashMap，有序很重要
        Map<String, String> colMappings = new LinkedHashMap<>();
        for (String item : mappingArrays) {
            if (StringUtils.isBlank(item)) {
                continue;
            }
            // a = b 用等号分割
            String[] mapping = item.split(UtilCommonConstant.EQUAL);
            // 去除前后空白
            String targetCol = mapping[0].trim();
            String sourceCol = (mapping.length == 2) ? mapping[1].trim() : null;
            colMappings.put(targetCol, sourceCol);
        }
        return colMappings;
    }

    /**
     * 不包含数组的记录转换为Row对象
     *
     * @param colMappings 字段映射
     * @param jsonObject 消息转化的JSON对象
     * @return later
     */
    public static Optional<GenericRowData> transSingleRow(Map<String, String> colMappings, JSONObject jsonObject) {
        // 获取targetCol的值
        List<Object> targetColValues = new ArrayList<>();
        for (Map.Entry<String, String> entry : colMappings.entrySet()) {
            String sourceCol = entry.getValue();
            String[] source = sourceCol.split(":");
            String jsonpath = "$." + source[0];
            Optional<Object> fieldValue = replaceFieldValue(JSONPath.eval(jsonObject, jsonpath));
            String fieldType = UtilMessageProcessor.getFieldType(source);
            boolean isTypeError = UtilCheckProcessor.isCheckTypeError(fieldValue, fieldType, jsonObject);
            // 如果发生错误，则返回空集合
            if (isTypeError) {
                return Optional.empty();
            }
            getTargetColValues(source, targetColValues, fieldValue);
        }
        return Optional.of(GenericRowData.of(targetColValues.toArray(new Object[0])));
    }

    private static void getTargetColValues(String[] source, List<Object> targetColValues, Optional<Object> fieldValue) {
        if (source.length >= 2 && DataTypes.MAP.name().equals(source[1])) {
            Map<StringData, StringData> map = new HashMap<>();
            if (!fieldValue.isPresent()) {
                targetColValues.add(fieldValue.map(value -> new GenericMapData(map)).orElse(null));
                return;
            }
            JSONObject mapKeyValues = JSONObject.parseObject(fieldValue.get().toString());
            for (String key : mapKeyValues.keySet()) {
                map.put(StringData.fromString(key), StringData.fromString(mapKeyValues.getString(key)));
            }
            targetColValues.add(fieldValue.map(value -> new GenericMapData(map)).orElse(null));
        } else {
            targetColValues.add(fieldValue.map(value -> StringData.fromString(value.toString())).orElse(null));
        }
    }

    /**
     * 不包含数组的记录转换为Row对象
     *
     * @param colMappings 字段映射
     * @param jsonObject 消息转化的JSON对象
     * @param arraySize arraySize
     * @return List
     */
    public static List<GenericRowData> flatMap(Map<String, String> colMappings, JSONObject jsonObject, int arraySize) {
        List<GenericRowData> rows = new ArrayList<>();
        // 数组长度不为0的情况，转化为多条记录
        boolean isTypeError = false;
        try {
            for (int i = 0; i < arraySize; i++) {
                // 获取targetCol的值
                List<Object> targetColValues = new ArrayList<>();
                UtilMessageProcessor.parseValues(colMappings, jsonObject, i, targetColValues);
                rows.add(GenericRowData.of(targetColValues.toArray(new Object[0])));
            }
        } catch (Exception e) {
            LOGGER.error("Type parse error: {}.", e.toString());
            // jsonObject.toJSONString() 不存在日志注入问题
            LOGGER.error("Error message: {}", jsonObject.toJSONString());
            isTypeError = true;
        }
        // 如果发生错误，则返回 nullParseException | IOException exception
        if (isTypeError) {
            return Collections.emptyList();
        }
        return rows;
    }

    /**
     * 将含有数组的消息转化打平为多条记录，然后转化为Row对象列表返回。
     *
     * @param colMappings 字段映射
     * @param jsonObject 消息的JSON对象
     * @return Row List
     */
    public static List<GenericRowData> transArrayRow(Map<String, String> colMappings, JSONObject jsonObject) {
        // 获取消息中JSON数组的长度
        int arraySize = UtilMessageProcessor.getArraySize(colMappings, jsonObject);
        // 如果数组长度为0，则设置对应字段为空
        if (arraySize == 0) {
            return UtilMessageProcessor.map(colMappings, jsonObject);
        } else {
            return UtilMessageProcessor.flatMap(colMappings, jsonObject, arraySize);
        }
    }

    /**
     * 对符合条件的字段值进行替换。
     *
     * @param fieldValue 字段值
     * @return 被替换后的Optional字段值
     */
    public static Optional<Object> replaceFieldValue(Object fieldValue) {
        if (fieldValue == null || UtilCommonConstant.NULL_VALUE.equals(fieldValue.toString())) {
            return Optional.empty();
        }
        return Optional.of(fieldValue);
    }

    /**
     * 对符合条件的字段值进行替换。
     *
     * @param fieldValue 字段值
     * @return 被替换后的Optional字段值
     */
    public static Optional<String> replaceFieldValue(String fieldValue) {
        if (fieldValue == null || UtilCommonConstant.NULL_VALUE.equals(fieldValue)) {
            return Optional.empty();
        }
        return Optional.of(fieldValue);
    }
}
