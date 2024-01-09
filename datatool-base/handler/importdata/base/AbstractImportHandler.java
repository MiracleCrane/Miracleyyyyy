/*
 * 文 件 名:  AbstractImportHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.handler.importdata.base;

import com.huawei.smartcampus.datatool.base.enumeration.DetailStatusEnum;
import com.huawei.smartcampus.datatool.base.enumeration.DgcResourceTypeEnum;
import com.huawei.smartcampus.datatool.base.enumeration.FileSuffixEnum;
import com.huawei.smartcampus.datatool.base.enumeration.ResourceOriginEnum;
import com.huawei.smartcampus.datatool.base.handler.importdata.helper.ImportHelper;
import com.huawei.smartcampus.datatool.base.model.TaskModel;
import com.huawei.smartcampus.datatool.base.pattern.ImportExportPattern;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 导入任务处理的基类，所有子类型导入任务都要继承此类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/10]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public abstract class AbstractImportHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImportHandler.class);
    private ImportHelper helper = SpringContextHelper.getBean(ImportHelper.class);
    private AbstractImportHandler nextHandler;

    /**
     * 模板模式入口方法
     *
     * @param taskModel 任务模型
     */
    public void importHandle(TaskModel taskModel) {
        try {
            checkFileName(taskModel);
            checkFileType(taskModel);
            checkIsJsonContent(taskModel);
            doNext(taskModel);
        } catch (DataToolRuntimeException e) {
            errorHandle(taskModel, e);
        }
    }

    /**
     * 校验文件名称
     *
     * @param taskModel 任务模型
     */
    protected final void checkFileName(TaskModel taskModel) {
        if (!ImportExportPattern.SINGLE_FILE_NAME.matcher(taskModel.getFileName()).find()) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_IMPORT_FILE_NAME,
                    taskModel.getFileName());
        }
    }

    /**
     * 校验文件类型
     *
     * @param taskModel 任务模型
     */
    protected final void checkFileType(TaskModel taskModel) {
        String expectedFileSuffix = "." + taskModel.getResourceType();
        if (!(taskModel.getFileSuffix().equals(expectedFileSuffix)
                || isJsonSuffixResourceType(taskModel.getFileSuffix(), taskModel.getResourceType(),
                        taskModel.getResourceOrigin())
                || isJobScriptSuffix(taskModel.getFileSuffix(), taskModel.getResourceType(),
                        taskModel.getResourceOrigin()))) {
            if (DgcResourceTypeEnum.JOB_SCRIPT.type().equals(taskModel.getResourceType())) {
                expectedFileSuffix = FileSuffixEnum.JOB.suffix() + "/" + FileSuffixEnum.SCRIPT.suffix();
            }
            if (isJsonSuffixResourceType(taskModel.getFileSuffix(), taskModel.getResourceType(),
                    taskModel.getResourceOrigin())) {
                expectedFileSuffix = FileSuffixEnum.JSON.suffix();
            }
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_FILE_TYPE, expectedFileSuffix,
                    taskModel.getFileSuffix());
        }
    }

    /**
     * 判断资源类型和文件后缀是否是json类型的文件
     *
     * @param fileSuffix 文件后缀
     * @param resourceType 资源类型
     * @param resourceOrigin 资产来源
     * @return 布尔
     */
    private boolean isJsonSuffixResourceType(String fileSuffix, String resourceType, String resourceOrigin) {
        return FileSuffixEnum.JSON.suffix().equals(fileSuffix) && ResourceOriginEnum.DGC.origin().equals(resourceOrigin)
                && DgcResourceTypeEnum.getJsonSuffixResourceTypeList().contains(resourceType);
    }

    /**
     * 判断资源类型和文件后缀是否是job_script类型的文件
     *
     * @param fileSuffix 文件后缀
     * @param resourceType 资源类型
     * @param resourceOrigin 资产来源
     * @return 布尔
     */
    private boolean isJobScriptSuffix(String fileSuffix, String resourceType, String resourceOrigin) {
        return DgcResourceTypeEnum.JOB_SCRIPT.type().equals(resourceType)
                && ResourceOriginEnum.DGC.origin().equals(resourceOrigin)
                && FileSuffixEnum.getJobScriptSuffixList().contains(fileSuffix);
    }

    /**
     * 校验文件内容是否是json类型
     *
     * @param taskModel 任务模型
     */
    protected final void checkIsJsonContent(TaskModel taskModel) {
        try {
            // 当类型是DLI时，是一个json数组
            if (DgcResourceTypeEnum.DLI.type().equals(taskModel.getResourceType())) {
                JSONObject.parseObject(taskModel.getFileContent(), JSONArray.class);
            } else {
                JSONObject.parseObject(taskModel.getFileContent());
            }
        } catch (Exception e) {
            LOGGER.error("The file content is not of json type.", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_FILE_CONTENT_NOT_JSON_TYPE);
        }
    }

    public void nextHandler(AbstractImportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * 处理导入任务
     *
     * @param taskModel 导入任务模型
     */
    public abstract void handleImportTask(TaskModel taskModel);

    protected void doNext(TaskModel taskModel) {
        this.handleImportTask(taskModel);
        if (this.nextHandler != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("this class: {}, this.nextHandler: {}", this.getClass().getSimpleName(),
                        this.nextHandler.getClass().getSimpleName());
            }
            this.nextHandler.doNext(taskModel);
        }
    }

    /**
     * 导入失败，错误处理，自己传入参数
     *
     * @param taskModel 单个任务模型
     * @param errorCode 错误码
     * @param params 参数
     */
    public void errorHandle(TaskModel taskModel, String errorCode, String... params) {
        taskModel.setErrorCode(errorCode);
        StringBuilder stringBuilder = new StringBuilder();
        for (String param : params) {
            stringBuilder.append(param).append(";");
        }
        taskModel.setParams(stringBuilder.toString());
        // 如果进入了失败处理逻辑，则存失败信息到detail表
        handle(taskModel, DetailStatusEnum.FAILURE.status());
    }

    /**
     * 导入失败，错误处理，抛出的异常获取参数
     *
     * @param taskModel 单个任务模型
     * @param e 异常
     */
    public void errorHandle(TaskModel taskModel, DataToolRuntimeException e) {
        taskModel.setErrorCode(e.getExceptionCode());
        if (e.getExceptionBean().getParameters() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object param : e.getExceptionBean().getParameters()) {
                stringBuilder.append(param != null ? String.valueOf(param) : "").append(";");
            }
            taskModel.setParams(stringBuilder.toString());
        }
        // 如果进入了失败处理逻辑，则存失败信息到detail表
        handle(taskModel, DetailStatusEnum.FAILURE.status());
    }

    /**
     * 导入结束处理逻辑
     *
     * @param taskModel 单个任务模型
     * @param status 状态
     */
    public void handle(TaskModel taskModel, String status) {
        helper.saveImportDetailEntity(taskModel, status);
        helper.confirm(taskModel);
        if (!taskModel.isSingleFile()) {
            taskModel.setNumber(taskModel.getNumber() + 1);
        }
    }
}