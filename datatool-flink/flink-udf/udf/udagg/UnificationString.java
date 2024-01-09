/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

package com.huawei.dataservice.sql.udf.udagg;

import com.huawei.dataservice.sql.udf.udagg.acc.UnificationAcc;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;

/**
 * STRING类型聚合
 * STRING类型聚合
 *
 * @author zwx632190
 * @version [SmartCampus V100R001C00, 2021/3/30]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@FunctionHint(input = @DataTypeHint("STRING"), output = @DataTypeHint("STRING"))
public class UnificationString extends UnificationBase<String> {
    private static final long serialVersionUID = 4162031555261782218L;

    @Override
    public void accumulate(UnificationAcc<String> acc, String in) {
        if (StringUtils.isNotEmpty(in)) {
            acc.setObj(in);
        }
    }

    /**
     * HOP窗口用到merge函数，合并结果
     *
     * @param acc 聚合实体
     * @param its 各个子任务的具体化实体
     */
    public void merge(UnificationAcc<String> acc, Iterable<UnificationAcc<String>> its) {
        for (UnificationAcc<String> accTmp : its) {
            if (accTmp.getObj() != null) {
                acc.setObj(accTmp.getObj());
            }
        }
    }
}
