/*
 * 文 件 名:  ExportHandlerFactory.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/11
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.handler.exportdata.factory;

import com.huawei.smartcampus.datatool.base.enumeration.ExportTypeAndFileNameEnum;
import com.huawei.smartcampus.datatool.base.handler.exportdata.ExportBatchJobHandler;
import com.huawei.smartcampus.datatool.base.handler.exportdata.ExportCipherHandler;
import com.huawei.smartcampus.datatool.base.handler.exportdata.ExportConnHandler;
import com.huawei.smartcampus.datatool.base.handler.exportdata.ExportEnvHandler;
import com.huawei.smartcampus.datatool.base.handler.exportdata.ExportScriptHandler;
import com.huawei.smartcampus.datatool.base.handler.exportdata.ExportStreamJobHandler;
import com.huawei.smartcampus.datatool.base.handler.exportdata.base.ExportHandler;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 导出处理器的工厂
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/11]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ExportHandlerFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportHandlerFactory.class);

    public static ExportHandler getExportHandler(String type) {
        switch (ExportTypeAndFileNameEnum.getExportTypeAndFileNameEnumByType(type)) {
            case BATCH_JOB:
                return new ExportBatchJobHandler();
            case ENV:
                return new ExportEnvHandler();
            case CONN:
                return new ExportConnHandler();
            case CIPHER:
                return new ExportCipherHandler();
            case SCRIPT:
                return new ExportScriptHandler();
            case STREAM_JOB:
                return new ExportStreamJobHandler();
            default:
                LOGGER.error("The type {} not support!", type);
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_EXPORT_RESOURCE_TYPE_NOT_SUPPORT);
        }
    }
}