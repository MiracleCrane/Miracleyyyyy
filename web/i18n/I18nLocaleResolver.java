/*
 * 文 件 名:  DataToolLocaleResolver.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.0.T22
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/7
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.i18n;

import com.huawei.smartcampus.datatool.constant.Constant;

import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 实现自己的解析器，用于从请求的头部获取语言值
 *
 * @author z00569896
 * @version [SmartCampus R23.0.T22, 2023/8/7]
 * @see [相关类/方法]
 * @since [SmartCampus R23.0.T22]
 */
public class I18nLocaleResolver implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale locale;
        String language = request.getHeader(Constant.LANGUAGE_KEY);
        if (Constant.LOCALE_EN.equals(language)) {
            locale = Locale.US;
        } else {
            locale = Locale.CHINA;
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}