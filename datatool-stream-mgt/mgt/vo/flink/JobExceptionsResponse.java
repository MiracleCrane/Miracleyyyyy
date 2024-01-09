/*
 * 文 件 名:  JobExceptions.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/14
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo.flink;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 获取作业异常信息响应，集合
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/14]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class JobExceptionsResponse {
    @JsonProperty(value = "root-exception")
    private String rootException;

    private boolean truncated;
    private long timestamp;

    @JsonProperty(value = "all-exceptions")
    private List<JobException> allExceptions;

    public String getRootException() {
        return rootException;
    }

    public void setRootException(String rootException) {
        this.rootException = rootException;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<JobException> getAllExceptions() {
        return allExceptions;
    }

    public void setAllExceptions(List<JobException> allExceptions) {
        this.allExceptions = allExceptions;
    }
}
