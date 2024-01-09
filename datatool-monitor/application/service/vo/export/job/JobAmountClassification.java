/*
 * 文 件 名:  JobAmountClassification.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job;

import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.GroupAmount;

/**
 * 作业数量分类汇总
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class JobAmountClassification {
    private GroupAmount batchJob;
    private GroupAmount batchScript;
    private GroupAmount streamJob;
    private GroupAmount total;

    public GroupAmount getBatchJob() {
        return batchJob;
    }

    public void setBatchJob(GroupAmount batchJob) {
        this.batchJob = batchJob;
    }

    public GroupAmount getBatchScript() {
        return batchScript;
    }

    public void setBatchScript(GroupAmount batchScript) {
        this.batchScript = batchScript;
    }

    public GroupAmount getStreamJob() {
        return streamJob;
    }

    public void setStreamJob(GroupAmount streamJob) {
        this.streamJob = streamJob;
    }

    public GroupAmount getTotal() {
        return total;
    }

    public void setTotal(GroupAmount total) {
        this.total = total;
    }
}