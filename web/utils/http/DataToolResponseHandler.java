/*
 * 文 件 名:  DataToolResponseHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30006786
 * 修改时间： 2022/1/27
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils.http;

import org.apache.hc.client5.http.impl.classic.AbstractHttpClientResponseHandler;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

/**
 * DataTool处理http请求的响应信息
 * 统一处理http response，统一关闭response
 *
 * @author l30006786
 * @version [SmartCampus V100R001C00, 2022/1/27]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class DataToolResponseHandler extends AbstractHttpClientResponseHandler<String> {
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Handle the response entity and transform it into the actual response
     * object.
     *
     * @param entity HttpEntity
     * @return EntityUtils.toString(entity, DEFAULT_ENCODING)
     * @throws IOException IOException
     */
    @Override
    public String handleEntity(HttpEntity entity) throws IOException {
        try {
            return EntityUtils.toString(entity, DEFAULT_ENCODING);
        } catch (ParseException var3) {
            throw new IOException(var3);
        }
    }
}
