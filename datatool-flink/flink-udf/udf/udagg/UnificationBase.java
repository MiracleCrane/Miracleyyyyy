/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

package com.huawei.dataservice.sql.udf.udagg;

import com.huawei.dataservice.sql.udf.udagg.acc.UnificationAcc;

import org.apache.flink.table.functions.AggregateFunction;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author zwx632190
 * @version [SmartCampus V100R001C00, 2021/3/30]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 * @param <T> 泛型
 */
public class UnificationBase<T> extends AggregateFunction<T, UnificationAcc<T>> {
    private static final long serialVersionUID = -8854734015725778538L;

    @Override
    public boolean isDeterministic() {
        return false;
    }

    @Override
    public T getValue(UnificationAcc<T> unificationAcc) {
        return unificationAcc.getObj();
    }

    /**
     * 更新最新的值
     * 根据聚合时间取最新的值
     *
     * @param acc 继承对象
     * @param param 入参
     */
    public void accumulate(UnificationAcc<T> acc, T param) {
        if (param != null) {
            acc.setObj(param);
        }
    }

    @Override
    public UnificationAcc createAccumulator() {
        return new UnificationAcc();
    }
}
