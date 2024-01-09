/*
 * 文 件 名:  JobBusinessServiceImpl.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.service.impl;

import com.huawei.smartcampus.datatool.entity.StreamJobEntity;
import com.huawei.smartcampus.datatool.enums.JobStatus;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.properties.QuantitativeConfig;
import com.huawei.smartcampus.datatool.repository.StreamJobRepository;
import com.huawei.smartcampus.datatool.stream.mgt.enums.FlinkOperationName;
import com.huawei.smartcampus.datatool.stream.mgt.service.BusinessService;
import com.huawei.smartcampus.datatool.stream.mgt.service.FlinkService;
import com.huawei.smartcampus.datatool.stream.mgt.service.impl.util.FlinkServiceUtils;
import com.huawei.smartcampus.datatool.stream.mgt.service.key.ReliablityKey;
import com.huawei.smartcampus.datatool.stream.mgt.vo.BatchOperateResult;
import com.huawei.smartcampus.datatool.stream.mgt.vo.BatchOperateResultDetail;
import com.huawei.smartcampus.datatool.stream.mgt.vo.DeleteStreamJobsReq;
import com.huawei.smartcampus.datatool.stream.mgt.vo.JobStatusInfo;
import com.huawei.smartcampus.datatool.stream.mgt.vo.QueryDataStreamJobInfoResult;
import com.huawei.smartcampus.datatool.stream.mgt.vo.QueryStreamJobsResult;
import com.huawei.smartcampus.datatool.stream.mgt.vo.StreamJobQueryCondi;
import com.huawei.smartcampus.datatool.stream.mgt.vo.StreamJobReq;
import com.huawei.smartcampus.datatool.stream.mgt.vo.StreamJobRuntimeInfo;
import com.huawei.smartcampus.datatool.stream.mgt.vo.ValidateNameReq;
import com.huawei.smartcampus.datatool.stream.mgt.vo.ValidateNameResp;
import com.huawei.smartcampus.datatool.stream.mgt.vo.flink.FlinkBasicInfo;
import com.huawei.smartcampus.datatool.utils.CommonUtil;
import com.huawei.smartcampus.datatool.utils.RequestContext;
import com.huawei.smartcampus.datatool.utils.StringUtils;
import com.huawei.smartcampus.datatool.vo.QueryMaxJobNumResp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 业务功能实现类
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Service
public class JobBusinessServiceImpl implements BusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobBusinessServiceImpl.class);

    @Autowired
    private StreamJobRepository jobRepository;

    @Autowired
    private FlinkService flinkService;

    @Autowired
    private FlinkServiceUtils flinkServiceUtils;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    @Transactional
    public BatchOperateResult deleteJobs(DeleteStreamJobsReq deleteStreamJobsReq) {
        Map<String, FlinkBasicInfo> idStatusMap = flinkService.getJobsStatus();
        List<String> needDeleteIds = new ArrayList<>(10);
        List<BatchOperateResultDetail> details = new ArrayList<>(10);
        int failureCount = 0;
        int successCount = 0;
        for (String id : deleteStreamJobsReq.getIds()) {
            Optional<StreamJobEntity> entityOptional = jobRepository.findById(id);
            // 如果id不存在，构造成功响应，success+1
            if (!entityOptional.isPresent()) {
                details.add(flinkServiceUtils.constructSuccessResp(id, null));
                successCount++;
                continue;
            }
            StreamJobEntity entity = entityOptional.get();
            FlinkBasicInfo basicInfo = idStatusMap.get(entity.getFlinkId());
            // 是否是强制删除，如果不是
            if (!deleteStreamJobsReq.getIsForce()) {
                if (basicInfo == null || basicInfo.getStatus().judgeBeDeleted()) {
                    needDeleteIds.add(entity.getId());
                    details.add(flinkServiceUtils.constructSuccessResp(entity.getId(), entity.getName()));
                    successCount++;
                    continue;
                }
                details.add(flinkServiceUtils.constructFailureResp(entity.getId(), entity.getName(),
                        ExceptionCode.DATATOOL_STREAM_JOB_STATE_ERROR));
                failureCount++;
                continue;
            }
            if (basicInfo == null || basicInfo.getStatus().checkAutoDelete()) {
                needDeleteIds.add(entity.getId());
                details.add(flinkServiceUtils.constructSuccessResp(entity.getId(), entity.getName()));
                successCount++;
                continue;
            }
            if (basicInfo.getStatus().judgeRun()) {
                // 强制删除运行中的作业，先停再删
                List<String> entityIds = new ArrayList<>();
                entityIds.add(entity.getId());
                flinkService.batchOperation(entityIds, FlinkOperationName.CANCEL, Short.valueOf("0"));
                needDeleteIds.add(entity.getId());
                details.add(flinkServiceUtils.constructSuccessResp(entity.getId(), entity.getName()));
                successCount++;
                continue;
            }
            details.add(flinkServiceUtils.constructFailureResp(entity.getId(), entity.getName(),
                    ExceptionCode.DATATOOL_FORCE_DELETE_STATE_NOT_SUPPORT));
            failureCount++;
        }
        jobRepository.deleteStreamJobEntitiesByIdIn(needDeleteIds);
        LOGGER.info("delete job success,id is {}", needDeleteIds);
        return new BatchOperateResult(details, failureCount, successCount, failureCount + successCount);
    }

    @Override
    public String saveJob(StreamJobReq jobInfo) {
        // 校验是否超过100个
        flinkServiceUtils.checkStreamJobNum();
        // 校验数据库中是否存在同名作业
        flinkServiceUtils.checkJobNameDuplicate(jobInfo.getName());
        StreamJobEntity repositoryJob = new StreamJobEntity();
        BeanUtils.copyProperties(jobInfo, repositoryJob, "id");
        repositoryJob.setState(ReliablityKey.UNSUBMITTED_STATE);
        repositoryJob.setCreatedBy(RequestContext.getUserName());
        repositoryJob.setFlinkSql(CommonUtil.base64EncodeString(jobInfo.getFlinkSql()));
        repositoryJob.setLastModifiedBy(RequestContext.getUserName());
        repositoryJob.setLastModifiedDate(new Date());
        // 双重锁，保证并发创建限制
        synchronized (JobBusinessServiceImpl.class) {
            TransactionStatus status = transactionManager
                    .getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
            try {
                flinkServiceUtils.checkStreamJobNum();
                flinkServiceUtils.checkJobNameDuplicate(jobInfo.getName());
                StreamJobEntity streamJobEntity = jobRepository.save(repositoryJob);
                transactionManager.commit(status);
                LOGGER.info("save job success, job name is {}", streamJobEntity.getName());
                return streamJobEntity.getId();
            } catch (DataToolRuntimeException exception) {
                transactionManager.rollback(status);
                throw exception;
            } catch (Exception e) {
                LOGGER.error("save job failed", e);
                transactionManager.rollback(status);
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_ERROR);
            }
        }
    }

    @Override
    public String updateJob(String jobId, StreamJobReq jobInfo) {
        // 判断入参 id 是否存在，不存在则抛出异常
        if (jobId == null) {
            throw new IllegalArgumentException("id不能为空!");
        }
        // 根据 id 判断表中是否存在此作业，不存在则抛出异常
        Optional<StreamJobEntity> repositoryJobOptional = jobRepository.findById(jobId);
        StreamJobEntity repositoryJob = repositoryJobOptional
                .orElseThrow(() -> new DataToolRuntimeException(ExceptionCode.DATATOOL_JOB_NOT_EXIST));
        flinkServiceUtils.checkJobNameDuplicate(repositoryJob.getId(), jobInfo.getName());
        if (flinkService.isJobRunning(repositoryJob.getFlinkId())) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_JOB_IN_USE);
        }
        StreamJobEntity updateJob = new StreamJobEntity();
        BeanUtils.copyProperties(jobInfo, updateJob, "id");
        updateJob.setId(repositoryJob.getId());
        updateJob.setState(repositoryJob.getState());
        updateJob.setRequestId(repositoryJob.getRequestId());
        updateJob.setSavepointPath(repositoryJob.getSavepointPath());
        updateJob.setFlinkSql(CommonUtil.base64EncodeString(jobInfo.getFlinkSql()));
        // 双重检查，保证并发编辑为同名作业时，提示重名报错，而不是提示系统异常报错
        synchronized (JobBusinessServiceImpl.class) {
            TransactionStatus status = transactionManager
                    .getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
            try {
                flinkServiceUtils.checkJobNameDuplicate(updateJob.getId(), jobInfo.getName());
                StreamJobEntity streamJobEntity = jobRepository.save(updateJob);
                transactionManager.commit(status);
                LOGGER.info("update job success, job name is {}", streamJobEntity.getName());
                return streamJobEntity.getId();
            } catch (DataToolRuntimeException exception) {
                transactionManager.rollback(status);
                throw exception;
            } catch (Exception e) {
                LOGGER.error("update job failed", e);
                transactionManager.rollback(status);
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_ERROR);
            }
        }
    }

    /**
     * 校验页码
     *
     * @param condition 查询条件
     * @param size 页数
     * @return 校验结果
     */
    public int checkPageNum(StreamJobQueryCondi condition, int size) {
        int pageIndex = condition.getPageIndex();

        if ((pageIndex - 1) * condition.getPageSize() >= size) {
            pageIndex = (size % condition.getPageSize()) == 0
                    ? (size / condition.getPageSize())
                    : (size / condition.getPageSize() + 1);
        }
        return Integer.max(1, pageIndex);
    }

    /**
     * 查询实时流任务
     *
     * @param condition 查询条件
     * @return 任务明细
     */
    @Override
    public QueryStreamJobsResult queryJobs(StreamJobQueryCondi condition) {
        Specification<StreamJobEntity> specification = queryStreamJobSpecification(condition);
        Map<String, FlinkBasicInfo> idStatusMap = flinkService.getJobsStatus();
        List<StreamJobEntity> entities = jobRepository.findAll(specification);
        // 按照状态过滤查询到的实体逻辑
        entities = filterJobsByStatus(condition, entities, idStatusMap);
        QueryStreamJobsResult queryStreamJobsResult = new QueryStreamJobsResult();
        queryStreamJobsResult.setTotal(entities.size());
        entities.sort((item1, item2) -> {
            if (null == item1.getLastModifiedDate()) {
                return -1;
            }
            if (null == item2.getLastModifiedDate()) {
                return 1;
            }
            return item2.getLastModifiedDate().compareTo(item1.getLastModifiedDate());
        });
        condition.setPageIndex(checkPageNum(condition, entities.size()));
        queryStreamJobsResult.setPageIndex(condition.getPageIndex());
        int start = (condition.getPageIndex() - 1) * condition.getPageSize();
        int end = condition.getPageIndex() * condition.getPageSize();
        if (start > entities.size()) {
            entities.clear();
        } else {
            entities = entities.subList(start, Math.min(end, entities.size()));
        }
        List<StreamJobRuntimeInfo> list = new ArrayList<>();
        addStreamJobRuntimeInfo(entities, list, idStatusMap);
        queryStreamJobsResult.setJobs(list);
        return queryStreamJobsResult;
    }

    private Specification<StreamJobEntity> queryStreamJobSpecification(StreamJobQueryCondi condition) {
        return (Root<StreamJobEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteria) -> {
            criteriaQuery.distinct(true);
            Predicate preAnd = criteria
                    .and(getStreamJobPredicates(root, condition, criteria).toArray(new Predicate[0]));
            criteriaQuery.where(preAnd);
            return preAnd;
        };
    }

    private List<Predicate> getStreamJobPredicates(Root<StreamJobEntity> root, StreamJobQueryCondi condition,
            CriteriaBuilder criteria) {
        List<Predicate> predicates = new ArrayList<>();
        if (!StringUtils.isEmpty(condition.getId())) {
            Predicate predicate = criteria.equal(root.get("id"), condition.getId());
            predicates.add(predicate);
        }
        if (!StringUtils.isEmpty(condition.getName())) {
            predicates.add(criteria.like(criteria.lower(root.get("name")),
                    "%" + CommonUtil.parameterEscape(condition.getName()).toLowerCase(Locale.ROOT) + "%"));
        }
        return predicates;
    }

    /**
     * 根据状态查询条件筛选作业
     *
     * @param condition condition
     * @param entities entities
     * @param idStatusMap idStatusMap
     * @return List<StreamJobEntity>
     */
    private List<StreamJobEntity> filterJobsByStatus(StreamJobQueryCondi condition, List<StreamJobEntity> entities,
            Map<String, FlinkBasicInfo> idStatusMap) {
        if (condition.getStatus() != null && StringUtils.isNotEmpty(condition.getStatus().name())) {
            return entities.stream().filter((StreamJobEntity item) -> {
                // 筛选停止状态
                if (idStatusMap.get(item.getFlinkId()) == null && condition.getStatus().equals(JobStatus.STOPPED)
                        && !ReliablityKey.SUBMITTED_STATE.equals(item.getState())) {
                    return true;
                }
                // 筛选异常停止状态
                if (idStatusMap.get(item.getFlinkId()) == null
                        && condition.getStatus().equals(JobStatus.EXCEPTION_STOPPED)
                        && ReliablityKey.SUBMITTED_STATE.equals(item.getState())) {
                    return true;
                }
                if (idStatusMap.get(item.getFlinkId()) == null) {
                    return false;
                }
                return condition.getStatus().equals(idStatusMap.get(item.getFlinkId()).getStatus());
            }).collect(Collectors.toList());
        }
        return entities;
    }

    /**
     * 新增作业
     *
     * @param entities 实体
     * @param list 作业集合
     * @param idStatusMap id状态集合
     */
    public void addStreamJobRuntimeInfo(List<StreamJobEntity> entities, List<StreamJobRuntimeInfo> list,
            Map<String, FlinkBasicInfo> idStatusMap) {
        for (StreamJobEntity entity : entities) {
            StreamJobRuntimeInfo jobRuntimeInfo = new StreamJobRuntimeInfo();
            BeanUtils.copyProperties(entity, jobRuntimeInfo);
            FlinkBasicInfo basicInfo = idStatusMap.get(entity.getFlinkId());
            if (basicInfo != null) {
                BeanUtils.copyProperties(basicInfo, jobRuntimeInfo);
            } else if (ReliablityKey.SUBMITTED_STATE.equals(entity.getState())) {
                jobRuntimeInfo.setStatus(JobStatus.EXCEPTION_STOPPED);
            } else {
                jobRuntimeInfo.setStatus(JobStatus.STOPPED);
            }
            list.add(jobRuntimeInfo);
        }
    }

    @Override
    public QueryDataStreamJobInfoResult getDataStreamJobInfo(String jobId) {
        StreamJobEntity job;
        Optional<StreamJobEntity> jobDb = jobRepository.findById(jobId);
        if (jobDb.isPresent()) {
            job = jobDb.get();
        } else {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_JOB_NOT_EXIST);
        }
        QueryDataStreamJobInfoResult jobInfoResult = new QueryDataStreamJobInfoResult();
        BeanUtils.copyProperties(job, jobInfoResult);
        jobInfoResult.setFlinkSql(CommonUtil.base64Decode(job.getFlinkSql()));
        return jobInfoResult;
    }

    @Override
    public JobStatusInfo queryJobStatusById(String id) {
        return flinkServiceUtils.queryJobStatusById(id);
    }

    @Override
    public ValidateNameResp checkJobName(ValidateNameReq validateNameReq) {
        ValidateNameResp validateNameResp = new ValidateNameResp();
        StreamJobEntity streamJobEntity = jobRepository.findStreamJobEntityByName(validateNameReq.getName());
        if (streamJobEntity == null || streamJobEntity.getId().equals(validateNameReq.getId())) {
            validateNameResp.setIsDuplicateName(false);
        }
        return validateNameResp;
    }

    @Override
    public QueryMaxJobNumResp queryMaxJobs() {
        return new QueryMaxJobNumResp(QuantitativeConfig.getMaxStreamJobs());
    }
}
