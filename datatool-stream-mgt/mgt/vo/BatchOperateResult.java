/*
 * 文 件 名:  BatchOperateResult.java
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
 * <批量操作结果>
 * <返回实时流任务批量操作结果>
 *
 * @author z00850154
 * @version [Core&Link 23.1, 2023/8/25]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */

public class BatchOperateResult {
    private List<BatchOperateResultDetail> details;
    private int failure;
    private int success;
    private int total;

    public BatchOperateResult() {
    }

    public BatchOperateResult(List<BatchOperateResultDetail> details, int failure, int success, int total) {
        this.details = details;
        this.failure = failure;
        this.success = success;
        this.total = total;
    }

    public List<BatchOperateResultDetail> getDetails() {
        return details;
    }

    public void setDetails(List<BatchOperateResultDetail> details) {
        this.details = details;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
