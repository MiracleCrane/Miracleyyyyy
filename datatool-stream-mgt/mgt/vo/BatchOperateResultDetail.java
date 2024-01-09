/*
 * 文 件 名:  BatchOperateResultDetail.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  z00850154
 * 修改时间： 2023/8/25
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo;

/**
 * <批量查询结果明细>
 * <实时流批量查询结果明细>
 *
 * @author z00850154
 * @version [Core&Link 23.1, 2023/8/25]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */

public class BatchOperateResultDetail {
    // 作业id
    private String id;

    // 作业名称或者key
    private String name;

    // 结果 [ success, failure ]
    private String status;

    // 失败详细原因
    private String message;

    public BatchOperateResultDetail() {
    }

    public BatchOperateResultDetail(String id, String name, String status, String message) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.message = message;
    }

    public BatchOperateResultDetail(String id, String name, String status) {
        this(id, name, status, null);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
