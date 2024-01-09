/*
 * 文 件 名:  KafkaMqsSourceFunction.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  dWX1154687
 * 修改时间： 2022/6/8
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.kafka.mqs.table;

import com.huawei.dataservice.sql.connector.kafka.mqs.config.KafkaMqsConfig;
import com.huawei.dataservice.sql.connector.kafka.mqs.enums.ConfigMode;
import com.huawei.dataservice.sql.connector.kafka.mqs.util.KafkaMqsProcessor;
import com.huawei.dataservice.sql.connector.util.config.UtilCommonConstant;
import com.huawei.dataservice.sql.connector.util.processor.UtilCheckProcessor;
import com.huawei.dataservice.sql.connector.util.processor.UtilMessageProcessor;
import com.huawei.smartcampus.datatool.utils.ArrayUtils;

import com.alibaba.fastjson.JSONObject;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.table.data.GenericRowData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.SynchronousQueue;

/**
 * KafkaMqs source function
 * 将数据写入 KafkaMqs
 *
 * @author dWX1154687
 * @version [SmartCampus V100R001C00, 2022/6/8]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 * @param <T> Input DataType
 */
public class KafkaMqsSourceFunction<T> extends RichParallelSourceFunction<T> implements ResultTypeQueryable<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMqsSourceFunction.class);

    private static final long serialVersionUID = 1749111808355626182L;
    private transient volatile boolean isRunning;
    private final String[] fieldNames;
    private final KafkaMqsConfig kafkaConfig;
    private final DeserializationSchema<T> deserializer;
    private transient KafkaConsumer<String, String> consumer;
    private final SynchronousQueue<String> blockingQueue = new SynchronousQueue<>();
    private final ConfigMode configMode;
    private final Map<String, String> colMappings;

    public KafkaMqsSourceFunction(Map<String, String> options, String[] fieldNames, KafkaMqsConfig kafkaConfig,
                                  DeserializationSchema<T> deserializer, ConfigMode configMode) {
        this.fieldNames = ArrayUtils.isNotEmpty(fieldNames)
                ? Arrays.copyOf(fieldNames, fieldNames.length)
                : ArrayUtils.EMPTY_STRING_ARRAY;
        this.kafkaConfig = kafkaConfig;
        this.deserializer = deserializer;
        this.configMode = configMode;
        // 获得列的映射关系
        colMappings = KafkaMqsProcessor.getColMappings(options, configMode);
    }

    @Override
    public TypeInformation<T> getProducedType() {
        return deserializer.getProducedType();
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        consumer = buildKafkaConsumer(kafkaConfig);
        consumer.subscribe(Collections.singletonList(kafkaConfig.getTopic()));
    }

    /**
     * 构建Kafka的消费者
     *
     * @param kafkaConfig kafka连接信息
     * @return kafkaConsumer 消费者
     */
    public KafkaConsumer<String, String> buildKafkaConsumer(KafkaMqsConfig kafkaConfig) {
        Properties props = KafkaMqsProcessor.getProperties(kafkaConfig);
        props.put("value.deserializer", StringDeserializer.class);
        props.put("key.deserializer", StringDeserializer.class);
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
        LOGGER.info("Consumer config Finished.");
        return kafkaConsumer;
    }

    @Override
    public void run(SourceContext<T> sourceContext) throws Exception {
        isRunning = true;
        while (isRunning) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                JSONObject jsonObject = JSONObject.parseObject(record.value());
                // PARAMETER 配置方式
                if (ConfigMode.PARAMETER.equals(configMode)) {
                    mappingModeCollect(sourceContext, jsonObject);
                }

                // TILE 配置方式
                if (ConfigMode.TILE.equals(configMode)) {
                    tileModeCollect(sourceContext, jsonObject);
                }
            }
        }
    }

    private void mappingModeCollect(SourceContext<T> sourceContext, JSONObject jsonObject) {
        List<GenericRowData> rows = new ArrayList<>();
        // 判断 Mapping 映射中是否包含数组
        boolean isContainsArray = false;
        for (Map.Entry<String, String> entry : colMappings.entrySet()) {
            if (entry.getValue().contains(UtilCommonConstant.ARRAY_MARK)) {
                isContainsArray = true;
                break;
            }
        }
        if (isContainsArray) {
            // 包含数组时处理逻辑，打平数组，只支持一个数组的打平
            List<GenericRowData> all = UtilMessageProcessor.transArrayRow(colMappings, jsonObject);
            if (all != null && !all.isEmpty()) {
                rows.addAll(all);
            }
        } else {
            Optional<GenericRowData> row = UtilMessageProcessor.transSingleRow(colMappings, jsonObject);
            row.ifPresent(rows::add);
        }
        // 使用 rows
        for (GenericRowData row : rows) {
            sourceContext.collect((T) row);
        }
    }

    private void tileModeCollect(SourceContext<T> sourceContext, JSONObject jsonObject) throws IOException {
        // 历史消息
        if (UtilMessageProcessor.isDeviceHistoryMsg(jsonObject)) {
            List<JSONObject> historyMessages = UtilMessageProcessor.splitDeviceHistoryMsg(jsonObject);
            // 收集历史消息
            for (JSONObject historyMessage : historyMessages) {
                collectMsg(sourceContext, deviceDataChangedMsgFormat(historyMessage));
            }
            return;
        }
        // 实时消息 和 设备上下线消息
        if (UtilCheckProcessor.isDeviceDataChangedMsg(jsonObject)) {
            collectMsg(sourceContext, deviceDataChangedMsgFormat(jsonObject));
        } else if (UtilCheckProcessor.isOnlineStatusMsg(jsonObject)) {
            collectMsg(sourceContext, deviceOnlineStatusMsgFormat(jsonObject));
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Error Message format, field [notifyType] is {}.",
                        jsonObject.getString(UtilCommonConstant.NOTIFY_TYPE));
            }
        }
    }

    private void collectMsg(SourceContext<T> sourceContext, String message) throws IOException {
        sourceContext.collect(deserializer.deserialize(message.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 将设备在线状态消息格式化
     *
     * @param jsonObj 设备属性消息
     * @return 格式化后的字符串
     */
    private String deviceOnlineStatusMsgFormat(JSONObject jsonObj) {
        JSONObject resultJson = new JSONObject();
        // 把写在 with 中配置的字段放入结果对象
        putOptionFields(resultJson, jsonObj);
        return resultJson.toJSONString();
    }

    /**
     * 将设备属性消息格式化
     *
     * @param jsonObj 设备属性消息
     * @return 格式化后的字符串
     */
    private String deviceDataChangedMsgFormat(JSONObject jsonObj) {
        JSONObject resultJson = new JSONObject();
        // 把写在 with 中的配置的字段放入结果对象
        putOptionFields(resultJson, jsonObj);

        // 把 data 的子字段放入结果对象
        putSubfieldsFromData(resultJson, jsonObj);
        return resultJson.toJSONString();
    }

    /**
     * 根据配置的字段配置项，从字符串对象中找到对应的字段，然后把值赋值给结果对象
     * 1、正常情况：判断字段是否是data，如果是data则写入JSONObject，如果不是则写入单字段
     * 2、异常情况：如果遇到没有找到的字段，应该赋值为 null，而不是报错
     *
     * @param resultJson 返回的结果对象
     * @param jsonObj 获取到的字符串对象
     */
    private void putOptionFields(JSONObject resultJson, JSONObject jsonObj) {
        // 遍历集合，把值插入JSON对象
        for (Map.Entry<String, String> entry : colMappings.entrySet()) {
            if (UtilCommonConstant.DATA.equals(entry.getValue())) {
                resultJson.put(entry.getKey(), jsonObj.getJSONObject(UtilCommonConstant.DATA));
            } else {
                resultJson.put(entry.getKey(), jsonObj.getString(entry.getValue()));
            }
        }
    }

    private void putSubfieldsFromData(JSONObject resultJson, JSONObject jsonObj) {
        // 找到 data 下的子字段
        List<String> subfields = findSubfieldsFromData();
        JSONObject data = jsonObj.getJSONObject(UtilCommonConstant.DATA);
        for (String field : subfields) {
            String fieldValue = data.getString(field);
            resultJson.put(field, UtilMessageProcessor.replaceFieldValue(fieldValue).orElse(null));
        }
    }

    /**
     * 在所有字段数组中找出属于 data 下的字段
     * 除去白名单中的字段，除去配置参数中的字段
     *
     * @return 集合，data下的字段集合
     */
    private List<String> findSubfieldsFromData() {
        List<String> fieldNameList = new ArrayList<>(Arrays.asList(fieldNames));
        // 剔除配置参数中的字段
        for (Map.Entry<String, String> entry : colMappings.entrySet()) {
            fieldNameList.removeIf(item -> item.equals(entry.getKey()));
        }
        return fieldNameList;
    }

    @Override
    public void cancel() {
        isRunning = false;
        closeKafkaConnection();
    }

    private void closeKafkaConnection() {
        if (consumer != null) {
            consumer.close();
        }
        blockingQueue.clear();
    }
}
