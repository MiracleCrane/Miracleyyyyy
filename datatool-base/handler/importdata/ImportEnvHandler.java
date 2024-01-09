/*
 * 文 件 名:  ImportEnvHandler.java
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
import com.huawei.smartcampus.datatool.base.enumeration.DgcResourceTypeEnum;
import com.huawei.smartcampus.datatool.base.enumeration.FileSuffixEnum;
import com.huawei.smartcampus.datatool.base.handler.importdata.base.AbstractImportHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.helper.ImportHelper;
import com.huawei.smartcampus.datatool.base.model.TaskModel;
import com.huawei.smartcampus.datatool.base.utils.CheckUtils;
import com.huawei.smartcampus.datatool.base.utils.JSONUtils;
import com.huawei.smartcampus.datatool.entity.DtEnvironmentVariableEntity;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 该处理器负责导入来自环境变量资产
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/10]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ImportEnvHandler extends AbstractImportHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportEnvHandler.class);

    @Autowired
    private ImportHelper importHelper;

    @Override
    public void handleImportTask(TaskModel taskModel) {
        if (!(FileSuffixEnum.ENV.suffix().equals(taskModel.getFileSuffix())
                && (DgcResourceTypeEnum.ENV.type().equals(taskModel.getResourceType())
                        || DataToolResourceTypeEnum.ENV.type().equals(taskModel.getResourceType())))) {
            return;
        }
        List<DtEnvironmentVariableEntity> createEnvReqs = convertEnvFormat(taskModel);
        for (DtEnvironmentVariableEntity environmentVariableEntity : createEnvReqs) {
            try {
                if (environmentVariableEntity == null) {
                    continue;
                }
                DtEnvironmentVariableEntity entity = importHelper
                        .findEnvironmentVariableEntityByKey(environmentVariableEntity.getKey());
                taskModel.setDetailName(environmentVariableEntity.getKey());
                CheckUtils.checkEnvParams(environmentVariableEntity);
                // 如果原来就不存在，就新增
                if (entity == null) {
                    importHelper.saveDtEnvironmentVariableEntity(environmentVariableEntity);
                }
                // 如果存在，覆盖更新env
                importHelper.updateEnvWithOverWrite(entity, taskModel.isSkip(), environmentVariableEntity);
                // 跳过，就写条数据到detail表中
                if (entity != null && taskModel.isSkip()) {
                    handle(taskModel, DetailStatusEnum.SKIPPED.status());
                    continue;
                }
                handle(taskModel, DetailStatusEnum.SUCCESS.status());
            } catch (DataToolRuntimeException e) {
                LOGGER.error("Import env fail.", e);
                errorHandle(taskModel, e);
            } catch (Exception e) {
                LOGGER.error("Import env fail.", e);
                errorHandle(taskModel, ExceptionCode.DATATOOL_SYSTEM_ERROR);
            }
        }
    }

    private List<DtEnvironmentVariableEntity> convertEnvFormat(TaskModel taskModel) {
        List<DtEnvironmentVariableEntity> environmentVariableEntities = new ArrayList<>();
        try {
            JSONArray jsonArray = JSONUtils
                    .getJSONArrayOrErrorHandler(JSONObject.parseObject(taskModel.getFileContent()), "params");
            for (int i = 0; i < jsonArray.size(); i++) {
                DtEnvironmentVariableEntity dtEnvironmentVariableEntity = new DtEnvironmentVariableEntity();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                dtEnvironmentVariableEntity.setKey(jsonObject.getString("name"));
                dtEnvironmentVariableEntity.setValue(jsonObject.getString("value"));
                environmentVariableEntities.add(dtEnvironmentVariableEntity);
            }
            taskModel.setTotal(environmentVariableEntities.size());
            taskModel.setSingleFile(false);
            // 更新history表total字段
            importHelper.updateImportHistoryEntity(taskModel.getTaskId(), environmentVariableEntities.size());
        } catch (DataToolRuntimeException e) {
            LOGGER.error("Convert env to object format fail.", e);
            errorHandle(taskModel, e);
        } catch (Exception e) {
            LOGGER.error("Convert env to object format fail.", e);
            errorHandle(taskModel, ExceptionCode.DATATOOL_FILE_CONTENT_CONVERT_FAIL);
        }
        return environmentVariableEntities;
    }
}