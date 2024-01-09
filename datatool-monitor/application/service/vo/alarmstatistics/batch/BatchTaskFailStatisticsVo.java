/*
 * 文 件 名:  BatchTaskFailStatisticsVo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/26
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmstatistics.batch;

/**
 * 作业实例失败统计数据对象
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/26]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class BatchTaskFailStatisticsVo {
    private String jobId;
    private String jobName;
    private String recentFailDate;
    private long failCount;

    /**
     * 无参构造
     */
    public BatchTaskFailStatisticsVo() {
    }

    public BatchTaskFailStatisticsVo(String jobId, String jobName, String recentFailDate, long failCount) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.recentFailDate = recentFailDate;
        this.failCount = failCount;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getRecentFailDate() {
        return recentFailDate;
    }

    public void setRecentFailDate(String recentFailDate) {
        this.recentFailDate = recentFailDate;
    }

    public long getFailCount() {
        return failCount;
    }

    public void setFailCount(long failCount) {
        this.failCount = failCount;
    }
}