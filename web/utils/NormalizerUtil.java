/*
 * 文 件 名:  NormalizerUtil.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/8/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

import java.text.Normalizer;

/**
 * 标准化工具类
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/8/1]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class NormalizerUtil {
    /**
     * 过滤不安全的特殊字符
     *
     * @param item 待检字符串
     * @return String 检验后的字符串
     */
    public static String normalizeForString(String item) {
        String result = null;
        if (null == item) {
            return result;
        }
        result = Normalizer.normalize(item, Normalizer.Form.NFKC);
        return result;
    }
}