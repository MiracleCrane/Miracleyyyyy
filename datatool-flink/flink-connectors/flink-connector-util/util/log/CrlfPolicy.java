/*
 * 文 件 名:  CrlfPolicy.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2022/12/16
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.util.log;

import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.message.SimpleMessage;

/**
 * 自定义日志插件，日志的打印内容进行处理，避免日志注入问题
 *
 * @author wwx643278
 * @version [SmartCampus V100R001C00, 2022/12/21]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Plugin(name = "CrlfPolicy", category = Core.CATEGORY_NAME, elementType = "rewritePolicy", printObject = true)
public class CrlfPolicy implements RewritePolicy {
    @PluginFactory
    public static CrlfPolicy createPolicy() {
        return new CrlfPolicy();
    }

    @Override
    public LogEvent rewrite(LogEvent logEvent) {
        if (!(logEvent instanceof Log4jLogEvent)) {
            return logEvent;
        }
        Log4jLogEvent log4jLogEvent = (Log4jLogEvent) logEvent;
        Message message = log4jLogEvent.getMessage();
        if (logEvent.getThrown() != null) {
            log4jLogEvent.asBuilder().setThrownProxy(new EncodeThrowableProxy(log4jLogEvent.getThrown())).build();

            log4jLogEvent = log4jLogEvent.asBuilder()
                    .setThrownProxy(new EncodeThrowableProxy(log4jLogEvent.getThrown())).build();
        }
        if (message instanceof SimpleMessage) {
            return escapeSimpleMessage(log4jLogEvent, (SimpleMessage) message);
        }
        if (message instanceof ParameterizedMessage) {
            return escapeParameterizedMessage(log4jLogEvent, (ParameterizedMessage) message);
        }
        return logEvent;
    }

    private LogEvent escapeSimpleMessage(Log4jLogEvent log4jLogEvent, SimpleMessage message) {
        SimpleMessage sm = new SimpleMessage(escapeJava(message.getFormattedMessage()));
        return log4jLogEvent.asBuilder().setMessage(sm).build();
    }

    private LogEvent escapeParameterizedMessage(Log4jLogEvent log4jLogEvent, ParameterizedMessage message) {
        Object[] params = message.getParameters();
        if (params == null || params.length == 0) {
            return log4jLogEvent;
        }
        Object[] newParams = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof String) {
                param = escapeJava((String) param);
            }
            newParams[i] = param;
        }
        ParameterizedMessage pm = new ParameterizedMessage(escapeJava(message.getFormat()), newParams,
                message.getThrowable());
        return log4jLogEvent.asBuilder().setMessage(pm).build();
    }

    /**
     * 对会引起注入问题编码做处理
     * 替代StringEscapeUtils.escapeJava，该工具类已被迁移到org.apache.commons.text，使用需要引入新的依赖
     * 
     * @param input 原字符串
     * @return 处理后字符串
     */
    public static String escapeJava(String input) {
        return input == null
                ? ""
                : input.replace("\n", "\\n").replace("\r", "\\r").replace("\b", "\\b").replace("\t", "\\t")
                        .replace("\f", "\\f").replace('\u007f', '_').replace('\u000b', '_');

    }

    // 对异常做特定编码处理的格式转换
    static class EncodeThrowableProxy extends ThrowableProxy {
        private static final long serialVersionUID = -1823695577856539014L;

        public EncodeThrowableProxy(Throwable throwable) {
            super(throwable);
        }

        // 重写getMessage方法，避免日志注入
        @Override
        public String getMessage() {
            return escapeJava(super.getMessage());
        }
    }
}
