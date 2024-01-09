/*
 * 文 件 名:  StreamJobQueryCondi.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  z00850154
 * 修改时间： 2023/8/25
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo;

import com.huawei.smartcampus.datatool.enums.JobStatus;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

/**
 * <实时流任务查询条件>
 * <实时流任务查询条件>
 *
 * @author z00850154
 * @version [Core&Link 23.1, 2023/8/25]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */

public class StreamJobQueryCondi {
    private String id;
    private String name;
    private JobStatus status;

    @Min(value = 1, message = "{DATATOOL_PAGEINDEX_INVALID}")
    private int pageIndex = 1;

    @Range(min = 1, max = 1000, message = "{DATATOOL_PAGESIZE_INVALID}")
    private int pageSize = 10;

    public StreamJobQueryCondi() {
    }

    public StreamJobQueryCondi(String name, JobStatus status, int pageIndex, int pageSize) {
        this.name = name;
        this.status = status;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

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
}
