/*
 * 文 件 名:  CommonUtils.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/11
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.utils;

import com.huawei.smartcampus.datatool.base.enumeration.DuplicatePolicyEnum;
import com.huawei.smartcampus.datatool.base.enumeration.ImportModeEnum;
import com.huawei.smartcampus.datatool.base.vo.req.ImportReq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;


import java.util.Locale;

/**
 * 通用的工具类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/11]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class CommonUtils {
    private CommonUtils() {
    }

    /**
     * 是否跳过转换
     *
     * @param duplicatePolicy 覆盖策略
     * @return 布尔值是否跳过
     */
    public static boolean isSkip(String duplicatePolicy) {
        return DuplicatePolicyEnum.SKIP.duplicatePolicy().equals(duplicatePolicy);
    }

    /**
     * 导入模式
     *
     * @param importReq 导入入参
     * @return 导入模式
     */
    public static String importMode(ImportReq importReq) {
        return importReq.getImportMode() != null && importReq.getImportMode().equals(ImportModeEnum.MANUAL.importMode())
                ? ImportModeEnum.MANUAL.importMode()
                : ImportModeEnum.AUTO.importMode();
    }

    /**
     * 转换属性值，将未取到的值赋值空字符串
     *
     * @param value 转换的值
     */
    public static String transformProperty(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    /**
     * 获取dgc的数据连接类型
     *
     * @param type 传入连接类型
     * @return 连接类型
     */
    public static String getDgcDBType(String type) {
        return "dws".equals(type.toLowerCase(Locale.ROOT)) ? "opengauss" : type;
    }

    /**
     * 将对象转换成json的字符串
     * 
     * @param obj 对象
     * @return 字符串
     */
    public static String transObjectToJsonString(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 当文件名称超过100时，使用...取代后面的值
     *
     * @param name 文件名称
     * @return 取代后的值
     */
    public static String getNameWithinRange(String name) {
        if (name.length() > 100) {
            name = name.substring(0, 100) + "...";
        }
        return name;
    }
}