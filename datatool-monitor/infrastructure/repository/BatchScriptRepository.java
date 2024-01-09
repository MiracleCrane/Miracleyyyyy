/*
 * 文 件 名:  BatchScriptRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository;

import com.huawei.smartcampus.datatool.entity.DtBatchJobEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobNodeEntity;
import com.huawei.smartcampus.datatool.entity.DtScriptDirEntity;
import com.huawei.smartcampus.datatool.entity.DtScriptEntity;
import com.huawei.smartcampus.datatool.entity.DtSqlScriptNodeDetailEntity;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job.BatchScriptDetail;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job.BundledJobInfo;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.BatchScriptGateWay;
import com.huawei.smartcampus.datatool.monitor.domain.overview.GroupStatisticsEnum;
import com.huawei.smartcampus.datatool.monitor.domain.overview.JobStatisticsType;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.factory.CustomAssetStrategyFactory;
import com.huawei.smartcampus.datatool.repository.DtScriptDirRepository;
import com.huawei.smartcampus.datatool.repository.DtScriptRepository;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 * 批处理脚本目录仓库
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/16]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Component
public class BatchScriptRepository implements BatchScriptGateWay {
    @Autowired
    private DtScriptRepository dtScriptRepository;

    @Autowired
    private DtScriptDirRepository dtScriptDirRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<String> getScriptFullDir(String id) {
        Optional<DtScriptEntity> dtScriptEntityOptional = dtScriptRepository.findById(id);
        if (!dtScriptEntityOptional.isPresent()) {
            return Collections.emptyList();
        }
        String dirId = dtScriptEntityOptional.get().getDirId();
        List<String> dirList = new ArrayList<>();
        while (!StringUtils.isEmpty(dirId)) {
            Optional<DtScriptDirEntity> dtScriptDirEntityOptional = dtScriptDirRepository.findById(dirId);
            if (!dtScriptDirEntityOptional.isPresent()) {
                break;
            }
            dirList.add(dtScriptDirEntityOptional.get().getName());
            dirId = dtScriptDirEntityOptional.get().getParentId();
        }
        // 逆序输出，保证目录顺序
        Collections.reverse(dirList);
        return dirList;
    }

    @Override
    public List<BatchScriptDetail> getScriptDetail() {
        List<DtScriptEntity> dtScriptEntityList = dtScriptRepository.findAll();
        List<BatchScriptDetail> batchScriptDetails = new ArrayList<>();
        Map<String, String> bundledJobMap = getBundledJob();
        for (DtScriptEntity dtScriptEntity : dtScriptEntityList) {
            BatchScriptDetail batchScriptDetail = new BatchScriptDetail();
            String scriptId = dtScriptEntity.getId();
            String scriptName = dtScriptEntity.getName();
            // 获取目录名
            Optional<DtScriptDirEntity> dtScriptDirEntityOptional = dtScriptDirRepository
                    .findById(dtScriptEntity.getDirId());
            dtScriptDirEntityOptional
                    .ifPresent(dtScriptDirEntity -> batchScriptDetail.setDir(dtScriptDirEntity.getName()));
            // 获取脚本名
            batchScriptDetail.setName(scriptName);
            // 获取来源
            batchScriptDetail.setOrigin(
                    CustomAssetStrategyFactory.createStrategy(JobStatisticsType.BATCH_SCRIPT).isCustom(scriptId)
                            ? GroupStatisticsEnum.CUSTOM.value()
                            : GroupStatisticsEnum.BASELINE.value());
            // 获取关联作业
            batchScriptDetail.setBundledJobs(bundledJobMap.getOrDefault(scriptName, ""));
            batchScriptDetails.add(batchScriptDetail);
        }
        return batchScriptDetails;
    }

    private Map<String, String> getBundledJob() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BundledJobInfo> getScriptNameCriteriaQuery = criteriaBuilder.createQuery(BundledJobInfo.class);
        Root<DtSqlScriptNodeDetailEntity> root = getScriptNameCriteriaQuery.from(DtSqlScriptNodeDetailEntity.class);
        Join<DtSqlScriptNodeDetailEntity, DtBatchJobNodeEntity> joinBatchJobNode = root.join("dtBatchJobNodeEntity",
                JoinType.LEFT);
        Join<DtBatchJobNodeEntity, DtBatchJobEntity> joinBatchJob = joinBatchJobNode.join("dtBatchJobEntity",
                JoinType.LEFT);
        getScriptNameCriteriaQuery.multiselect(root.get("scriptName").alias("scriptName"),
                joinBatchJob.get("name").alias("jobName"));
        TypedQuery<BundledJobInfo> typedQuery = entityManager.createQuery(getScriptNameCriteriaQuery);
        List<BundledJobInfo> result = typedQuery.getResultList();
        Map<String, String> bundledJobMap = new HashMap<>();
        if (result == null || result.isEmpty()) {
            return bundledJobMap;
        }
        for (BundledJobInfo bundledJobInfo : result) {
            String scriptId = bundledJobInfo.getScriptId();
            String jobName = bundledJobInfo.getJobName();
            // 多个关联作业用","拼接
            bundledJobMap.merge(scriptId, jobName, (item1, item2) -> String.join(",", item1, item2));
        }
        return bundledJobMap;
    }
}