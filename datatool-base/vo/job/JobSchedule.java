/*
 * 文 件 名:  JobSchedule.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/21
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.vo.job;

import java.util.List;

/**
 * 作业调度
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/21]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class JobSchedule {
    private String dependFailPolicy;
    private List<String> dependJobNames;
    private Boolean dependSelf;
    private String type;
    private Cron cron;

    public String getDependFailPolicy() {
        return dependFailPolicy;
    }

    public void setDependFailPolicy(String dependFailPolicy) {
        this.dependFailPolicy = dependFailPolicy;
    }

    public List<String> getDependJobNames() {
        return dependJobNames;
    }

    public void setDependJobNames(List<String> dependJobNames) {
        this.dependJobNames = dependJobNames;
    }

    public Boolean isDependSelf() {
        return dependSelf;
    }

    public void setDependSelf(Boolean dependSelf) {
        this.dependSelf = dependSelf;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Cron getCron() {
        return cron;
    }

    public void setCron(Cron cron) {
        this.cron = cron;
    }
}