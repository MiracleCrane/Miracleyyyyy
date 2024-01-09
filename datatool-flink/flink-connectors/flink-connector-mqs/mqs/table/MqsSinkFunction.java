/*
 * 文 件 名:  MqsSinkFunction.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2021/10/26
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.mqs.table;

import com.huawei.dataservice.sql.connector.mqs.config.MqsConfig;
import com.huawei.dataservice.sql.connector.mqs.enums.ConfigMode;
import com.huawei.dataservice.sql.connector.util.config.UtilCommonConstant;
import com.huawei.dataservice.sql.connector.util.processor.UtilMessageProcessor;
import com.huawei.it.eip.ump.client.producer.Producer;
import com.huawei.it.eip.ump.client.producer.SendResult;
import com.huawei.it.eip.ump.common.exception.UmpException;
import com.huawei.it.eip.ump.common.message.Message;
import com.huawei.smartcampus.datatool.utils.ArrayUtils;

import com.alibaba.fastjson.JSONObject;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.table.connector.sink.DynamicTableSink.DataStructureConverter;
import org.apache.flink.types.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * Mqs sink function
 * 将数据写入 mqs
 *
 * @param <T> Input DataType
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2021/10/26]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class MqsSinkFunction<T> extends RichSinkFunction<T> {
    private static final long serialVersionUID = -1905839142350256949L;
    private static final Logger LOGGER = LoggerFactory.getLogger(MqsSinkFunction.class);
    private transient Producer producer;
    private final DataStructureConverter converter;
    private final MqsConfig mqsConfig;
    private final Map<String, String> colMappings;
    private final String[] fieldNames;
    private final ConfigMode configMode;

    public MqsSinkFunction(DataStructureConverter converter, MqsConfig mqsConfig, ConfigMode configMode,
                           String[] fieldNames, Map<String, String> colMappings) {
        this.converter = converter;
        this.mqsConfig = mqsConfig;
        // 获得列的映射关系
        this.colMappings = colMappings;
        this.fieldNames = ArrayUtils.isNotEmpty(fieldNames)
                ? Arrays.copyOf(fieldNames, fieldNames.length)
                : ArrayUtils.EMPTY_STRING_ARRAY;
        this.configMode = configMode;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        producer = new Producer();
        buildMQSProducer(mqsConfig, producer);
        try {
            producer.start();
            LOGGER.info("Producer start Success.");
        } catch (UmpException e) {
            String errMsg = "Producer start failed.";
            throw new UmpException(errMsg, e);
        }
    }

    /**
     * 构建 Producer 实例
     *
     * @param mqsConfig mqs 配置参数对象
     * @param producer 生产者
     */
    public static void buildMQSProducer(MqsConfig mqsConfig, Producer producer) {
        // 设置统一消息平台的服务器地址
        producer.setUmpNamesrvUrls(mqsConfig.getNamesrvUrls());
        // 设置客户端账号
        producer.setAppId(mqsConfig.getAppId());
        // 设置客户端密钥
        producer.setAppSecret(mqsConfig.getAppSecret());
        // 设置Topic Name
        producer.setTopic(mqsConfig.getTopic());
        // 设置是否需要加密传输
        producer.setEncryptTransport(true);
        // 设置订阅消息的标签，可以指定消费某一类型的消息，默认*表示消费所有类型的消息
        producer.setTags(UtilCommonConstant.TAGS_DEFAULT);
        producer.setInstanceName(UUID.randomUUID().toString());
        LOGGER.info("Producer config Finished.");
    }

    @Override
    public void close() throws Exception {
        if (!producer.isClosed()) {
            producer.shutdown();
        }
        LOGGER.info("producer shutdown ok.");
    }

    @Override
    public void invoke(T value, Context context) throws Exception {
        // toExternal将给定的内部结构转换为外部对象
        Row row = (Row) converter.toExternal(value);
        String strMessage = null;

        // 全映射方式与非全映射方式做不同的处理
        if (ConfigMode.TOTAL_MAPPINGS.equals(configMode)) {
            JSONObject objectMessage = new JSONObject();
            for (int i = 0; i < fieldNames.length; i++) {
                objectMessage.put(fieldNames[i], row.getField(i));
            }
            strMessage = objectMessage.toJSONString();
        } else {
            strMessage = UtilMessageProcessor.transToJson(row, colMappings);
        }
        // 消息体，推荐使用 JSON.toJSONString(businessDo).getBytes("UTF-8")
        // 注：二进制消息MQS不做转换,Producer和Consumer需协商好序列化和反序列化方式
        Message sinkMessage = new Message();
        sinkMessage.setBody(strMessage.getBytes(StandardCharsets.UTF_8));
        SendResult sendResult = producer.send(sinkMessage);
        if (sendResult.isSuccess()) {
            if (LOGGER.isDebugEnabled()) {
                // 发送成功的逻辑处理
                LOGGER.debug("sendResult ok.");
            }
        } else {
            // 发送失败的逻辑处理
            LOGGER.error("sendResult failed.");
        }
    }
}
