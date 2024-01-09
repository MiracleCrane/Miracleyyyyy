/*
 * 文 件 名:  MqsDynamicTableSink.java
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
import com.huawei.smartcampus.datatool.utils.ArrayUtils;

import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.sink.SinkFunctionProvider;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.types.DataType;

import java.util.Arrays;
import java.util.Map;

/**
 * Mqs TableSink
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2021/10/26]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class MqsDynamicTableSink implements DynamicTableSink {
    private final DataType dataType;
    private final MqsConfig mqsConfig;
    private final Map<String, String> options;
    private final ConfigMode configMode;
    private final String[] fieldNames;
    private final Map<String, String> colMappings;

    public MqsDynamicTableSink(DataType dataType, Map<String, String> options, String[] fieldNames,
                               ConfigMode configMode, MqsConfig mqsConfig, Map<String, String> colMappings) {
        this.dataType = dataType;
        this.options = options;
        this.fieldNames = ArrayUtils.isNotEmpty(fieldNames)
                ? Arrays.copyOf(fieldNames, fieldNames.length)
                : ArrayUtils.EMPTY_STRING_ARRAY;
        this.configMode = configMode;
        this.mqsConfig = mqsConfig;
        this.colMappings = colMappings;
    }

    @Override
    public ChangelogMode getChangelogMode(ChangelogMode changelogMode) {
        return changelogMode;
    }

    @Override
    public SinkRuntimeProvider getSinkRuntimeProvider(Context context) {
        DataStructureConverter converter = context.createDataStructureConverter(dataType);
        final MqsSinkFunction<RowData> sinkFunction = new MqsSinkFunction(converter, mqsConfig, configMode, fieldNames,
                colMappings);
        return SinkFunctionProvider.of(sinkFunction);
    }

    @Override
    public DynamicTableSink copy() {
        return new MqsDynamicTableSink(dataType, options, fieldNames, configMode, mqsConfig, colMappings);
    }

    @Override
    public String asSummaryString() {
        return "MQS Table Sink";
    }

    /**
     * 根据是否包含特定配置参数来判断配置模式
     * 包含fieldMappings字段则为FIELD_MAPPINGS配置方式
     * 包含parameter字段则为PARAMETER_MODE配置方式
     * 否则为打平方式
     *
     * @param options 所有的配置项
     * @return 平铺模式或者 fieldMappings 模式或者 parameter 模式
     */
    public ConfigMode getConfigMode(Map<String, String> options) {
        if (options.containsKey("fieldMappings")) {
            return ConfigMode.FIELD_MAPPINGS;
        } else if (options.containsKey(UtilCommonConstant.PARAMETER_MODE)) {
            return ConfigMode.PARAMETER;
        } else {
            return ConfigMode.TILE;
        }
    }
}
