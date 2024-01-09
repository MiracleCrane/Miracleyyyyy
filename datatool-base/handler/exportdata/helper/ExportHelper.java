/*
 * 文 件 名:  ExportHelper.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.handler.exportdata.helper;

import com.huawei.smartcampus.datatool.base.enumeration.DataToolResourceTypeEnum;
import com.huawei.smartcampus.datatool.entity.DtBatchJobDependenceEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobDirEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobNodeEntity;
import com.huawei.smartcampus.datatool.entity.DtCipherVariableEntity;
import com.huawei.smartcampus.datatool.entity.DtConnectionEntity;
import com.huawei.smartcampus.datatool.entity.DtEnvironmentVariableEntity;
import com.huawei.smartcampus.datatool.entity.DtScriptDirEntity;
import com.huawei.smartcampus.datatool.entity.DtScriptEntity;
import com.huawei.smartcampus.datatool.entity.DtSqlScriptNodeDetailEntity;
import com.huawei.smartcampus.datatool.entity.StreamJobEntity;
import com.huawei.smartcampus.datatool.repository.DtBatchJobDependenceRepository;
import com.huawei.smartcampus.datatool.repository.DtBatchJobDirRepository;
import com.huawei.smartcampus.datatool.repository.DtBatchJobNodeRepository;
import com.huawei.smartcampus.datatool.repository.DtBatchJobRepository;
import com.huawei.smartcampus.datatool.repository.DtCipherVariableRepository;
import com.huawei.smartcampus.datatool.repository.DtConnectionRepository;
import com.huawei.smartcampus.datatool.repository.DtEnvironmentVariableRepository;
import com.huawei.smartcampus.datatool.repository.DtScriptDirRepository;
import com.huawei.smartcampus.datatool.repository.DtScriptRepository;
import com.huawei.smartcampus.datatool.repository.DtSqlScriptNodeDetailRepository;
import com.huawei.smartcampus.datatool.repository.StreamJobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * 导出帮助类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/20]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Component
@Transactional
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ExportHelper {
    /**
     * 脚本和作业的根目录id，默认为-1
     */
    private static final String ROOT_DIR_ID = "-1";

    @Autowired
    private DtScriptDirRepository scriptDirRepository;

    @Autowired
    private DtBatchJobDirRepository jobDirRepository;

    @Autowired
    private DtCipherVariableRepository dtCipherVariableRepository;

    @Autowired
    private DtConnectionRepository dtConnectionRepository;

    @Autowired
    private DtEnvironmentVariableRepository dtEnvironmentVariableRepository;

    @Autowired
    private StreamJobRepository streamJobRepository;

    @Autowired
    private DtBatchJobRepository jobRepository;

    @Autowired
    private DtScriptRepository scriptRepository;

    @Autowired
    private DtBatchJobNodeRepository jobNodeRepository;

    @Autowired
    private DtSqlScriptNodeDetailRepository detailRepository;

    @Autowired
    private DtBatchJobDependenceRepository jobDependenceRepository;

    /**
     * 获取目录
     *
     * @param dirId 目录的id
     * @param type 类型
     * @return 全目录
     */
    public String getDirectory(String dirId, String type) {
        StringBuilder dir = new StringBuilder();
        // 如果是-1，表示是根目录，导出时为"/"
        if (ROOT_DIR_ID.equals(dirId)) {
            dir.append(File.separator);
            return dir.toString();
        }
        // 如果不是根目录，递归遍历一直往上找父目录，拼接全路径目录结构
        buildDirectory(dirId, dir, type);
        return dir.toString();
    }

    /**
     * 组装脚本目录结构，递归实现
     *
     * @param parentId 父节点id
     * @param sb 记录目录结果
     * @param type 目录类型
     */
    private void buildDirectory(String parentId, StringBuilder sb, String type) {
        // 构建脚本目录
        if (DataToolResourceTypeEnum.SCRIPT.type().equals(type)) {
            DtScriptDirEntity scriptDirEntity = scriptDirRepository.findDtScriptDirEntityById(parentId);
            if (scriptDirEntity.getParentId() != null) {
                buildDirectory(scriptDirEntity.getParentId(), sb, type);
                sb.append(File.separator).append(scriptDirEntity.getName());
            }
        }
        // 构建作业目录
        if (DataToolResourceTypeEnum.JOB.type().equals(type)) {
            DtBatchJobDirEntity jobDirEntity = jobDirRepository.findDtBatchJobDirEntityById(parentId);
            if (jobDirEntity.getParentId() != null) {
                buildDirectory(jobDirEntity.getParentId(), sb, type);
                sb.append(File.separator).append(jobDirEntity.getName());
            }
        }
    }

    /**
     * 根据id列表，查询密码箱实体
     *
     * @param ids id列表
     * @return 密码箱实体列表
     */
    public List<DtCipherVariableEntity> findCipherVariableEntitiesByIdIn(List<String> ids) {
        return dtCipherVariableRepository.findCipherVariableEntitiesByIdIn(ids);
    }

    /**
     * 根据id列表，查询数据连接实体
     *
     * @param ids id列表
     * @return 数据连接实体列表
     */
    public List<DtConnectionEntity> findDtConnectionEntitiesByIdIn(List<String> ids) {
        return dtConnectionRepository.findDtConnectionEntitiesByIdIn(ids);
    }

    /**
     * 根据id列表，查询环境变量实体
     *
     * @param ids id列表
     * @return 环境变量实体列表
     */
    public List<DtEnvironmentVariableEntity> findDtEnvironmentVariableEntitiesByIdIn(List<String> ids) {
        return dtEnvironmentVariableRepository.findDtEnvironmentVariableEntitiesByIdIn(ids);
    }

    /**
     * 根据id列表，查询流处理作业实体
     *
     * @param ids id列表
     * @return 流处理作业实体列表
     */
    public List<StreamJobEntity> findStreamJobEntitiesByIdIn(List<String> ids) {
        return streamJobRepository.findStreamJobEntitiesByIdIn(ids);
    }

    /**
     * 根据id列表，查询批处理作业实体
     *
     * @param ids id列表
     * @return 批处理作业实体列表
     */
    public List<DtBatchJobEntity> findDtBatchJobEntitiesByIdIn(List<String> ids) {
        return jobRepository.findDtBatchJobEntitiesByIdIn(ids);
    }

    /**
     * 根据id列表，查询脚本实体
     *
     * @param ids id列表
     * @return 脚本实体列表
     */
    public List<DtScriptEntity> findDtScriptEntitiesByIdIn(List<String> ids) {
        return scriptRepository.findDtScriptEntitiesByIdIn(ids);
    }

    /**
     * 根据jobId，查询作业节点信息
     *
     * @param jobId 作业id
     * @return 作业节点信息列表
     */
    public List<DtBatchJobNodeEntity> findDtBatchJobNodeEntitiesByJobId(String jobId) {
        return jobNodeRepository.findDtBatchJobNodeEntitiesByJobId(jobId);
    }

    /**
     * 根据jobNodeId，查询脚本节点详情信息
     *
     * @param jobNodeId 节点id
     * @return 脚本节点详情信息
     */
    public DtSqlScriptNodeDetailEntity findDtSqlScriptNodeDetailEntityByJobNodeId(String jobNodeId) {
        return detailRepository.findDtSqlScriptNodeDetailEntityByJobNodeId(jobNodeId);
    }

    /**
     * 根据作业名，查询作业依赖信息
     *
     * @param jobName 作业名
     * @return 作业依赖信息
     */
    public List<DtBatchJobDependenceEntity> findDtBatchJobDependenceEntitiesByJobName(String jobName) {
        return jobDependenceRepository.findDtBatchJobDependenceEntitiesByJobName(jobName);
    }
}