/*
 * 文 件 名:  ImportCipherFromDataTool.java
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
import com.huawei.smartcampus.datatool.base.utils.JSONUtils;
import com.huawei.smartcampus.datatool.entity.DtCipherVariableEntity;
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
 * 该处理器负责导入来自datatool的密码箱资产
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/10]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ImportCipherDataToolHandler extends AbstractImportHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportCipherDataToolHandler.class);

    @Autowired
    private ImportHelper importHelper;

    @Override
    public void handleImportTask(TaskModel taskModel) {
        if (!(ResourceOriginEnum.DATATOOL.origin().equals(taskModel.getResourceOrigin())
                && FileSuffixEnum.CIPHER.suffix().equals(taskModel.getFileSuffix())
                && DataToolResourceTypeEnum.CIPHER.type().equals(taskModel.getResourceType()))) {
            return;
        }
        List<DtCipherVariableEntity> cipherVariableEntities = convertEnvFormat(taskModel);
        for (DtCipherVariableEntity dtCipherVariableEntity : cipherVariableEntities) {
            try {
                taskModel.setDetailName(dtCipherVariableEntity.getKey());
                DtCipherVariableEntity entity = importHelper
                        .findCipherVariableEntityByKey(dtCipherVariableEntity.getKey());
                CheckUtils.checkCipherParams(dtCipherVariableEntity);
                importHelper.updateOrSaveCipherVariableEntity(dtCipherVariableEntity, entity, taskModel.isSkip());
                // 跳过，就写条数据到detail表中
                if (entity != null && taskModel.isSkip()) {
                    handle(taskModel, DetailStatusEnum.SKIPPED.status());
                    continue;
                }
                handle(taskModel, DetailStatusEnum.SUCCESS.status());
            } catch (DataToolRuntimeException e) {
                LOGGER.error("Import cipher fail.", e);
                errorHandle(taskModel, e);
            } catch (Exception e) {
                LOGGER.error("Import cipher fail.", e);
                errorHandle(taskModel, ExceptionCode.DATATOOL_SYSTEM_ERROR);
            }
        }
    }

    private List<DtCipherVariableEntity> convertEnvFormat(TaskModel taskModel) {
        List<DtCipherVariableEntity> cipherVariableEntities = new ArrayList<>();
        try {
            JSONArray jsonArray = JSONUtils
                    .getJSONArrayOrErrorHandler(JSONObject.parseObject(taskModel.getFileContent()), "params");
            for (int i = 0; i < jsonArray.size(); i++) {
                DtCipherVariableEntity dtCipherVariableEntity = new DtCipherVariableEntity();
                dtCipherVariableEntity.setKey(jsonArray.getJSONObject(i).getString("cipher_key"));
                dtCipherVariableEntity.setValue("");
                cipherVariableEntities.add(dtCipherVariableEntity);
            }
            taskModel.setTotal(cipherVariableEntities.size());
            taskModel.setSingleFile(false);
            // 更新history表total字段
            importHelper.updateImportHistoryEntity(taskModel.getTaskId(), cipherVariableEntities.size());
        } catch (DataToolRuntimeException e) {
            LOGGER.error("Convert datatool cipher to object format fail.", e);
            errorHandle(taskModel, e);
        } catch (Exception e) {
            LOGGER.error("Convert datatool cipher to object format fail.", e);
            errorHandle(taskModel, ExceptionCode.DATATOOL_FILE_CONTENT_CONVERT_FAIL);
        }
        return cipherVariableEntities;
    }
}