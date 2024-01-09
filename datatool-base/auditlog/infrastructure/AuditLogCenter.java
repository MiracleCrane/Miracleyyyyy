/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.base.auditlog.infrastructure;

import com.huawei.hicampus.campuscommon.auditlog.entity.AuditLog;
import com.huawei.hicampus.campuscommon.common.util.Constant;
import com.huawei.hicampus.campuscommon.common.util.DateUtils;
import com.huawei.smartcampus.datatool.base.auditlog.gateway.AuditLogGateway;
import com.huawei.smartcampus.datatool.properties.AuditLogProperties;
import com.huawei.smartcampus.datatool.utils.StringUtils;
import com.huawei.smartcampus.datatool.utils.http.DataToolResponseHandler;
import com.huawei.smartcampus.datatool.utils.http.HttpClientPool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 用于调用园区审计日志服务
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/9/23]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
@Service
public class AuditLogCenter implements AuditLogGateway {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogCenter.class);

    private static final String AUDIT_LOG_CENTER_RECORD_URL = "/service/AuditLog/0.1.0/log";

    private static final String AUDIT_LOG_CENTER_BATCH_RECORD_URL = "/service/AuditLog/0.1.0/log/batch";

    @Override
    public boolean sendAuditLog(AuditLog auditLog) {
        if (auditLog == null) {
            return true;
        }
        return sendAddAuditLogRestRequest(Collections.singletonList(auditLog), false);
    }

    @Override
    public boolean sendBatchAuditLog(List<AuditLog> auditLogs) {
        if (CollectionUtils.isEmpty(auditLogs)) {
            return true;
        }
        return sendAddAuditLogRestRequest(auditLogs, true);
    }

    private boolean sendAddAuditLogRestRequest(List<AuditLog> auditLogs, boolean isBatch) {
        String response;

        try {
            // 构建请求参数--url、header、body
            String url = getUrl(isBatch);
            Map<String, Object> body = initBody(auditLogs, isBatch);

            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(JSONObject.toJSONString(body), ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            response = HttpClientPool.getHttpClient().execute(httpPost, new DataToolResponseHandler());
        } catch (Exception e) {
            LOGGER.error("request auditlog center to record auditlog failed with exception.", e);
            return false;
        }

        // 处理请求
        return processResponse(response);
    }

    private String getUrl(boolean isBatch) {
        String path = isBatch ? AUDIT_LOG_CENTER_BATCH_RECORD_URL : AUDIT_LOG_CENTER_RECORD_URL;
        String host = AuditLogProperties.auditAddress();
        return host + path;
    }

    private Map<String, Object> initBody(List<AuditLog> auditLogs, boolean isBatch) {
        Map<String, Object> bodyMap = new HashMap<>();
        if (CollectionUtils.isEmpty(auditLogs)) {
            return bodyMap;
        }

        if (!isBatch) {
            addAuditLogParam(bodyMap, auditLogs.get(0));
        } else {
            List<Map<String, Object>> auditLogBodyList = new LinkedList<>();
            for (AuditLog auditLog : auditLogs) {
                Map<String, Object> auditLogMap = new HashMap<>();
                addAuditLogParam(auditLogMap, auditLog);
                auditLogBodyList.add(auditLogMap);
            }
            bodyMap.put("auditLogList", auditLogBodyList);
        }
        return bodyMap;
    }

    private void addAuditLogParam(Map<String, Object> map, AuditLog auditLog) {
        // 获取基本信息
        addMapParam(map, "module", auditLog.getModule());
        addMapParam(map, "operationName", auditLog.getOperationName());
        addMapParam(map, "operationObject", auditLog.getOperationObject());
        addMapParam(map, "resultCode", auditLog.getResultCode());
        addMapParam(map, "resultMessage", auditLog.getResultMessage());
        addMapParam(map, "detail", auditLog.getDetail());
        addMapParam(map, "type", auditLog.getType());
        addMapParam(map, "riskLevel", auditLog.getRiskLevel());
        addMapParam(map, "requestDate",
                DateUtils.convertDate2String(auditLog.getRequestDate(), Constant.DEFAUT_DATETIME_FORMAT));
        addMapParam(map, "userName", auditLog.getUserName());
        addMapParam(map, "userId", auditLog.getUserId());
        addMapParam(map, "clientIp", auditLog.getClientIp());
        if (StringUtils.isNotEmpty(auditLog.getUniqueId())) {
            addMapParam(map, "uniqueId", auditLog.getUniqueId());
        }
    }

    private void addMapParam(Map<String, Object> map, String paramName, Object paramValue) {
        if (paramValue != null) {
            map.put(paramName, paramValue);
        }
    }

    private boolean processResponse(String response) {
        if (response == null) {
            LOGGER.error("request audit log center failed.");
            return false;
        }
        JSONObject responseJson = JSON.parseObject(response);
        // resCode为0,记录成功，直接返回
        if (Constant.RESULT_CODE_SUCCESS.equals(responseJson.getString("resCode"))) {
            return true;
        }

        // resCode不为0，记录错误日志
        LOGGER.error("record audit log failed with resCode: {}", responseJson.getString("resCode"));
        return false;
    }
}