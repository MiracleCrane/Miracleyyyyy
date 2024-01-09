/*
 * 文 件 名:  ImportScriptHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.handler.importdata;

import com.huawei.smartcampus.datatool.base.enumeration.DetailStatusEnum;
import com.huawei.smartcampus.datatool.base.enumeration.FileSuffixEnum;
import com.huawei.smartcampus.datatool.base.enumeration.ResourceOriginEnum;
import com.huawei.smartcampus.datatool.base.handler.importdata.base.AbstractImportHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.helper.ImportHelper;
import com.huawei.smartcampus.datatool.base.model.TaskModel;
import com.huawei.smartcampus.datatool.base.utils.CheckUtils;
import com.huawei.smartcampus.datatool.base.utils.JSONUtils;
import com.huawei.smartcampus.datatool.base.vo.script.ImportScriptContent;
import com.huawei.smartcampus.datatool.entity.DtScriptEntity;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.repository.DtScriptRepository;

import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 该处理器负责导入脚本资产
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/10]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ImportScriptHandler extends AbstractImportHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportScriptHandler.class);

    @Autowired
    private ImportHelper importHelper;

    @Autowired
    private DtScriptRepository scriptRepository;

    @Override
    public void handleImportTask(TaskModel taskModel) {
        if (!(FileSuffixEnum.SCRIPT.suffix().equals(taskModel.getFileSuffix()))) {
            return;
        }
        try {
            ImportScriptContent scriptContent = convertScriptFormat(taskModel);
            taskModel.setDetailName(scriptContent.getDtScriptEntity().getName());
            CheckUtils.checkScriptParams(scriptContent);
            DtScriptEntity scriptEntity = importHelper
                    .findDtScriptEntityByName(scriptContent.getDtScriptEntity().getName());
            // 如果原来就不存在，就新增
            if (scriptEntity == null) {
                // 创建目录
                scriptContent.getDtScriptEntity().setDirId(importHelper.createScriptDir(scriptContent.getDir()));
                scriptRepository.save(scriptContent.getDtScriptEntity());
            }
            // 如果存在，覆盖更新
            if (scriptEntity != null && !taskModel.isSkip()) {
                scriptContent.getDtScriptEntity().setId(scriptEntity.getId());
                scriptContent.getDtScriptEntity().setCreatedBy(scriptEntity.getCreatedBy());
                // 更新目录
                scriptContent.getDtScriptEntity().setDirId(importHelper.createScriptDir(scriptContent.getDir()));
                scriptRepository.save(scriptContent.getDtScriptEntity());
            }
            // 跳过，就写条数据到detail表中
            if (scriptEntity != null && taskModel.isSkip()) {
                handle(taskModel, DetailStatusEnum.SKIPPED.status());
                return;
            }
            handle(taskModel, DetailStatusEnum.SUCCESS.status());
        } catch (DataToolRuntimeException e) {
            LOGGER.error("Import script fail.", e);
            errorHandle(taskModel, e);
        } catch (Exception e) {
            LOGGER.error("Import script fail.", e);
            errorHandle(taskModel, ExceptionCode.DATATOOL_SYSTEM_ERROR);
        }
    }

    private ImportScriptContent convertScriptFormat(TaskModel taskModel) {
        ImportScriptContent scriptContent = new ImportScriptContent();
        DtScriptEntity scriptEntity = new DtScriptEntity();
        try {
            JSONObject jsonObject = JSONObject.parseObject(taskModel.getFileContent());
            scriptEntity.setName(JSONUtils.getStringOrErrorHandler(jsonObject, "name"));
            scriptEntity.setConnName(JSONUtils.getStringOrDefaultEmpty(jsonObject, "connectionName"));
            if (ResourceOriginEnum.DGC.origin().equals(taskModel.getResourceOrigin())) {
                scriptEntity.setDatabase(JSONUtils.getStringOrDefaultEmpty(jsonObject, "currentDatabase"));
            }
            if (ResourceOriginEnum.DATATOOL.origin().equals(taskModel.getResourceOrigin())) {
                scriptEntity.setDatabase(JSONUtils.getStringOrDefaultEmpty(jsonObject, "database"));
            }
            scriptEntity.setContent(jsonObject.getString("content"));
            scriptEntity.setScriptArgs(
                    importHelper.parseScriptContent(JSONUtils.getStringOrDefaultEmpty(jsonObject, "content")));
            scriptContent.setDtScriptEntity(scriptEntity);
            scriptContent.setDir(JSONUtils.getStringOrDefaultEmpty(jsonObject, "directory"));
        } catch (DataToolRuntimeException e) {
            LOGGER.error("Convert script to object format fail.", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Convert script to object format fail.", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_FILE_CONTENT_CONVERT_FAIL);
        }
        return scriptContent;
    }
}