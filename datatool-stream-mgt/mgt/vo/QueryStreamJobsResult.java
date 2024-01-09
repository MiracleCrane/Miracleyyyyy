/*
 * 文 件 名:  QueryStreamJobsResult.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  z00850154
 * 修改时间： 2023/8/25
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo;

import java.util.List;

/**
 * <实时流任务查询结果>
 * <返回实时流查询结果>
 *
 * @author z00850154
 * @version [Core&Link 23.1, 2023/8/25]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class QueryStreamJobsResult {
    private int total;
    private int pageIndex;
    private List<StreamJobRuntimeInfo> jobs;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<StreamJobRuntimeInfo> getJobs() {
        return jobs;
    }

    public void setJobs(List<StreamJobRuntimeInfo> jobs) {
        this.jobs = jobs;
    }
}
