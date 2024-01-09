/*
 * 文 件 名:  ImportReq.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/12
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.vo.req;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 导入接口入参
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/12]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ImportReq {
    @NotNull(message = "{DATATOOL_MISSING_UPLOAD_FILE}")
    private MultipartFile resource;
    private String duplicatePolicy;
    private String resourceType;
    private String resourceOrigin;
    private String importMode;

    public ImportReq(MultipartFile resource, String resourceType, String resourceOrigin, String importMode) {
        this.resource = resource;
        this.resourceType = resourceType;
        this.resourceOrigin = resourceOrigin;
        this.importMode = importMode;
    }

    public ImportReq() {
    }

    public MultipartFile getResource() {
        return resource;
    }

    public void setResource(MultipartFile resource) {
        this.resource = resource;
    }

    public String getDuplicatePolicy() {
        return duplicatePolicy;
    }

    public void setDuplicatePolicy(String duplicatePolicy) {
        this.duplicatePolicy = duplicatePolicy;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceOrigin() {
        return resourceOrigin;
    }

    public void setResourceOrigin(String resourceOrigin) {
        this.resourceOrigin = resourceOrigin;
    }

    public String getImportMode() {
        return importMode;
    }

    public void setImportMode(String importMode) {
        this.importMode = importMode;
    }

    @Override
    public String toString() {
        return "ImportReq{" + "duplicatePolicy='" + duplicatePolicy + '\'' + ", resourceType='" + resourceType + '\''
                + ", resourceOrigin='" + resourceOrigin + '\'' + ", importMode='" + importMode + '\'' + '}';
    }
}