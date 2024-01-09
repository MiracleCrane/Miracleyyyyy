/*
 * 文 件 名:  ImportConnDgcHandler.java
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
import com.huawei.smartcampus.datatool.base.utils.CommonUtils;
import com.huawei.smartcampus.datatool.base.utils.JSONUtils;
import com.huawei.smartcampus.datatool.entity.DtConnectionEntity;
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
 * 该处理器负责导入来自dgc的数据连接资产
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/10]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportConnDgcHandler extends AbstractImportHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportConnDgcHandler.class);

    @Autowired
    private ImportHelper importHelper;

    @Override
    public void handleImportTask(TaskModel taskModel) {
        if (!(DgcResourceTypeEnum.CONN.type().equals(taskModel.getResourceType())
                && ResourceOriginEnum.DGC.origin().equals(taskModel.getResourceOrigin())
                && FileSuffixEnum.CONN.suffix().equals(taskModel.getFileSuffix()))) {
            return;
        }
        try {
            DtConnectionEntity dtConnectionEntity = convertConnFormat(taskModel);
            DtConnectionEntity entity = importHelper.findDtConnectionEntityByName(dtConnectionEntity.getName());
            taskModel.setDetailName(dtConnectionEntity.getName());
            CheckUtils.checkConnParams(dtConnectionEntity, taskModel);
            // 如果原来就不存在，就新增
            if (entity == null) {
                importHelper.saveConnectionEntity(dtConnectionEntity);
                handle(taskModel, DetailStatusEnum.SUCCESS.status());
            } else {
                handle(taskModel, DetailStatusEnum.SKIPPED.status());
            }
        } catch (DataToolRuntimeException e) {
            LOGGER.error("Import conn fail.", e);
            errorHandle(taskModel, e);
        } catch (Exception e) {
            LOGGER.error("Import conn fail.", e);
            errorHandle(taskModel, ExceptionCode.DATATOOL_SYSTEM_ERROR);
        }
    }

    private DtConnectionEntity convertConnFormat(TaskModel taskModel) {
        DtConnectionEntity dtConnectionEntity = new DtConnectionEntity();
        try {
            JSONObject jsonObject = JSONObject.parseObject(taskModel.getFileContent());
            JSONObject config = jsonObject.getJSONObject("config");
            dtConnectionEntity.setName(JSONUtils.getStringOrErrorHandler(jsonObject, "name"));
            dtConnectionEntity.setType(CommonUtils.getDgcDBType(JSONUtils.getStringOrErrorHandler(jsonObject, "type")));
            dtConnectionEntity.setUser(JSONUtils.getStringOrErrorHandler(config, "userName"));
            dtConnectionEntity.setHost(JSONUtils.getStringOrErrorHandler(config, "ip"));
            dtConnectionEntity.setPort(JSONUtils.getIntegerOrErrorHandler(config, "port"));
        } catch (DataToolRuntimeException e) {
            LOGGER.error("Convert dgc conn to object format fail.", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Convert dgc conn to object format fail.", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_FILE_CONTENT_CONVERT_FAIL);
        }
        return dtConnectionEntity;
    }
}