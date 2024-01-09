/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

package com.huawei.dataservice.sql.udf;

import org.apache.flink.table.functions.ScalarFunction;

/**
 * 字符串HASH函数
 *
 * @since 2020-12-30 10:43:47
 */
public class GetHashCode extends ScalarFunction {
    private static final long serialVersionUID = 5330110146564332385L;

    /**
     * 返回字符串对应的hash值
     *
     * @param str 字符串
     * @return hash值
     */
    public int eval(String str) {
        return str.hashCode();
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
