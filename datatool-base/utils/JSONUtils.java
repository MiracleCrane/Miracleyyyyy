/*
 * 文 件 名:  JSONUtils.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/10/30
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.utils;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * json格式，工具类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/10/30]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class JSONUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);

    /**
     * 获取json中String类型的字段
     * 不存在，默认为空字符串。
     *
     * @param jsonObject json对象
     * @param strName 提取字段名
     * @return 字段值
     */
    public static String getStringOrDefaultEmpty(JSONObject jsonObject, String strName) {
        String result;
        try {
            result = jsonObject.getString(strName);
            if (result == null) {
                result = "";
            }
        } catch (Exception e) {
            LOGGER.error("getStringOrDefaultEmpty fail", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_UNKNOWN_ERROR, strName);
        }
        return result;
    }

    /**
     * 获取json中String类型的字段
     * 不存在，或者为空，抛出缺少参数或为空异常。
     *
     * @param jsonObject json对象
     * @param strName 提取字段名
     * @return 字段值
     */
    public static String getStringOrErrorHandler(JSONObject jsonObject, String strName) {
        String result;
        try {
            result = jsonObject.getString(strName);
            if (result == null) {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_MISSING, strName);
            }
            if ("".equals(result)) {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_EMPTY, strName);
            }
            return result;
        } catch (DataToolRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("getStringOrErrorHandler fail", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_UNKNOWN_ERROR, strName);
        }
    }

    /**
     * 获取json中Integer类型的字段
     * 不存在，抛出缺少参数异常；非Integer类型，抛出参数类型错误异常。
     *
     * @param jsonObject json对象
     * @param strName 提取字段名
     * @return 字段值
     */
    public static Integer getIntegerOrErrorHandler(JSONObject jsonObject, String strName) {
        Integer result;
        try {
            result = jsonObject.getInteger(strName);
            if (result == null) {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_MISSING, strName);
            }
        } catch (DataToolRuntimeException e) {
            throw e;
        } catch (NumberFormatException e) {
            LOGGER.error("getIntegerOrErrorHandler fail", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_INVALID_PARAM_TYPE, strName);
        } catch (Exception e) {
            LOGGER.error("getIntegerOrErrorHandler fail", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_UNKNOWN_ERROR, strName);
        }
        return result;
    }

    /**
     * 获取json中Integer类型的字段
     * 不存在，抛出缺少参数异常；非Integer类型，抛出参数类型错误异常。
     *
     * @param jsonObject json对象
     * @param strName 提取字段名
     * @return 字段值
     */
    public static Integer getIntegerOrDefaultZero(JSONObject jsonObject, String strName) {
        Integer result;
        try {
            result = jsonObject.getInteger(strName);
            if (result == null) {
                result = 0;
            }
        } catch (NumberFormatException e) {
            LOGGER.error("getIntegerOrDefaultZero fail", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_INVALID_PARAM_TYPE, strName);
        } catch (Exception e) {
            LOGGER.error("getIntegerOrDefaultZero fail", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_UNKNOWN_ERROR, strName);
        }
        return result;
    }

    /**
     * 获取json中Date日期类型的字段
     * 不存在，抛出缺少参数异常；非Date类型，抛出参数类型错误异常。
     *
     * @param jsonObject json对象
     * @param strName 提取字段名
     * @return 字段值
     */
    public static Date getDateOrErrorHandler(JSONObject jsonObject, String strName) {
        Date date;
        try {
            date = jsonObject.getDate(strName);
            if (date == null) {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_MISSING, strName);
            }
        } catch (DataToolRuntimeException e) {
            throw e;
        } catch (NumberFormatException | JSONException e) {
            LOGGER.error("getDateOrErrorHandler fail", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_INVALID_PARAM_TYPE, strName);
        } catch (Exception e) {
            LOGGER.error("getDateOrErrorHandler fail", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_UNKNOWN_ERROR, strName);
        }
        return date;
    }

    /**
     * 获取json中Date日期类型的字段
     * 存在，或者为空不报错，返回null；非Date类型，抛出参数类型错误异常。
     *
     * @param jsonObject json对象
     * @param strName 提取字段名
     * @return 字段值
     */
    public static Date getDate(JSONObject jsonObject, String strName) {
        Date date;
        try {
            date = jsonObject.getDate(strName);
        } catch (NumberFormatException | JSONException e) {
            LOGGER.error("getDate fail", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_INVALID_PARAM_TYPE, strName);
        } catch (Exception e) {
            LOGGER.error("getDate fail", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_UNKNOWN_ERROR, strName);
        }
        return date;
    }

    /**
     * 获取json中布尔类型的字段
     * 不存在，抛出缺少参数异常；非Boolean类型，抛出参数类型错误异常。
     *
     * @param jsonObject json对象
     * @param strName 提取字段名
     * @return 字段值
     */
    public static Boolean getBooleanOrErrorHandler(JSONObject jsonObject, String strName) {
        Boolean result;
        try {
            result = jsonObject.getBoolean(strName);
            if (result == null) {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_MISSING, strName);
            }
        } catch (DataToolRuntimeException e) {
            throw e;
        } catch (NumberFormatException | JSONException e) {
            LOGGER.error("getBooleanOrErrorHandler fail", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_INVALID_PARAM_TYPE, strName);
        } catch (Exception e) {
            LOGGER.error("getBooleanOrErrorHandler fail", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_UNKNOWN_ERROR, strName);
        }
        return result;
    }

    /**
     * 获取json中JSONArray类型的字段
     * 不存在，抛出缺少参数异常。
     *
     * @param jsonObject json对象
     * @param strName 提取字段名
     * @return 字段值
     */
    public static JSONArray getJSONArrayOrErrorHandler(JSONObject jsonObject, String strName) {
        JSONArray result;
        try {
            result = jsonObject.getJSONArray(strName);
            if (result == null) {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_MISSING, strName);
            }
            if (result.size() == 0) {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_LIST_EMPTY, strName);
            }
            return result;
        } catch (DataToolRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("getJSONArrayOrErrorHandler fail", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_UNKNOWN_ERROR, strName);
        }
    }

    /**
     * 获取json中JSONObject类型的字段
     * 不存在，抛出缺少参数异常。
     *
     * @param jsonObject json对象
     * @param strName 提取字段名
     * @return 字段值
     */
    public static JSONObject getJSONObjectOrErrorHandler(JSONObject jsonObject, String strName) {
        JSONObject result;
        try {
            result = jsonObject.getJSONObject(strName);
            if (result == null) {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_MISSING, strName);
            }
            return result;
        } catch (DataToolRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("getJSONObjectOrErrorHandler fail", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_UNKNOWN_ERROR, strName);
        }
    }
}