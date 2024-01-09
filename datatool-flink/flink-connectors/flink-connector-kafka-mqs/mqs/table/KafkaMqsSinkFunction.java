/*
 * 文 件 名:  KafkaMqsSinkFunction.java
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
import com.huawei.dataservice.sql.connector.util.processor.UtilMessageProcessor;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.types.Row;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

/**
 * KafkaMqs sink function
 * 将数据写入 KafkaMqs
 *
 * @param <T> Input DataType
 * @author dWX1154687
 * @version [SmartCampus V100R001C00, 2022/6/8]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class KafkaMqsSinkFunction<T> extends RichSinkFunction<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMqsSinkFunction.class);

    private static final long serialVersionUID = 7139916206879392435L;

    private final DynamicTableSink.DataStructureConverter converter;
    private final KafkaMqsConfig kafkaConfig;
    private final Map<String, String> colMappings;
    private transient KafkaProducer<String, String> producer;

    public KafkaMqsSinkFunction(
            DynamicTableSink.DataStructureConverter converter,
            KafkaMqsConfig kafkaConfig,
            Map<String, String> options,
            ConfigMode configMode) {
        this.converter = converter;
        this.kafkaConfig = kafkaConfig;
        // 获得列的映射关系
        colMappings = KafkaMqsProcessor.getColMappings(options, configMode);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        producer = buildKafkaProducer(kafkaConfig);
    }

    /**
     * 功能 构建生产者实例
     *
     * @param kafkaConfig 连接信息
     * @return KafkaProducer 实例
     */
    public KafkaProducer<String, String> buildKafkaProducer(KafkaMqsConfig kafkaConfig) {
        Properties props = KafkaMqsProcessor.getProperties(kafkaConfig);
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", StringSerializer.class);
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);
        LOGGER.info("Producer config Finished.");
        return kafkaProducer;
    }

    @Override
    public void close() throws Exception {
        if (producer != null) {
            producer.flush();
            producer.close();
        }
        LOGGER.info("producer close ok.");
    }

    @Override
    public void invoke(T value, Context context) throws Exception {
        // toExternal将给定的内部结构转换为外部对象
        Row row = (Row) converter.toExternal(value);
        String json = UtilMessageProcessor.transToJson(row, colMappings);
        ProducerRecord<String, String> record = new ProducerRecord(kafkaConfig.getTopic(), json);
        // 不传key，分区写入策略，默认采用轮询
        producer.send(record);
    }
}
