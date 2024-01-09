/*
 * 文 件 名:  ImportHistoryReq.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/9
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.vo.req;

import org.hibernate.validator.constraints.Range;

import java.util.List;

import javax.validation.constraints.Min;

/**
 * 导入历史接口入参
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/9]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ImportHistoryReq {
    @Min(value = 1, message = "{DATATOOL_PAGEINDEX_INVALID}")
    private int pageIndex = 1;

    @Range(min = 1, max = 1000, message = "{DATATOOL_PAGESIZE_INVALID}")
    private int pageSize = 10;
    private String resourceName;
    private String resourceImportStatus;
    private String resourceImportMode;
    private String resourceType;
    private String resourceOrigin;
    private List<String> resourceImportPeriod;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceImportStatus() {
        return resourceImportStatus;
    }

    public void setResourceImportStatus(String resourceImportStatus) {
        this.resourceImportStatus = resourceImportStatus;
    }

    public String getResourceImportMode() {
        return resourceImportMode;
    }

    public void setResourceImportMode(String resourceImportMode) {
        this.resourceImportMode = resourceImportMode;
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

    public List<String> getResourceImportPeriod() {
        return resourceImportPeriod;
    }

    public void setResourceImportPeriod(List<String> resourceImportPeriod) {
        this.resourceImportPeriod = resourceImportPeriod;
    }
}