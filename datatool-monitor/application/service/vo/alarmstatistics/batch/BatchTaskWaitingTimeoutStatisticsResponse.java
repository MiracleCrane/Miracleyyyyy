/*
 * 文 件 名:  BatchTaskWaitingTimeoutStatisticsResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/26
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmstatistics.batch;

import java.util.List;

/**
 * 作业实例等待超时告警统计数据响应
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/26]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class BatchTaskWaitingTimeoutStatisticsResponse {
    private List<BatchTaskWaitingTimeoutStatisticsVo> jobList;
    private int total;

    public List<BatchTaskWaitingTimeoutStatisticsVo> getJobList() {
        return jobList;
    }

    public void setJobList(List<BatchTaskWaitingTimeoutStatisticsVo> jobList) {
        this.jobList = jobList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}