/*
 * 文 件 名:  BatchJobRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository;

import com.huawei.smartcampus.datatool.entity.DagRunEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobDependenceEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobDirEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobNodeEntity;
import com.huawei.smartcampus.datatool.entity.DtSqlScriptNodeDetailEntity;
import com.huawei.smartcampus.datatool.entity.TaskInstanceEntity;
import com.huawei.smartcampus.datatool.enums.PeriodUnit;
import com.huawei.smartcampus.datatool.enums.ScheduleType;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.batchjob.BatchJobInstanceStateCountVo;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.batchjob.DagRunState;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.BatchJobDetail;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job.BundledScriptInfo;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata.BatchJobAlarmThreshold;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.BatchJobGateway;
import com.huawei.smartcampus.datatool.monitor.domain.overview.GroupStatisticsEnum;
import com.huawei.smartcampus.datatool.monitor.domain.overview.JobStatisticsType;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.factory.CustomAssetStrategyFactory;
import com.huawei.smartcampus.datatool.repository.DtBatchJobDependenceRepository;
import com.huawei.smartcampus.datatool.repository.DtBatchJobDirRepository;
import com.huawei.smartcampus.datatool.repository.DtBatchJobNodeRepository;
import com.huawei.smartcampus.datatool.repository.DtBatchJobRepository;
import com.huawei.smartcampus.datatool.repository.TaskInstanceRepository;
import com.huawei.smartcampus.datatool.utils.StringUtils;
import com.huawei.smartcampus.datatool.vo.BatchRunningJobInstanceCountVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 批处理数据仓库
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Component
public class BatchJobRepository implements BatchJobGateway {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchJobRepository.class);

    private static final String COMMA = ",";
    private final String[] waitingState = {"queued", "scheduled", "up_for_retry", "up_for_reschedule"};

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TaskInstanceRepository taskInstanceRepository;

    @Autowired
    private DtBatchJobRepository dtBatchJobRepository;

    @Autowired
    private DtBatchJobNodeRepository dtBatchJobNodeRepository;

    @Autowired
    private DtBatchJobDirRepository dtBatchJobDirRepository;

    @Autowired
    private DtBatchJobDependenceRepository dtBatchJobDependenceRepository;

    @Override
    public List<BatchJobInstanceStateCountVo> queryBatchJobInstanceStateCount(Timestamp startDate, Timestamp endDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BatchJobInstanceStateCountVo> countVoCriteriaQuery = criteriaBuilder
                .createQuery(BatchJobInstanceStateCountVo.class);
        Root<TaskInstanceEntity> root = countVoCriteriaQuery.from(TaskInstanceEntity.class);
        Join<TaskInstanceEntity, DagRunEntity> joinDagRun = root.join("dagRunEntity", JoinType.LEFT);
        // 关联batch_job_node表过滤测试运行作业
        Join<TaskInstanceEntity, DtBatchJobNodeEntity> joinBatchJobNode = root.join("dtBatchJobNodeEntity",
                JoinType.LEFT);
        Predicate preFinal = queryBatchJobInstancePredicate(startDate, endDate, joinDagRun, joinBatchJobNode);
        countVoCriteriaQuery
                .multiselect(joinDagRun.get("state").alias("state"),
                        criteriaBuilder.count(joinDagRun.get("state")).alias("count"))
                .where(preFinal).groupBy(joinDagRun.get("state"));
        TypedQuery<BatchJobInstanceStateCountVo> typedQuery = entityManager.createQuery(countVoCriteriaQuery);
        List<BatchJobInstanceStateCountVo> result = typedQuery.getResultList();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("query batch job instance state count result: {}", result);
        }
        return result;
    }

    @Override
    public List<Long> queryBatchJobInstanceStateWaitingCount(Timestamp startDate, Timestamp endDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countVoCriteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<TaskInstanceEntity> root = countVoCriteriaQuery.from(TaskInstanceEntity.class);
        Join<TaskInstanceEntity, DagRunEntity> joinDagRun = root.join("dagRunEntity", JoinType.LEFT);
        Join<TaskInstanceEntity, DtBatchJobNodeEntity> joinBatchJobNode = root.join("dtBatchJobNodeEntity",
                JoinType.LEFT);
        List<Predicate> predicateFinalList = new ArrayList<>();
        Predicate preAnd = queryBatchJobInstancePredicate(startDate, endDate, joinDagRun, joinBatchJobNode);
        predicateFinalList.add(preAnd);
        predicateFinalList.add(criteriaBuilder.equal(joinDagRun.get("state"), DagRunState.RUNNING.value()));
        // 获取dag_run状态是running以及task_instance在WAITING_STATE或者state是null的数量
        List<Predicate> predicateOrList = new ArrayList<>();
        predicateOrList.add(root.get("state").in(waitingState));
        predicateOrList.add(root.get("state").isNull());
        Predicate preOr = criteriaBuilder.or(predicateOrList.toArray(new Predicate[0]));
        predicateFinalList.add(preOr);
        Predicate preFinal = criteriaBuilder.and(predicateFinalList.toArray(new Predicate[0]));
        countVoCriteriaQuery.select(criteriaBuilder.count(root.get("dagId")).alias("count")).where(preFinal);
        TypedQuery<Long> typedQuery = entityManager.createQuery(countVoCriteriaQuery);
        List<Long> result = typedQuery.getResultList();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("query batch job instance state waiting count result is {}", result);
        }
        return result;
    }

    @Override
    public List<BatchRunningJobInstanceCountVo> queryBatchRunningJobInstanceCount(Integer interval, Timestamp startDate,
            Timestamp endDate) {
        return taskInstanceRepository.findRunningJobInstanceCountByStartDateAndInterval(interval, startDate, endDate);
    }

    @Override
    public List<String> queryBatchRunningJobId() {
        List<String> jobList = new ArrayList<>();
        List<DtBatchJobEntity> dtBatchJobEntities = dtBatchJobRepository.findByState(true);
        if (dtBatchJobEntities == null || dtBatchJobEntities.isEmpty()) {
            return jobList;
        }
        for (DtBatchJobEntity dtBatchJobEntity : dtBatchJobEntities) {
            jobList.add(dtBatchJobEntity.getId());
        }
        return jobList;
    }

    @Override
    public List<BatchJobAlarmThreshold> queryBatchJobAlarmThreshold() {
        List<DtBatchJobNodeEntity> dtBatchJobNodeEntities = dtBatchJobNodeRepository.findAll();
        if (dtBatchJobNodeEntities.isEmpty()) {
            return new ArrayList<>();
        }
        List<BatchJobAlarmThreshold> jobAlarmThresholdList = new ArrayList<>();
        for (DtBatchJobNodeEntity dtBatchJobNodeEntity : dtBatchJobNodeEntities) {
            BatchJobAlarmThreshold batchJobAlarmThreshold = new BatchJobAlarmThreshold(dtBatchJobNodeEntity.getJobId(),
                    (long) dtBatchJobNodeEntity.getAlarmThreshold());
            jobAlarmThresholdList.add(batchJobAlarmThreshold);
        }
        return jobAlarmThresholdList;
    }

    @Override
    public List<String> getJobFullDir(String id) {
        Optional<DtBatchJobEntity> dtBatchJobEntity = dtBatchJobRepository.findById(id);
        if (!dtBatchJobEntity.isPresent()) {
            return Collections.emptyList();
        }
        String dirId = dtBatchJobEntity.get().getDirId();
        List<String> dirList = new ArrayList<>();
        while (StringUtils.isNotEmpty(dirId)) {
            Optional<DtBatchJobDirEntity> dtBatchJobDirEntityOptional = dtBatchJobDirRepository.findById(dirId);
            if (!dtBatchJobDirEntityOptional.isPresent()) {
                break;
            }
            dirList.add(dtBatchJobDirEntityOptional.get().getName());
            dirId = dtBatchJobDirEntityOptional.get().getParentId();
        }
        // 逆序输出，保证目录顺序
        Collections.reverse(dirList);
        return dirList;
    }

    @Override
    public List<BatchJobDetail> getBatchJobDetail() {
        List<DtBatchJobEntity> dtBatchJobEntities = dtBatchJobRepository.findAll();
        List<BatchJobDetail> batchJobDetails = new ArrayList<>();
        Map<String, String> bundledScriptInfos = getBundledScript();
        for (DtBatchJobEntity dtBatchJobEntity : dtBatchJobEntities) {
            BatchJobDetail detail = new BatchJobDetail();
            String jobId = dtBatchJobEntity.getId();
            // 获取目录名
            Optional<DtBatchJobDirEntity> dtBatchJobDirEntityOptional = dtBatchJobDirRepository
                    .findById(dtBatchJobEntity.getDirId());
            dtBatchJobDirEntityOptional.ifPresent(dtBatchJobDirEntity -> detail.setDir(dtBatchJobDirEntity.getName()));
            // 获取作业名
            detail.setName(dtBatchJobEntity.getName());
            // 获取来源
            detail.setOrigin(CustomAssetStrategyFactory.createStrategy(JobStatisticsType.BATCH_JOB).isCustom(jobId)
                    ? GroupStatisticsEnum.CUSTOM.value()
                    : GroupStatisticsEnum.BASELINE.value());
            // 获取关联脚本
            detail.setBundledScript(bundledScriptInfos.getOrDefault(jobId, ""));
            // 获取调度信息
            handleScheduleInfo(dtBatchJobEntity, detail);
            // 获取自依赖信息
            detail.setSelfDependent(dtBatchJobEntity.getSelfDependence());
            // 获取依赖的作业
            List<String> dependNames = new ArrayList<>();
            List<DtBatchJobDependenceEntity> dtBatchJobDependenceEntityList = dtBatchJobDependenceRepository
                    .findDtBatchJobDependenceEntitiesByJobName(dtBatchJobEntity.getName());
            for (DtBatchJobDependenceEntity dtBatchJobDependenceEntity : dtBatchJobDependenceEntityList) {
                dependNames.add(dtBatchJobDependenceEntity.getDependJobName());
            }
            detail.setDependJobs(String.join(COMMA, dependNames));
            // 获取是否启动
            detail.setRunning(dtBatchJobEntity.getState());
            batchJobDetails.add(detail);
        }
        return batchJobDetails;
    }

    private Predicate queryBatchJobInstancePredicate(Timestamp startDate, Timestamp endDate,
            Join<TaskInstanceEntity, DagRunEntity> joinDagRun,
            Join<TaskInstanceEntity, DtBatchJobNodeEntity> joinBatchJobNode) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        List<Predicate> predicateAndList = new ArrayList<>();
        predicateAndList.add(criteriaBuilder.between(joinDagRun.get("startDate"), startDate, endDate));
        predicateAndList.add(criteriaBuilder.isNotNull(joinBatchJobNode.get("id")));
        return criteriaBuilder.and(predicateAndList.toArray(new Predicate[0]));
    }

    private Map<String, String> getBundledScript() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BundledScriptInfo> getScriptNameCriteriaQuery = criteriaBuilder
                .createQuery(BundledScriptInfo.class);
        Root<DtSqlScriptNodeDetailEntity> root = getScriptNameCriteriaQuery.from(DtSqlScriptNodeDetailEntity.class);
        Join<DtSqlScriptNodeDetailEntity, DtBatchJobNodeEntity> joinBatchJobNode = root.join("dtBatchJobNodeEntity",
                JoinType.LEFT);
        Join<DtBatchJobNodeEntity, DtBatchJobEntity> joinBatchJob = joinBatchJobNode.join("dtBatchJobEntity",
                JoinType.LEFT);
        getScriptNameCriteriaQuery.multiselect(joinBatchJob.get("id").alias("jobId"),
                root.get("scriptName").alias("scriptName"));
        TypedQuery<BundledScriptInfo> typedQuery = entityManager.createQuery(getScriptNameCriteriaQuery);
        List<BundledScriptInfo> result = typedQuery.getResultList();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("query batch job instance state count result: {}", result);
        }
        if (result == null || result.isEmpty()) {
            return new HashMap<>();
        }
        return result.stream().collect(Collectors.toMap(BundledScriptInfo::getJobId, BundledScriptInfo::getScriptName));
    }

    private void handleScheduleInfo(DtBatchJobEntity dtBatchJobEntity, BatchJobDetail detail) {
        detail.setScheduleType(dtBatchJobEntity.getScheduleType());
        if (ScheduleType.ONCE.value().equals(dtBatchJobEntity.getScheduleType())) {
            return;
        }
        String periodUnit = dtBatchJobEntity.getPeriod();
        detail.setPeriodUnit(periodUnit);
        switch (PeriodUnit.getPeriodUnitByValue(periodUnit)) {
            case MINUTE:
            case HOUR:
                detail.setPeriodInterval(String.valueOf(dtBatchJobEntity.getPeriodInterval()));
                break;
            case DAY:
                // 调度周期为天，时间间隔默认为1天
                detail.setPeriodInterval("1");
                break;
            case WEEK:
            case MONTH:
                detail.setPeriodInterval(dtBatchJobEntity.getPeriodDay());
                break;
            default:
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PERIOD_NOT_SUPPORT, periodUnit);
        }
        String startTime = dtBatchJobEntity.getPeriodStartTime();
        String endTime = dtBatchJobEntity.getPeriodEndTime();
        String scheduleTimePerDay = StringUtils.isEmpty(endTime) ? startTime : startTime + "-" + endTime;
        detail.setScheduleTimePerDay(scheduleTimePerDay);
    }
}