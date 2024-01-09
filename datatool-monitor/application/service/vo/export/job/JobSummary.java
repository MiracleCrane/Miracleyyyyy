/*
 * 文 件 名:  JobSummary.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job;

/**
 * 作业总览
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class JobSummary {
    private Integer totalJobNum;
    private Integer totalRunningJobNum;
    private Double utilizationRate;

    public Integer getTotalJobNum() {
        return totalJobNum;
    }

    public void setTotalJobNum(Integer totalJobNum) {
        this.totalJobNum = totalJobNum;
    }

    public Integer getTotalRunningJobNum() {
        return totalRunningJobNum;
    }

    public void setTotalRunningJobNum(Integer totalRunningJobNum) {
        this.totalRunningJobNum = totalRunningJobNum;
    }

    public Double getUtilizationRate() {
        return utilizationRate;
    }

    public void setUtilizationRate(Double utilizationRate) {
        this.utilizationRate = utilizationRate;
    }
}