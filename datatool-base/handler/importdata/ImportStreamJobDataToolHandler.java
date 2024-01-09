/*
 * 文 件 名:  ImportStreamJobDataToolHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.handler.importdata;

import com.huawei.smartcampus.datatool.base.enumeration.DataToolResourceTypeEnum;
import com.huawei.smartcampus.datatool.base.enumeration.DetailStatusEnum;
import com.huawei.smartcampus.datatool.base.enumeration.FileSuffixEnum;
import com.huawei.smartcampus.datatool.base.enumeration.ResourceOriginEnum;
import com.huawei.smartcampus.datatool.base.handler.importdata.base.AbstractImportHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.helper.ImportHelper;
import com.huawei.smartcampus.datatool.base.model.TaskModel;
import com.huawei.smartcampus.datatool.base.utils.CheckUtils;
import com.huawei.smartcampus.datatool.entity.StreamJobEntity;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;

import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 该处理器负责导入来自datatool的流处理作业资产
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/10]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ImportStreamJobDataToolHandler extends AbstractImportHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportStreamJobDataToolHandler.class);

    @Autowired
    private ImportHelper importHelper;

    @Override
    public void handleImportTask(TaskModel taskModel) {
        if (!(DataToolResourceTypeEnum.STREAM.type().equals(taskModel.getResourceType())
                && ResourceOriginEnum.DATATOOL.origin().equals(taskModel.getResourceOrigin())
                && FileSuffixEnum.STREAM.suffix().equals(taskModel.getFileSuffix()))) {
            return;
        }
        try {
            StreamJobEntity streamJobEntity = convertStreamJobFormat(taskModel);
            if (streamJobEntity == null) {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_FILE_CONTENT_CONVERT_FAIL);
            }
            StreamJobEntity entity = importHelper.findStreamJobEntityByName(streamJobEntity.getName());
            // 如果转换后有内容，非跳过情况下，保存或更新到stream_job
            taskModel.setDetailName(streamJobEntity.getName());
            CheckUtils.checkStreamParams(streamJobEntity, taskModel.getResourceType());
            // 如果原来就不存在，就新增，新增时state设为UNSUBMITTED，flinkSql经base64加密保存
            if (entity == null) {
                importHelper.saveStreamJobEntity(streamJobEntity);
            } else if (!taskModel.isSkip()) {
                importHelper.updateStreamJobEntity(entity, streamJobEntity);
            } else {
                // 成功后，插入数据到detail表中
                handle(taskModel, DetailStatusEnum.SKIPPED.status());
                return;
            }
            handle(taskModel, DetailStatusEnum.SUCCESS.status());
        } catch (DataToolRuntimeException e) {
            LOGGER.error("Import datatool stream job fail.", e);
            errorHandle(taskModel, e);
        } catch (Exception e) {
            LOGGER.error("Import datatool stream job fail.", e);
            errorHandle(taskModel, ExceptionCode.DATATOOL_SYSTEM_ERROR);
        }
    }

    private StreamJobEntity convertStreamJobFormat(TaskModel taskModel) {
        StreamJobEntity streamJobEntity;
        try {
            streamJobEntity = JSONObject.parseObject(taskModel.getFileContent(), StreamJobEntity.class);
            streamJobEntity.setId("");
        } catch (Exception e) {
            LOGGER.error("Convert datatool stream job to object format fail.", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_FILE_CONTENT_CONVERT_FAIL);
        }
        return streamJobEntity;
    }
}