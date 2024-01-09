/*
 * 文 件 名:  QuantitativeConfig.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/10/26
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.properties;

/**
 * 公共配置
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/10/26]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class QuantitativeConfig {
    private static final ApplicationProperties PROPERTIES = ApplicationProperties.instance();
    private static final String MAX_STREAM_JOBS = "max_stream_jobs";
    private static final String MAX_ENVIRONMENT_VARIABLES = "max_environment_variables";
    private static final String MAX_CIPHER_VARIABLES = "max_cipher_variables";
    private static final String MAX_SCRIPTS = "max_scripts";
    private static final String MAX_SCRIPT_DIRECTORIES = "max_script_directories";
    private static final String MAX_SCRIPT_DIRECTORY_DEPTH = "max_script_directory_depth";
    private static final String MAX_JOBS = "max_jobs";
    private static final String MAX_JOB_DIRECTORIES = "max_job_directories";
    private static final String MAX_JOB_DIRECTORY_DEPTH = "max_job_directory_depth";
    private static final String MAX_CONNECTIONS = "max_connections_limit";

    public static int getMaxStreamJobs() {
        return PROPERTIES.getInt(MAX_STREAM_JOBS);
    }

    public static int getMaxEnvironmentVariables() {
        return PROPERTIES.getInt(MAX_ENVIRONMENT_VARIABLES);
    }

    public static int getMaxCipherVariables() {
        return PROPERTIES.getInt(MAX_CIPHER_VARIABLES);
    }

    public static int getMaxScripts() {
        return PROPERTIES.getInt(MAX_SCRIPTS);
    }

    public static int getMaxScriptDirectories() {
        return PROPERTIES.getInt(MAX_SCRIPT_DIRECTORIES);
    }

    public static int getMaxScriptDirectoryDepth() {
        return PROPERTIES.getInt(MAX_SCRIPT_DIRECTORY_DEPTH);
    }

    public static int getMaxJobs() {
        return PROPERTIES.getInt(MAX_JOBS);
    }

    public static int getMaxJobDirectories() {
        return PROPERTIES.getInt(MAX_JOB_DIRECTORIES);
    }

    public static int getMaxJobDirectoryDepth() {
        return PROPERTIES.getInt(MAX_JOB_DIRECTORY_DEPTH);
    }

    public static int getMaxConnections(){
        return PROPERTIES.getInt(MAX_CONNECTIONS);
    }
}