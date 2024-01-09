/*
 * 文 件 名:  TimeUtil.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/14
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public final class TimeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtil.class);

    public static final String COMMON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String NO_HYPHEN_DATE_FORMAT = "yyyyMMddHHmmss";
    private static final String EXTRACT_DATE_FORMAT = "yyyy-MM-dd 00:00:00";

    /**
     * 校验时间格式
     *
     * @param date 日期
     */
    public static void isValidDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(COMMON_DATE_FORMAT);
        try {
            // 设置严格校验日期格式
            simpleDateFormat.setLenient(false);
            simpleDateFormat.parse(date);
        } catch (ParseException e) {
            LOGGER.error("parse date failed!", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_TIME_FORMAT_INVALID, date);
        }
    }

    /**
     * 获取UTC时间字符串
     *
     * @param date 日期
     * @return UTC时间字符串
     */
    public static String getUtcDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(COMMON_DATE_FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(date);
    }

    /**
     * 获取当前日期零点的时间戳
     *
     * @param timestamp 时间戳
     * @return UTC时间时间戳
     */
    public static Timestamp getUtcZeroOClock(Timestamp timestamp) {
        SimpleDateFormat extractDateFormat = new SimpleDateFormat(EXTRACT_DATE_FORMAT);
        // 取当前时区下的零时刻时间字符串
        String extractedDate = extractDateFormat.format(timestamp);
        // 当前时间时间戳减去时区间隔即为UTC时间的时间戳
        return Timestamp.valueOf(extractedDate);
    }

    /**
     * 获取某utc日期的本地时区的时间戳
     *
     * @param utcDate utc日期
     * @return 本地时区的时间戳
     */
    public static Timestamp getLocalTimeStamp(String utcDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(COMMON_DATE_FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date localDate = simpleDateFormat.parse(utcDate);
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            return Timestamp.valueOf(simpleDateFormat.format(localDate));
        } catch (ParseException e) {
            LOGGER.error("parse date failed!", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_TIME_FORMAT_INVALID, utcDate);
        }
    }

    /**
     * 字符串转Date
     *
     * @param time 传入字符串类型日期
     * @return Date类型的日期
     */
    public static Date getDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            LOGGER.error("parse time failed, parse date is: {}", time);
        }
        return date;
    }

    /**
     * 无中划线日期转UTC日期
     *
     * @param noHyphenDate 无中划线日期字符串
     * @return UTC日期
     */
    public static String getUtcDateFromNoHyphen(String noHyphenDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(NO_HYPHEN_DATE_FORMAT);
        try {
            Date date = sdf.parse(noHyphenDate);
            SimpleDateFormat commonFormat = new SimpleDateFormat(COMMON_DATE_FORMAT);
            commonFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return commonFormat.format(date);
        } catch (ParseException e) {
            LOGGER.error("parse time failed, parse date is: {}", noHyphenDate, e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_TIME_FORMAT_INVALID, noHyphenDate);
        }
    }

    /**
     * 获取当前数字在一个月中的日期带后缀
     *
     * @param num 天数
     * @return 天数的日期带后缀
     */
    public static String getDateOfMonthWithSuffix(int num) {
        if (num < 1 || num > 31) {
            return "";
        }
        if (num >= 11 && num <= 13) {
            return num + "th";
        }
        switch(num % 10) {
            case 1:
                return num + "st";
            case 2:
                return num + "nd";
            case 3:
                return num + "rd";
            default:
                return num + "th";
        }
    }
}