/*
 * 文 件 名:  I18nUtils.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.0.T22
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/7
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.i18n;

import com.huawei.smartcampus.datatool.properties.I18nConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * 国际化工具类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/7]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class I18nUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(I18nUtils.class);
    private static ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

    static {
        messageSource.setDefaultEncoding(I18nConfig.messagesEncoding());
        messageSource.setBasenames(new String[]{I18nConfig.messagesBasename()});
    }

    /**
     * 本方法用以根据异常码和语言上下文，从国际化文件获取详细错误信息，默认US
     *
     * @param code 错误码
     * @param params 传入参数
     * @return 返回完整I18n异常信息
     */
    public static String getMessageUS(String code, Object... params) {
        return getMessage(Locale.US, code, params);
    }

    /**
     * 本方法用以根据异常码和语言上下文，从国际化文件获取详细错误信息
     *
     * @param code 错误码
     * @param params 传入参数
     * @param locale 多语言
     * @return 返回完整I18n异常信息
     */
    public static String getMessage(Locale locale, String code, Object... params) {
        String message = "";
        try {
            message = messageSource.getMessage(code, params, locale);
        } catch (NoSuchMessageException e) {
            LOGGER.warn("Cannot find the i18n message of {}, return the original error message.", code);
        }
        return message;
    }

    /**
     * 本方法用以根据异常码和语言上下文，从国际化文件获取详细错误信息，默认自动从上下文获取语言类型
     *
     * @param code 错误码
     * @param params 传入参数
     * @return 返回完整I18n异常信息
     */
    public static String getMessage(String code, Object... params) {
        return getMessage(LocaleContextHolder.getLocale(), code, params);
    }
}
