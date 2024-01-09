/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 */

package com.huawei.dataservice.sql.udf;

import org.apache.flink.table.functions.ScalarFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Locale;

/**
 * 根据经纬度和人数生成人员随机数组字符串
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2021/9/3]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class GeneratePersonLocationUdfFunction extends ScalarFunction {
    private static final long serialVersionUID = -3164189037735476509L;
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratePersonLocationUdfFunction.class);

    // 经度范围常量
    private static final double LONGITUDE_WAVE = 0.0001D;

    // 纬度范围常量
    private static final double LATITUDE_WAVE = 0.00008D;

    // 逗号分隔符
    private static final String SEPARATOR = ",";

    // 左中括号
    private static final String LEFT_BRACKET = "[";

    // 右中括号
    private static final String RIGHT_BRACKET = "]";

    // 字符串模板
    private static final String TEMPLATE = "{\"person_id\":\"\",\"longitude\":%f,\"latitude\":%f}";

    /**
     * 根据经纬度和人数生成人员随机数组字符串
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @param personNum 人数
     * @return [{"person_id":%s,"longitude":%f,"latitude":%f}]
     */
    public String eval(Double longitude, Double latitude, Integer personNum) {
        if (personNum == 0 || longitude == null || latitude == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("personNum is 0 or latitude is null or longitude is null");
            }
            return "[]";
        }
        StringBuilder result = new StringBuilder();
        result.append(LEFT_BRACKET);
        // 返回字符串模板
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < personNum; i++) {
            // 经度随机波动
            Double newLongitude = (random.nextInt(200) / 1000000D) - LATITUDE_WAVE;
            // 纬度随机波动
            Double newLatitude = (random.nextInt(160) / 1000000D) - LONGITUDE_WAVE;
            // 经度
            BigDecimal randLongitude = new BigDecimal(longitude + newLongitude + "");
            // 纬度
            BigDecimal randLatitude = new BigDecimal(latitude + newLatitude + "");
            result.append(String.format(Locale.ROOT, TEMPLATE, randLongitude, randLatitude));
            if (i < personNum - 1) {
                result.append(SEPARATOR);
            } else {
                result.append(RIGHT_BRACKET);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("result String '{}'", result);
        }
        return result.toString();
    }

    @Override
    public boolean isDeterministic() {
        return false;
    }
}
