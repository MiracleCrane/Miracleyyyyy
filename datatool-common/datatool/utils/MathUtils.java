/*
 * 文 件 名:  MathUtils.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/27
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

import java.math.BigDecimal;

/**
 * 数学运算工具类
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/27]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public final class MathUtils {
    /**
     * 获取两个整数的商，四舍五入保留2位小数
     *
     * @param dividend 被除数
     * @param divisor 除数
     * @return 商
     */
    public static double getQuotient(int dividend, int divisor) {
        if (divisor == 0) {
            return 0;
        }
        BigDecimal dividendBd = new BigDecimal(dividend);
        BigDecimal divisorBd = new BigDecimal(divisor);
        return dividendBd.divide(divisorBd, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}