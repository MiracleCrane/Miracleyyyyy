/*
 * 文 件 名:  StartStreamJobsReq.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  z00850154
 * 修改时间： 2023/8/25
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.vo.flink;

import java.util.List;

import javax.validation.constraints.Size;

/**
 * <启动实时任务请求>
 * <启动实时任务请求>
 *
 * @author z00850154
 * @version [Core&Link 23.1, 2023/8/25]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class StartStreamJobsReq {
    @Size(max = 100, message = "{DATATOOL_STREAM_JOB_BATCH_START_SIZE_EXCEED_LIMIT}")
    private List<String> ids;

    private boolean fromSavepoint;

    /**
     * 无参构造
     */
    public StartStreamJobsReq() {
    }

    public StartStreamJobsReq(List<String> ids, boolean fromSavepoint) {
        this.ids = ids;
        this.fromSavepoint = fromSavepoint;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public boolean getFromSavepoint() {
        return fromSavepoint;
    }

    public void setFromSavepoint(boolean fromSavepoint) {
        this.fromSavepoint = fromSavepoint;
    }

    @Override
    public String toString() {
        return "StartStreamJobsReq{" + "ids=" + ids + '}';
    }
}
