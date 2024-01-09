/*
 * 文 件 名:  MqsSourceFunction.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2021/9/3
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.mqs.table;

import com.huawei.dataservice.sql.connector.mqs.config.MqsConfig;
import com.huawei.dataservice.sql.connector.mqs.enums.ConfigMode;
import com.huawei.dataservice.sql.connector.mqs.exp.MqsConnectorRuntimeException;
import com.huawei.dataservice.sql.connector.util.config.UtilCommonConstant;
import com.huawei.dataservice.sql.connector.util.processor.UtilCheckProcessor;
import com.huawei.dataservice.sql.connector.util.processor.UtilMessageProcessor;
import com.huawei.it.eip.ump.client.consumer.ConsumeStatus;
import com.huawei.it.eip.ump.client.consumer.Consumer;
import com.huawei.it.eip.ump.client.consumer.MessageModel;
import com.huawei.it.eip.ump.common.exception.UmpException;
import com.huawei.it.eip.ump.common.message.Message;
import com.huawei.smartcampus.datatool.utils.ArrayUtils;

import com.alibaba.fastjson.JSONObject;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.table.data.GenericRowData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.SynchronousQueue;

/**
 * Mqs source
 * 从 mqs 获取消息
 *
 * @param <T> Output DataType
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2021/9/3]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class MqsSourceFunction<T> extends RichParallelSourceFunction<T> implements ResultTypeQueryable<T> {
    /**
     * 默认消费模式
     */
    public static final String MESSAGE_MODEL_DEFAULT = "CLUSTERING";
    private static final long serialVersionUID = 4165595215983229011L;
    private static final Logger LOGGER = LoggerFactory.getLogger(MqsSourceFunction.class);
    private transient volatile boolean isRunning;
    private final String[] fieldNames;
    private final MqsConfig mqsConfig;
    private final DeserializationSchema<T> deserializer;
    private transient Consumer consumer;
    private final SynchronousQueue<Message> blockingQueue = new SynchronousQueue<>();
    private final ConfigMode configMode;
    private final Map<String, String> colMappings;

    public MqsSourceFunction(String[] fieldNames, MqsConfig mqsConfig, DeserializationSchema<T> deserializer,
            ConfigMode configMode, Map<String, String> colMappings) {
        this.fieldNames = ArrayUtils.isNotEmpty(fieldNames)
                ? Arrays.copyOf(fieldNames, fieldNames.length)
                : ArrayUtils.EMPTY_STRING_ARRAY;
        this.mqsConfig = mqsConfig;
        this.deserializer = deserializer;
        this.configMode = configMode;
        this.colMappings = colMappings;
    }

    @Override
    public TypeInformation<T> getProducedType() {
        return deserializer.getProducedType();
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        consumer = new Consumer();
        buildMQSConsumer(mqsConfig, consumer);
        try {
            consumer.subscribe(message -> {
                try {
                    blockingQueue.put(message);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Receive a message from mqs.");
                    }
                } catch (InterruptedException e) {
                    LOGGER.error("InterruptedException when put message.", e);
                    Thread.currentThread().interrupt();
                }
                // 正常接收到消息后，请务必返回CONSUME_SUCCESS，只有在业务处理失败才返回RECONSUME_LATER
                return ConsumeStatus.CONSUME_SUCCESS;
            });
            consumer.start();
            LOGGER.info("Consumer start success.");
        } catch (UmpException e) {
            String errorMsg = "Consumer start failed.";
            throw new MqsConnectorRuntimeException(errorMsg, e);
        }
    }

    /**
     * 构建MQS的消费者
     *
     * @param mqsConfig 配置参数
     * @param consumer Consumer实例
     */
    private void buildMQSConsumer(MqsConfig mqsConfig, Consumer consumer) {
        // 设置统一消息平台的服务器地址
        consumer.setUmpNamesrvUrls(mqsConfig.getNamesrvUrls());
        // 设置客户端账号
        consumer.setAppId(mqsConfig.getAppId());
        // 设置客户端密钥
        consumer.setAppSecret(mqsConfig.getAppSecret());
        // 设置topic
        consumer.setTopic(mqsConfig.getTopic());
        // 设置订阅消息的标签，可以指定消费某一类型的消息，默认*表示消费所有类型的消息
        consumer.setTags(UtilCommonConstant.TAGS_DEFAULT);
        // 消费模式 CLUSTERING 集成模式，默认 BROADCASTING 广播模式
        consumer.setMessageModel(MessageModel.valueOf(MESSAGE_MODEL_DEFAULT));
        // 设置是否需要加密传输
        consumer.setEncryptTransport(true);
        consumer.setConsumeThreadMin(1);
        consumer.setConsumeThreadMax(1);
        consumer.setInstanceName(UUID.randomUUID().toString());
        LOGGER.info("Consumer config finished.");
    }

    @Override
    public void run(SourceContext<T> sourceContext) throws Exception {
        LOGGER.info("Start processing messages.");
        isRunning = true;
        while (isRunning) {
            Message message = blockingQueue.take();
            JSONObject objectMessage = JSONObject.parseObject(new String(message.getBody(), StandardCharsets.UTF_8));

            // FIELD_MAPPINGS || PARAMETER 配置方式
            if (ConfigMode.FIELD_MAPPINGS.equals(configMode) || ConfigMode.PARAMETER.equals(configMode)) {
                mappingModeCollect(sourceContext, objectMessage);
                continue;
            }

            // TOTAL_MAPPINGS配置方式
            if (ConfigMode.TOTAL_MAPPINGS.equals(configMode)) {
                sourceContext
                        .collect(deserializer.deserialize(objectMessage.toString().getBytes(StandardCharsets.UTF_8)));
                continue;
            }

            // TILE 配置方式
            if (ConfigMode.TILE.equals(configMode)) {
                tileModeCollect(sourceContext, objectMessage);
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
        LOGGER.info("Cancel mqs source start.");
        isRunning = false;
        closeMqsConnection();
        LOGGER.info("Cancel mqs source success.");
    }

    private void closeMqsConnection() {
        try {
            if (!consumer.isClosed()) {
                consumer.shutdown();
            }
            blockingQueue.clear();
        } catch (UmpException e) {
            LOGGER.error("Consumer shutdown failed.", e);
            throw new MqsConnectorRuntimeException("Consumer shutdown failed.");
        }
    }
}
