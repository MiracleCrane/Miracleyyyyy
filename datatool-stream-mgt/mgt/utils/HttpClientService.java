/*
 * 文 件 名:  HttpClientService.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.utils;

import com.huawei.smartcampus.datatool.utils.ParamsCheckUtil;
import com.huawei.smartcampus.datatool.utils.http.DataToolResponseHandler;
import com.huawei.smartcampus.datatool.utils.http.HttpClientPool;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Component
public class HttpClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientService.class);

    private static final String CONTENT_TYPE = "Content-Type";

    private volatile String jarId;

    @Value("${httpclient.connect.timeout}")
    private int connectTimeout;

    @Value("${httpclient.connection.request.timeout}")
    private int connectionRequestTimeout;

    /**
     * HttpClientService
     */
    public HttpClientService() {
    }

    /**
     * getRemoteSqlJarId
     *
     * @param remoteUrl remoteUrl
     * @return jarId
     * @throws IOException IOException
     * @throws URISyntaxException URISyntaxException
     */
    public String getRemoteSqlJarId(String remoteUrl) throws IOException, URISyntaxException {
        if (jarId == null) {
            synchronized (this) {
                if (jarId == null) {
                    JSONObject result = doHttp(remoteUrl + "/jars", null, HttpMethod.GET);
                    this.jarId = result.getJSONArray("files").getJSONObject(0).getString("id");
                }
            }
        }
        return this.jarId;
    }

    /**
     * doHttp
     *
     * @param baseUrl 请求路径
     * @param params 参数
     * @param method 请求方式
     * @return JSONObject
     * @throws IOException io异常
     * @throws URISyntaxException URISyntax异常
     */
    public JSONObject doHttp(String baseUrl, JSONObject params, HttpMethod method)
            throws IOException, URISyntaxException {
        if (method == HttpMethod.POST) {
            return doPost(baseUrl, params);
        }
        if (method == HttpMethod.PATCH) {
            return doPatch(baseUrl, params);
        }
        return doGet(baseUrl, params);
    }

    /**
     * doGet
     *
     * @param baseUrl 请求路径
     * @param params 参数
     * @return JSONObject
     * @throws IOException IOException
     * @throws URISyntaxException URISyntaxException
     */
    public JSONObject doGet(String baseUrl, JSONObject params) throws IOException, URISyntaxException {
        return JSONObject.parseObject(doGetRequest(baseUrl, params));
    }

    /**
     * doPost
     *
     * @param baseUrl 请求路径
     * @param params 参数
     * @return JSONObject
     * @throws IOException IOException
     */
    public JSONObject doPost(String baseUrl, JSONObject params) throws IOException {
        ParamsCheckUtil.checkFlinkRequestUrl(baseUrl);
        HttpPost httpPost = new HttpPost(URI.create(baseUrl));
        httpPost.setHeader(CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        if (params != null) {
            httpPost.setEntity(new StringEntity(JSON.toJSONString(params), StandardCharsets.UTF_8));
        }
        return JSONObject.parseObject(HttpClientPool.getHttpClient().execute(httpPost, new DataToolResponseHandler()));
    }

    /**
     * doPatch
     *
     * @param baseUrl 请求路径
     * @param params 参数
     * @return JSONObject
     * @throws IOException IOException
     */
    public JSONObject doPatch(String baseUrl, JSONObject params) throws IOException {
        ParamsCheckUtil.checkFlinkRequestUrl(baseUrl);
        HttpPatch httpPatch = new HttpPatch(URI.create(baseUrl));
        httpPatch.setHeader(CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        if (params != null) {
            httpPatch.setEntity(new StringEntity(JSON.toJSONString(params), StandardCharsets.UTF_8));
        }
        return JSONObject.parseObject(HttpClientPool.getHttpClient().execute(httpPatch, new DataToolResponseHandler()));
    }

    private String doGetRequest(String baseUrl, JSONObject params) throws IOException, URISyntaxException {
        HttpGet httpGet = buildHttpGet(baseUrl, params);
        httpGet.setHeader(CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        return HttpClientPool.getHttpClient().execute(httpGet, new DataToolResponseHandler());
    }

    private HttpGet buildHttpGet(String baseUrl, JSONObject params) throws URISyntaxException {
        ParamsCheckUtil.checkFlinkRequestUrl(baseUrl);
        URIBuilder uriBuilder = new URIBuilder(baseUrl);
        if (params != null) {
            Map<String, Object> map = params.getInnerMap();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeout, TimeUnit.MILLISECONDS)
                .setConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS).build();
        httpGet.setConfig(requestConfig);
        return httpGet;
    }
}
