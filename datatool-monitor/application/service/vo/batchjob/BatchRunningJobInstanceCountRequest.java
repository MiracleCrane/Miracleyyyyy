/*
 * 文 件 名:  BatchRunningJobInstanceCountRequest.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/22
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.batchjob;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 近24小时作业实例运行数量请求
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/22]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class BatchRunningJobInstanceCountRequest {
    @NotBlank
    private String currentDate;
    @NotNull
    private Integer interval;

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }
}