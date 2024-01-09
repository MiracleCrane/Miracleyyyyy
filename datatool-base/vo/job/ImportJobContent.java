/*
 * 文 件 名:  ImportJobContent.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/22
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.vo.job;

import com.huawei.smartcampus.datatool.entity.DtBatchJobDependenceEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobNodeEntity;
import com.huawei.smartcampus.datatool.entity.DtSqlScriptNodeDetailEntity;

import java.util.List;

/**
 * 导入作业模型类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/22]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ImportJobContent {
    private String dir;
    private DtBatchJobEntity jobEntity;
    private List<DtBatchJobDependenceEntity> jobDependenceEntities;
    private List<DtBatchJobNodeEntity> jobNodeEntities;
    private DtSqlScriptNodeDetailEntity scriptNodeDetailEntity;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public DtBatchJobEntity getJobEntity() {
        return jobEntity;
    }

    public void setJobEntity(DtBatchJobEntity jobEntity) {
        this.jobEntity = jobEntity;
    }

    public List<DtBatchJobDependenceEntity> getJobDependenceEntities() {
        return jobDependenceEntities;
    }

    public void setJobDependenceEntities(List<DtBatchJobDependenceEntity> jobDependenceEntities) {
        this.jobDependenceEntities = jobDependenceEntities;
    }

    public List<DtBatchJobNodeEntity> getJobNodeEntities() {
        return jobNodeEntities;
    }

    public void setJobNodeEntities(List<DtBatchJobNodeEntity> jobNodeEntities) {
        this.jobNodeEntities = jobNodeEntities;
    }

    public DtSqlScriptNodeDetailEntity getScriptNodeDetailEntity() {
        return scriptNodeDetailEntity;
    }

    public void setScriptNodeDetailEntity(DtSqlScriptNodeDetailEntity scriptNodeDetailEntity) {
        this.scriptNodeDetailEntity = scriptNodeDetailEntity;
    }
}