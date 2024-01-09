/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

package com.huawei.dataservice.sql.udf;

import com.huawei.smartcampus.datatool.utils.StringUtils;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.types.Row;

import java.util.Map;

/**
 * 列转行
 *
 * @author zwx632190
 * @version [SmartCampus V100R001C00, 2021/3/30]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@FunctionHint(output = @DataTypeHint("ROW<column_name STRING, column_value STRING>"))
public class RowToColumn extends TableFunction<Row> {
    private static final long serialVersionUID = -4329070284266958607L;

    /**
     * 将Map拆分成多行返回，key作为column_name,value作业column_value
     *
     * @param param map
     */
    public void eval(Map<String, String> param) {
        if (param == null || param.isEmpty()) {
            return;
        }
        param.forEach((String key, String value) -> {
            if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
                collect(Row.of(key, value));
            }
        });
    }

    /**
     * UDF 默认对于相同的输入会有相同的输出，如果 UDF 不能保证相同的输出
     * （例如在 UDF 中调用外部服务，相同的输入值可能返回不同的结果）
     * 建议实现override isDeterministic()方法，返回false
     *
     * @return boolean
     */
    @Override
    public boolean isDeterministic() {
        return false;
    }
}
