/*
 * 文 件 名:  KafkaMqsDynamicTableSource.java
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
import com.huawei.smartcampus.datatool.utils.ArrayUtils;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.format.DecodingFormat;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.connector.source.ScanTableSource;
import org.apache.flink.table.connector.source.SourceFunctionProvider;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.DataType;

import java.util.Arrays;
import java.util.Map;

/**
 * kafkamqs TableSource
 *
 * @author dWX1154687
 * @version [SmartCampus V100R001C00, 2022/6/8]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class KafkaMqsDynamicTableSource implements ScanTableSource {
    private final Map<String, String> options;
    private final KafkaMqsConfig kafkaConfig;
    private final DecodingFormat<DeserializationSchema<RowData>> decodingFormat;
    private final DataType dataType;
    private final String[] fieldNames;
    private final ConfigMode configMode;

    /**
     * 构造函数
     *
     * @param decodingFormat 编码格式
     * @param dataType       数据类型
     * @param fieldNames     字段集合
     * @param options        配置参数
     */
    public KafkaMqsDynamicTableSource(DecodingFormat<DeserializationSchema<RowData>> decodingFormat, DataType dataType,
                                      String[] fieldNames, Map<String, String> options) {
        this.decodingFormat = decodingFormat;
        this.dataType = dataType;
        this.fieldNames = ArrayUtils.isNotEmpty(fieldNames)
                ? Arrays.copyOf(fieldNames, fieldNames.length)
                : ArrayUtils.EMPTY_STRING_ARRAY;
        this.options = options;
        this.configMode = KafkaMqsProcessor.getConfigMode(options);
        this.kafkaConfig = KafkaMqsProcessor.getKafkaConfig(options, configMode);
    }

    @Override
    public ChangelogMode getChangelogMode() {
        // 指定 insert 方式
        return ChangelogMode.insertOnly();
    }

    @Override
    public ScanRuntimeProvider getScanRuntimeProvider(ScanContext scanContext) {
        final DeserializationSchema<RowData> deserializer = decodingFormat.createRuntimeDecoder(scanContext, dataType);
        final SourceFunction<RowData> sourceFunction = new KafkaMqsSourceFunction(options, fieldNames, kafkaConfig,
                deserializer, configMode);
        return SourceFunctionProvider.of(sourceFunction, false);
    }

    @Override
    public DynamicTableSource copy() {
        return new KafkaMqsDynamicTableSource(decodingFormat, dataType, fieldNames, options);
    }

    @Override
    public String asSummaryString() {
        return "kafka-mqs Table Source";
    }
}
