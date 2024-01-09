/*
 * 文 件 名:  MqsDynamicTableSource.java
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
 * Mqs TableSource
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2021/9/3]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class MqsDynamicTableSource implements ScanTableSource {
    private final MqsConfig mqsConfig;
    private final DecodingFormat<DeserializationSchema<RowData>> decodingFormat;
    private final DataType dataType;
    private final String[] fieldNames;
    private final ConfigMode configMode;
    private final Map<String, String> colMappings;

    /**
     * 构造函数
     *
     * @param decodingFormat 编码格式
     * @param dataType       数据类型
     * @param fieldNames     字段集合
     * @param configMode     配置方式
     * @param mqsConfig      对接mqs的配置信息
     * @param colMappings    列的映射关系
     */
    public MqsDynamicTableSource(DecodingFormat<DeserializationSchema<RowData>> decodingFormat, DataType dataType,
                                 String[] fieldNames, ConfigMode configMode, MqsConfig mqsConfig, Map<String, String> colMappings) {
        this.decodingFormat = decodingFormat;
        this.dataType = dataType;
        this.fieldNames = ArrayUtils.isNotEmpty(fieldNames)
                ? Arrays.copyOf(fieldNames, fieldNames.length)
                : ArrayUtils.EMPTY_STRING_ARRAY;
        this.configMode = configMode;
        this.mqsConfig = mqsConfig;
        this.colMappings = colMappings;
    }

    @Override
    public ChangelogMode getChangelogMode() {
        // 指定 insert 方式
        return ChangelogMode.insertOnly();
    }

    @Override
    public ScanRuntimeProvider getScanRuntimeProvider(ScanContext scanContext) {
        final DeserializationSchema<RowData> deserializer = decodingFormat.createRuntimeDecoder(scanContext, dataType);
        final SourceFunction<RowData> sourceFunction = new MqsSourceFunction(fieldNames, mqsConfig, deserializer,
                configMode, colMappings);
        return SourceFunctionProvider.of(sourceFunction, false);
    }

    @Override
    public DynamicTableSource copy() {
        return new MqsDynamicTableSource(decodingFormat, dataType, fieldNames, configMode, mqsConfig, colMappings);
    }

    @Override
    public String asSummaryString() {
        return "Mqs table source";
    }
}
