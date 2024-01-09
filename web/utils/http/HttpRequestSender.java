/*
 * 文 件 名:  HttpRequestSender.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/11/24
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils.http;

import static org.apache.hc.core5.http.HttpHeaders.CONTENT_TYPE;

import com.huawei.smartcampus.datatool.constant.Constant;
import com.huawei.smartcampus.datatool.utils.CommonUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 机机接口调用封装多语言
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/11/24]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class HttpRequestSender {
    /**
     * 发送post请求，头部携带多语言信息
     *
     * @param baseUrl 请求路径
     * @param params 参数
     * @return JSONObject
     * @throws IOException IOException
     */
    public static JSONObject doPostWithLocale(String baseUrl, Object params) throws IOException {
        HttpPost httpPost = new HttpPost(CommonUtil.encodeForURL(baseUrl));
        httpPost.setHeader(Constant.LANGUAGE_KEY, LocaleContextHolder.getLocale());
        httpPost.setHeader(CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        if (params != null) {
            httpPost.setEntity(new StringEntity(JSON.toJSONString(params), StandardCharsets.UTF_8));
        }
        return JSONObject.parseObject(HttpClientPool.getHttpClient().execute(httpPost, new DataToolResponseHandler()));
    }
}