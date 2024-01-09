/*
 * 文 件 名:  ParamsCheckUtil.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  yWX895180
 * 修改时间： 2021/9/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;

import java.util.regex.Pattern;

/**
 * 参数检查工具类
 *
 * @author yWX895180
 * @version [SmartCampus V100R001C00, 2021/9/28]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public final class ParamsCheckUtil {
    private static final Pattern FLINK_REQUEST_URL_PATTERN = Pattern
            .compile("^http[s]?:\\/\\/([\\w-]+\\.)*[\\w-]+(:[0-9]+)?(\\/[\\w\\-.\\/?%&=:]*)?$");

    private ParamsCheckUtil() {
    }

    /**
     * Flink相关的完整请求路径校验
     *
     * @param url Flink相关的请求路径
     * @return 请求路径
     */
    public static String checkFlinkRequestUrl(String url) {
        if (!FLINK_REQUEST_URL_PATTERN.matcher(url).matches()) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_REQUEST_URL_ERROR);
        }
        return url;
    }
}
