/*
 * 文 件 名:  BundledScriptInfo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job;

/**
 * 关联脚本信息
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class BundledScriptInfo {
    private String jobId;
    private String scriptName;

    public BundledScriptInfo() {
    }

    public BundledScriptInfo(String jobId, String scriptName) {
        this.jobId = jobId;
        this.scriptName = scriptName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }
}