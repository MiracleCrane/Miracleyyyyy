/*
 * 文 件 名:  FileUtil.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/22
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * 文件工具类
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/22]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public final class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 删除文件目录
     *
     * @param directory 文件目录
     */
    public static void deleteDirectory(String directory) {
        try {
            File srcDirectory = new File(directory);
            FileUtils.deleteDirectory(srcDirectory);
        } catch (IOException e) {
            LOGGER.error("delete directory fail ", e);
        }
    }
}