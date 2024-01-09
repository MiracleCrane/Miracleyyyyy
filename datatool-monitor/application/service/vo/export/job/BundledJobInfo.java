/*
 * 文 件 名:  BundledJobInfo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/12/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job;

/**
 * 关联作业信息
 *
 * @author j00826364
 * @version [Core&Link 23.1, 2023/12/1]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class BundledJobInfo {
    private String scriptId;
    private String jobName;

    public BundledJobInfo() {
    }

    public BundledJobInfo(String scriptId, String jobName) {
        this.scriptId = scriptId;
        this.jobName = jobName;
    }

    public String getScriptId() {
        return scriptId;
    }

    public void setScriptId(String scriptId) {
        this.scriptId = scriptId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}