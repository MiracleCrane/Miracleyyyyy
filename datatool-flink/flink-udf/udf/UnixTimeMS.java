/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

package com.huawei.dataservice.sql.udf;

import org.apache.flink.table.functions.ScalarFunction;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间操作
 *
 * @since 2020-12-30 10:46:22
 */
public class UnixTimeMS extends ScalarFunction {
    private static final long serialVersionUID = 4670232501277330970L;

    /**
     * 返回当前系统时间对应毫秒数
     *
     * @return 返回当前系统时间对应毫秒数
     */
    public long eval() {
        return System.currentTimeMillis();
    }

    /**
     * 时间字符串转毫秒数
     *
     * @param time   时间字符串
     * @param format time字符串的格式
     * @return time对应的毫秒数
     * @throws ParseException ParseException
     */
    public long eval(String time, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(time);
        return date.getTime();
    }

    /**
     * 获取时间类型的毫秒数
     *
     * @param timestamp 时间戳
     * @return 时间戳对应的毫秒数
     */
    public long eval(Timestamp timestamp) {
        return timestamp.getTime();
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
