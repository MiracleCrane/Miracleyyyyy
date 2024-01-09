/*
 * 文 件 名:  ExportFileNameEnum.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/11
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.enumeration;

/**
 * 导出文件名枚举类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/11]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public enum ExportTypeAndFileNameEnum {
    BATCH_JOB("job", "datatool_databatch_job.zip"),
    STREAM_JOB("stream", "datatool_datastream_job.zip"),
    ENV("env", "datatool_environment_variable.zip"),
    CONN("conn", "datatool_connection.zip"),
    CIPHER("cipher", "datatool_cipher_variable.zip"),
    SCRIPT("script", "datatool_databatch_script.zip");

    private final String type;
    private final String fileName;

    ExportTypeAndFileNameEnum(String type, String fileName) {
        this.type = type;
        this.fileName = fileName;
    }

    /**
     * 导出文件类型枚举值
     *
     * @return String
     */
    public String type() {
        return type;
    }

    /**
     * 导出文件名枚举值
     *
     * @return String
     */
    public String fileName() {
        return fileName;
    }

    public static String getFileNameByType(String type) {
        if (type == null) {
            return "";
        }
        for (ExportTypeAndFileNameEnum types : ExportTypeAndFileNameEnum.values()) {
            if (types.type.equals(type)) {
                return types.fileName;
            }
        }
        return "";
    }

    public static ExportTypeAndFileNameEnum getExportTypeAndFileNameEnumByType(String type) {
        if (type == null) {
            return null;
        }
        for (ExportTypeAndFileNameEnum typeAndFileNameEnum : ExportTypeAndFileNameEnum.values()) {
            if (typeAndFileNameEnum.type.equals(type)) {
                return typeAndFileNameEnum;
            }
        }
        return null;
    }
}
