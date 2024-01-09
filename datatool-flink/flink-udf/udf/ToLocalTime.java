/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

package com.huawei.dataservice.sql.udf;

import org.apache.flink.table.functions.ScalarFunction;

import java.sql.Timestamp;

/**
 * 毫秒转本地时间戳
 *
 * @since 2020-12-30 10:45:13
 */
public class ToLocalTime extends ScalarFunction {
    private static final long serialVersionUID = -5994308866854667964L;

    /**
     * 毫秒数转本地时间戳
     *
     * @param timeMs 时间的毫秒数
     * @return {@link Timestamp}
     */
    public Timestamp eval(long timeMs) {
        return new Timestamp(timeMs);
    }

    /**
     * 声明为非确定性函数
     *
     * @return false表示非确定性函数
     */
    @Override
    public boolean isDeterministic() {
        return false;
    }
}
