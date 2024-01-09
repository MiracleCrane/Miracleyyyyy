/*
 * 文 件 名:  ImportDliVarDgcHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.handler.importdata;

import com.huawei.smartcampus.datatool.base.enumeration.DetailStatusEnum;
import com.huawei.smartcampus.datatool.base.enumeration.DgcResourceTypeEnum;
import com.huawei.smartcampus.datatool.base.enumeration.FileSuffixEnum;
import com.huawei.smartcampus.datatool.base.enumeration.ResourceOriginEnum;
import com.huawei.smartcampus.datatool.base.handler.importdata.base.AbstractImportHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.helper.ImportHelper;
import com.huawei.smartcampus.datatool.base.model.TaskModel;
import com.huawei.smartcampus.datatool.base.utils.CheckUtils;
import com.huawei.smartcampus.datatool.base.utils.JSONUtils;
import com.huawei.smartcampus.datatool.base.vo.dlivar.DliVar;
import com.huawei.smartcampus.datatool.entity.DtCipherVariableEntity;
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
 * 该处理器负责导入来自dgc的Dli_Var资产
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/10]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ImportDliVarDgcHandler extends AbstractImportHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportDliVarDgcHandler.class);

    @Autowired
    private ImportHelper importHelper;

    @Override
    public void handleImportTask(TaskModel taskModel) {
        if (!(ResourceOriginEnum.DGC.origin().equals(taskModel.getResourceOrigin())
                && DgcResourceTypeEnum.DLI_VAR.type().equals(taskModel.getResourceType())
                && FileSuffixEnum.JSON.suffix().equals(taskModel.getFileSuffix()))) {
            return;
        }
        List<DliVar> dliVars = convertDliVarFormat(taskModel);
        for (DliVar dliVar : dliVars) {
            try {
                taskModel.setDetailName(dliVar.getName());
                // 如果是敏感信息，存到密码箱
                if (dliVar.isSensitive()) {
                    DtCipherVariableEntity entity = importHelper.findCipherVariableEntityByKey(dliVar.getName());
                    CheckUtils.checkCipherParams(dliVar);
                    // 如果原来就不存在，就新增；否则覆盖更新
                    importHelper.updateOrSaveCipherVariableEntity(dliVar, entity, taskModel.isSkip());
                    // 跳过，就写条数据到detail表中
                    if (entity != null && taskModel.isSkip()) {
                        handle(taskModel, DetailStatusEnum.SKIPPED.status());
                        continue;
                    }
                } else {
                    DtEnvironmentVariableEntity entity = importHelper
                            .findEnvironmentVariableEntityByKey(dliVar.getName());
                    CheckUtils.checkEnvParams(dliVar);
                    // 如果原来就不存在，就新增
                    if (entity == null) {
                        DtEnvironmentVariableEntity dtEnvironmentVariableEntity = new DtEnvironmentVariableEntity();
                        dtEnvironmentVariableEntity.setKey(dliVar.getName());
                        dtEnvironmentVariableEntity.setValue(dliVar.getValue());
                        importHelper.saveDtEnvironmentVariableEntity(dtEnvironmentVariableEntity);
                    }
                    // 覆盖更新
                    importHelper.updateEnvWithOverWrite(entity, taskModel.isSkip(), dliVar);
                    // 跳过，就写条数据到detail表中
                    if (entity != null && taskModel.isSkip()) {
                        handle(taskModel, DetailStatusEnum.SKIPPED.status());
                        continue;
                    }
                }
                handle(taskModel, DetailStatusEnum.SUCCESS.status());
            } catch (DataToolRuntimeException e) {
                LOGGER.error("Import dli_var fail.", e);
                errorHandle(taskModel, e);
            } catch (Exception e) {
                LOGGER.error("Import dli_var fail.", e);
                errorHandle(taskModel, ExceptionCode.DATATOOL_SYSTEM_ERROR);
            }
        }
    }

    private List<DliVar> convertDliVarFormat(TaskModel taskModel) {
        List<DliVar> dliVars = new ArrayList<>();
        try {
            JSONArray jsonArray = JSONObject.parseObject(taskModel.getFileContent()).getJSONArray("global_vars");
            for (int i = 0; i < jsonArray.size(); i++) {
                DliVar dliVar = new DliVar();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                dliVar.setName(JSONUtils.getStringOrErrorHandler(jsonObject, "var_name"));
                dliVar.setValue(JSONUtils.getStringOrErrorHandler(jsonObject, "var_value"));
                dliVar.setSensitive(JSONUtils.getBooleanOrErrorHandler(jsonObject, "is_sensitive"));
                dliVars.add(dliVar);
            }
            taskModel.setTotal(dliVars.size());
            taskModel.setSingleFile(false);
            // 更新history表total字段
            importHelper.updateImportHistoryEntity(taskModel.getTaskId(), dliVars.size());
        } catch (DataToolRuntimeException e) {
            LOGGER.error("Convert dli_var to object format fail.", e);
            errorHandle(taskModel, e);
        } catch (Exception e) {
            LOGGER.error("Convert dli_var to object format fail.", e);
            errorHandle(taskModel, ExceptionCode.DATATOOL_FILE_CONTENT_CONVERT_FAIL);
        }
        return dliVars;
    }
}