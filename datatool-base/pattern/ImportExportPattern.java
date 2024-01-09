/*
 * 文 件 名:  ImportExportPattern.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/10/21
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.pattern;

import java.util.regex.Pattern;

/**
 * 正则表达式类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/10/21]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ImportExportPattern {
    /**
     * zip文件名称
     */
    public static final Pattern ZIP_FILE_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9\\u4e00-\\u9fa5-_()（）\\s]{1,128}$");

    /**
     * 校验导入zip包里文件，全路径只能包含英文字母、数字、中文、“-”、“_”、“.”，还有代表路径的分隔符号
     */
    public static final Pattern ZIP_CHILD_FILE_PATH_PATTERN = Pattern
            .compile("^[a-zA-Z0-9_\\-\\u4e00-\\u9fa5\\./\\\\]+$");

    /**
     * 数据连接名称
     */
    public static final Pattern CONN_NAME_PATTERN = Pattern.compile("^[^&\\s<>\"\\'()\\x22]{1,50}$");

    /**
     * 数据连接IP
     */
    public static final Pattern CONN_HOST_PATTERN = Pattern
            .compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    /**
     * 环境变量名称、数据连接用户名称，校验正则
     */
    public static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\-0-9_]{1,64}$");

    /**
     * 密码箱名称
     */
    public static final Pattern CIPHER_NAME_PATTERN = Pattern.compile("^[a-zA-Z\\.\\@\\-0-9_]{1,500}$");

    /**
     * 单个文件名称
     */
    public static final Pattern SINGLE_FILE_NAME = Pattern.compile("^[a-zA-Z\\-\\d_.]{1,128}$");

    /**
     * 流作业名称
     */
    public static final Pattern STREAM_NAME_PATTERN = Pattern.compile("^[\\w\\-]{1,57}$");

    /**
     * 流作业描述
     */
    public static final Pattern STREAM_DESC_PATTERN = Pattern.compile("^.{0,512}$");

    /**
     * 流作业重试次数
     */
    public static final Pattern STREAM_RETRY_TIMES_PATTERN = Pattern.compile("^(?:0|5|10|20|50|100)$");

    /**
     * 流作业并行度
     */
    public static final Pattern STREAM_PARALLELISM_PATTERN = Pattern.compile("^(?:1|2|3|4)$");

    /**
     * cron正则表达式
     */
    public static final Pattern CRON_EXPRESSION_PATTERN = Pattern.compile("^[,\\-0-9\\?\\ \\*\\/]{9,}$");

    /**
     * 目录名称非法
     */
    public static final Pattern DIR_NAME_PATTERN = Pattern.compile("^[a-zA-Z\\-0-9_\\u4e00-\\u9fa5]{1,32}$");

    /**
     * 脚本名称非法
     */
    public static final Pattern SCRIPT_NAME_PATTERN = Pattern.compile("^[.a-zA-Z\\-\\d_]{1,128}$");

    /**
     * 作业名称非法
     */
    public static final Pattern JOB_NAME_PATTERN = Pattern.compile("^[.a-zA-Z\\-0-9_]{1,128}$");

    /**
     * 节点名称非法
     */
    public static final Pattern NODE_NAME_PATTERN = Pattern.compile("^[-_a-zA-Z0-9.]{1,128}$");

    /**
     * 脚本内容中脚本参数的正则
     */
    public static final Pattern SCRIPT_ARGS_PATTERN = Pattern.compile("\\$\\s?\\{\\s?\\S+?\\s?\\}");

    /**
     * chkMode字段
     */
    public static final Pattern CHK_MODE_PATTERN = Pattern.compile("^(?:0|1)$");

    /**
     * 目录路径
     */
    public static final Pattern PATH_PATTERN = Pattern.compile("^\\/$|^((\\/([a-zA-Z0-9\\u4e00-\\u9fa5_-]+))+)$");
}