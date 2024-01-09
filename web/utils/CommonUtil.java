/*
 * 文 件 名:  CommonUtil.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  yWX890060
 * 修改时间： 2021/4/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;

import com.google.common.collect.Lists;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * 通用工具类
 *
 * @author yWX890060
 * @version [SmartCampus V100R001C00, 2021/4/16]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public final class CommonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);
    private static final List<String> SCHEME_WHITELIST = Lists.newArrayList("https");
    private static final List<Integer> PORT_WHITELIST = Lists.newArrayList(8080, 8081, 8083);

    /**
     * 将字符串编码成base64格式
     *
     * @param origin 输入内容
     * @return base64转码后的内容
     */
    public static String base64EncodeString(String origin) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] textByte = origin.getBytes(StandardCharsets.UTF_8);
        return encoder.encodeToString(textByte);
    }

    /**
     * 将目标base64格式字符串解码
     *
     * @param target 输入内容
     * @return base64解码后的内容
     */
    public static String base64Decode(String target) {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(target), StandardCharsets.UTF_8);
    }

    /**
     * 等待
     *
     * @param timeout 等待时长，单位毫秒
     */
    public static void waiting(int timeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            LOGGER.error("Polling wait error:{}", e);
        }
    }

    /**
     * 获取当前Timestamp
     *
     * @return Timestamp格式时间戳
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 字符串转为 long
     * 
     * @param obj object
     * @return long
     */
    public static Long convertToLong(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj instanceof Long) {
            return (Long) obj;
        }

        if (obj instanceof String) {
            String str = (String) obj;
            if (str.isEmpty()) {
                return null;
            }

            try {
                return Long.parseLong(str);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null; // 如果无法转换为Long类型，则返回null
    }

    /**
     * 参数处理
     *
     * @param str 参数
     * @return 处理结果参数
     */
    public static String parameterEscape(String str) {
        if (StringUtils.isEmpty(str)) {
            // 如果为空，返回空串，否则外部拼接的是会拼接null
            return "";
        }
        return str.replace("%", "\\%").replace("_", "\\_");
    }

    /**
     * 在HTTP协议中，HTTP Header与HTTP Body是用两个CRLF分隔的，浏览器就是根据这两个CRLF来取出HTTP 内容并显示出来。
     * 所以，一旦我们能够控制HTTP 消息头中的字符，注入一些恶意的换行，这样我们就能注入一些会话Cookie或者HTML代码。
     * 清理方式是对数据中的\r或者\n进行替换。
     *
     * @param message 消息
     * @return string
     */
    public static String replaceCRLF(String message) {
        if (message == null) {
            return "";
        }
        return message.replace("\r", "").replace("\n", "");
    }

    /**
     * 解决SSRF漏洞，对flink拼接的url进行过滤
     *
     * @param url 接口url
     * @return URI
     */
    public static URI encodeForURL(String url) {
        // 校验协议头:scheme
        URI uri = URI.create(url);
        String scheme = uri.getScheme();
        if (Strings.isEmpty(scheme) || !SCHEME_WHITELIST.contains(scheme.toLowerCase(Locale.ROOT))) {
            LOGGER.error("scheme[{}]illegal, should be http/https.");
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_URL_SCHEME_ERROR);
        }
        if (!PORT_WHITELIST.contains(uri.getPort())) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_URL_PORT_ERROR);
        }
        return uri;
    }
}
