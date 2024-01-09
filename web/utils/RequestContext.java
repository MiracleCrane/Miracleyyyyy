/*
 * 文 件 名:  RequestContext.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  cWX630741
 * 修改时间： 2021/7/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTPS请求的上下文类
 * 临时保存每次请求的关键事件要素，用于记录审计日志
 *
 * @author cWX630741
 * @version [SmartCampus V100R001C00, 2021/7/1]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public final class RequestContext {
    /**
     * 内部调用时的用户名
     */
    public static final String INTERNAL_NAME = "system";

    /**
     * 当前用户名字段
     */
    public static final String USER_NAME_FIELD = "currentUser";

    /**
     * 当前用户ID字段
     */
    public static final String USER_ID_FIELD = "currentUserId";

    /**
     * 当前用户类型字段
     */
    public static final String USER_TYPE_FIELD = "currentUserType";

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 机机用户的用户类型(0代表人机用户)
     */
    private static final String OAUTH_USER_TYPE = "1";

    /**
     * 机机用户的用户名
     */
    private static final String OAUTH_USER_NAME = "system";

    /**
     * 请求ID
     */
    public static final String REQUEST_ID = "requestId";

    /**
     * 请求IP
     */
    public static final String REQUEST_IP = "requestIp";

    /**
     * 请求时间
     */
    public static final String REQUEST_TIME = "requestTime";

    /**
     * 请求开始时间
     */
    public static final String REQUEST_START_TIME = "requestStartTime";

    /**
     * 操作名称
     */
    public static final String OPERATION_NAME = "operationName";

    /**
     * 指定操作对象
     */
    public static final String OPERATION_OBJECT = "operationObject";

    /**
     * 响应码
     */
    public static final String RES_CODE = "resCode";

    /**
     * 响应信息
     */
    public static final String RES_MSG = "resMsg";

    /**
     * 请求信息
     */
    public static final String REQUEST = "request";

    /**
     * 审计日志详情
     */
    public static final String REQUEST_DETAIL = "request_detail";

    private RequestContext() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * get 方法
     *
     * @param key key
     * @return value
     */
    public static Object get(String key) {
        Map<String, Object> stringObjectMap = THREAD_LOCAL.get();
        if (stringObjectMap == null) {
            return null;
        }
        return THREAD_LOCAL.get().get(key);
    }

    /**
     * set 方法
     *
     * @param key key
     * @param value value
     */
    public static void set(String key, Object value) {
        Map<String, Object> stringObjectMap = THREAD_LOCAL.get();
        if (stringObjectMap == null) {
            stringObjectMap = new HashMap<>();
            THREAD_LOCAL.set(stringObjectMap);
        }
        stringObjectMap.put(key, value);
    }

    /**
     * clear 方法
     */
    public static void clear() {
        THREAD_LOCAL.remove();
    }

    /**
     * 获取当前用户名
     *
     * @return 用户名
     */
    public static String getUserName() {
        String currentUser = (String) get(USER_NAME_FIELD);
        String currentUserType = (String) get(USER_TYPE_FIELD);

        // 如果用户名和用户类型都为空，则返回内部调用用户名
        if (StringUtils.isEmpty(currentUser) && StringUtils.isEmpty(currentUserType)) {
            return INTERNAL_NAME;
        }

        // 如果用户名或者用户类型存在一个为空，则抛出异常
        if (StringUtils.isEmpty(currentUser)) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_GET_CURRENT_USER_FAIL);
        }
        if (StringUtils.isEmpty(currentUserType)) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_GET_CURRENT_USER_TYPE_FAIL);
        }

        // 如果用户名或者用户类型都不为空，则判断是否是机机用户
        if (OAUTH_USER_TYPE.equals(currentUserType)) {
            currentUser = OAUTH_USER_NAME;
        }
        return currentUser;
    }

    /**
     * set audit log detail
     *
     * @param key param
     * @param value param value
     */
    public static void setRequestDetail(String key, Object value) {
        JSONObject requestDetail = getRequestDetail();
        JSONArray valueList;
        if (requestDetail.isEmpty()) {
            set(REQUEST_DETAIL, requestDetail);
        }

        if (requestDetail.containsKey(key)) {
            valueList = requestDetail.getJSONArray(key);
        } else {
            valueList = new JSONArray();
        }

        valueList.add(value);
        requestDetail.put(key, valueList);
    }

    /**
     * get audit log detail
     *
     * @return operation detail
     */
    public static JSONObject getRequestDetail() {
        JSONObject requestDetail = (JSONObject) get(REQUEST_DETAIL);
        if (requestDetail == null) {
            return new JSONObject();
        }
        return requestDetail;
    }
}
