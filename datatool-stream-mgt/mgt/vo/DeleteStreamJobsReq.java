/*
 * 文 件 名:  DeleteStreamJobsReq.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  z00850154
 * 修改时间： 2023/8/25
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo;

import java.util.List;

import javax.validation.constraints.Size;

/**
 * 批量删除流处理作业请求
 *
 * @author z00850154
 * @version [Core&Link 23.1, 2023/8/25]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */

public class DeleteStreamJobsReq {
    // 待删除作业id，最多同时删除100个
    @Size(max = 100, message = "{DATATOOL_STREAM_JOB_BATCH_DELETE_SIZE_EXCEED_LIMIT}")
    private List<String> ids;

    // 是否强制删除
    private boolean isForce;

    /**
     * 无参构造
     */
    public DeleteStreamJobsReq() {
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public boolean getIsForce() {
        return isForce;
    }

    public void setIsForce(boolean isForce) {
        this.isForce = isForce;
    }

    @Override
    public String toString() {
        return "DeleteStreamJobsReq{" + "ids=" + ids + '}';
    }
}
