/*
 * 文 件 名:  InputStreamResponseHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/10/26
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils.http;

import org.apache.hc.client5.http.impl.classic.AbstractHttpClientResponseHandler;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 输入流响应处理方法
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/10/26]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class InputStreamResponseHandler extends AbstractHttpClientResponseHandler<InputStream> {
    /**
     * 输入流响应处理
     *
     * @param entity HttpEntity
     * @return 输入流
     * @throws IOException IOException
     */
    @Override
    public InputStream handleEntity(HttpEntity entity) throws IOException {
        return new ByteArrayInputStream(EntityUtils.toByteArray(entity));
    }
}