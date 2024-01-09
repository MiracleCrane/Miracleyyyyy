/*
 * 文 件 名:  KafkaMqsDynamicTableSink.java
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

import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.sink.SinkFunctionProvider;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.DataType;

import java.util.Map;

/**
 * kafkamqs TableSink
 *
 * @author dWX1154687
 * @version [SmartCampus V100R001C00, 2022/6/8]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class KafkaMqsDynamicTableSink implements DynamicTableSink {
    private final DataType dataType;
    private final KafkaMqsConfig kafkaConfig;
    private final Map<String, String> options;
    private final ConfigMode configMode;

    public KafkaMqsDynamicTableSink(DataType dataType, Map<String, String> options) {
        this.dataType = dataType;
        this.options = options;
        this.configMode = KafkaMqsProcessor.getConfigMode(options);
        this.kafkaConfig = KafkaMqsProcessor.getKafkaConfig(options, configMode);
    }

    @Override
    public ChangelogMode getChangelogMode(ChangelogMode changelogMode) {
        return changelogMode;
    }

    @Override
    public SinkRuntimeProvider getSinkRuntimeProvider(Context context) {
        DataStructureConverter converter = context.createDataStructureConverter(dataType);
        final KafkaMqsSinkFunction<RowData> sinkFunction =
                new KafkaMqsSinkFunction(converter, kafkaConfig, options, configMode);
        return SinkFunctionProvider.of(sinkFunction);
    }

    @Override
    public DynamicTableSink copy() {
        return new KafkaMqsDynamicTableSink(dataType, options);
    }

    @Override
    public String asSummaryString() {
        return "kafka-mqs Table Sink";
    }
}
