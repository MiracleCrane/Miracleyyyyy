/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.vo;

/**
 * 查询最大作业数量响应
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/19]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class QueryMaxJobNumResp {
    private int maxJobs;

    public int getMaxJobs() {
        return maxJobs;
    }

    public void setMaxJobs(int maxJobs) {
        this.maxJobs = maxJobs;
    }

    public QueryMaxJobNumResp(int maxJobs) {
        this.maxJobs = maxJobs;
    }

    /**
     * 无参构造函数
     */
    public QueryMaxJobNumResp() {
    }

    @Override
    public String toString() {
        return "QueryMaxJobNumResp{" + "maxJobs=" + maxJobs + '}';
    }
}