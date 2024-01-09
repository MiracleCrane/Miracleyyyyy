/*
 * 文 件 名:  TaskModel.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.model;

import java.util.Locale;

/**
 * 单个数据导入任务模型
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/10]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class TaskModel {
    private String taskId;
    private String resourceOrigin;
    private String resourceType;
    private String importMode;
    private boolean isSkip;
    private boolean isSingleFile;
    private int total;
    private String fileName;
    private String detailName;
    private String fileSuffix;
    private String fileContent;
    private String errorCode;
    private int number;
    private String params;
    private String creator;
    private String creatorType;
    private Locale locale;

    public TaskModel(String taskId, String resourceOrigin, String resourceType, String importMode, boolean isSkip,
            int total, String creator, String creatorType, Locale locale) {
        this.taskId = taskId;
        this.resourceOrigin = resourceOrigin;
        this.resourceType = resourceType;
        this.importMode = importMode;
        this.isSkip = isSkip;
        this.total = total;
        this.creator = creator;
        this.creatorType = creatorType;
        this.locale = locale;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(String creatorType) {
        this.creatorType = creatorType;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getImportMode() {
        return importMode;
    }

    public void setImportMode(String importMode) {
        this.importMode = importMode;
    }

    public String getResourceOrigin() {
        return resourceOrigin;
    }

    public void setResourceOrigin(String resourceOrigin) {
        this.resourceOrigin = resourceOrigin;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public boolean isSkip() {
        return isSkip;
    }

    public void setSkip(boolean skip) {
        isSkip = skip;
    }

    public boolean isSingleFile() {
        return isSingleFile;
    }

    public void setSingleFile(boolean singleFile) {
        isSingleFile = singleFile;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}