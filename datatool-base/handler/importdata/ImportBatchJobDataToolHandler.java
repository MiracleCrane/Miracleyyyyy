/*
 * 文 件 名:  ImportBatchJobDataToolHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.handler.importdata;

import com.huawei.smartcampus.datatool.base.enumeration.DependFailPolicyEnum;
import com.huawei.smartcampus.datatool.base.enumeration.DetailStatusEnum;
import com.huawei.smartcampus.datatool.base.enumeration.FileSuffixEnum;
import com.huawei.smartcampus.datatool.base.enumeration.PeriodEnum;
import com.huawei.smartcampus.datatool.base.enumeration.ResourceOriginEnum;
import com.huawei.smartcampus.datatool.base.enumeration.ScheduleTypeEnum;
import com.huawei.smartcampus.datatool.base.handler.importdata.base.AbstractImportHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.helper.ImportHelper;
import com.huawei.smartcampus.datatool.base.model.TaskModel;
import com.huawei.smartcampus.datatool.base.utils.CheckUtils;
import com.huawei.smartcampus.datatool.base.utils.JSONUtils;
import com.huawei.smartcampus.datatool.base.vo.job.ImportJobContent;
import com.huawei.smartcampus.datatool.base.vo.req.ReqIds;
import com.huawei.smartcampus.datatool.entity.DtBatchJobDependenceEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobNodeEntity;
import com.huawei.smartcampus.datatool.entity.DtSqlScriptNodeDetailEntity;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 该处理器负责导入来自datatool的批处理作业资产
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/10]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ImportBatchJobDataToolHandler extends AbstractImportHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportBatchJobDataToolHandler.class);

    @Autowired
    private ImportHelper importHelper;

    @Override
    public void handleImportTask(TaskModel taskModel) {
        if (!(ResourceOriginEnum.DATATOOL.origin().equals(taskModel.getResourceOrigin())
                && FileSuffixEnum.JOB.suffix().equals(taskModel.getFileSuffix()))) {
            return;
        }
        try {
            ImportJobContent jobContent = convertBatchJobFormat(taskModel);
            taskModel.setDetailName(jobContent.getJobEntity().getName());
            CheckUtils.checkJobParams(jobContent);
            DtBatchJobEntity jobEntity = importHelper.findDtBatchJobEntityByName(jobContent.getJobEntity().getName());
            // 如果原来就不存在，就新增
            if (jobEntity == null) {
                // 创建目录
                jobContent.getJobEntity().setDirId(importHelper.createJobDir(jobContent.getDir()));
                importHelper.saveJobContent(jobContent);
            }
            // 如果存在，覆盖更新
            if (jobEntity != null && !taskModel.isSkip()) {
                // 更新目录
                jobContent.getJobEntity().setDirId(importHelper.createJobDir(jobContent.getDir()));
                importHelper.updateJobContent(jobContent, jobEntity);
                if (jobEntity.getState()) {
                    // 然后再重启，使更新生效
                    importHelper.requestBatchJobStart(new ReqIds(Arrays.asList(jobEntity.getId())));
                }
            }
            // 跳过，就写条数据到detail表中
            if (jobEntity != null && taskModel.isSkip()) {
                handle(taskModel, DetailStatusEnum.SKIPPED.status());
                return;
            }
            handle(taskModel, DetailStatusEnum.SUCCESS.status());
        } catch (DataToolRuntimeException e) {
            LOGGER.error("Import datatool batch-job fail.", e);
            errorHandle(taskModel, e);
        } catch (Exception e) {
            LOGGER.error("Import datatool batch-job fail.", e);
            errorHandle(taskModel, ExceptionCode.DATATOOL_SYSTEM_ERROR);
        }
    }

    private ImportJobContent convertBatchJobFormat(TaskModel taskModel) {
        ImportJobContent jobContent = new ImportJobContent();
        List<DtBatchJobNodeEntity> jobNodeEntityList = new ArrayList<>();
        try {
            JSONObject jsonObject = JSONObject.parseObject(taskModel.getFileContent());
            JSONArray nodes = JSONUtils.getJSONArrayOrErrorHandler(jsonObject, "nodes");
            JSONObject schedule = JSONUtils.getJSONObjectOrErrorHandler(jsonObject, "schedule");
            String scheduleType = JSONUtils.getStringOrErrorHandler(schedule, "type").toLowerCase(Locale.ROOT);
            jobContent.setDir(JSONUtils.getStringOrErrorHandler(jsonObject, "directory"));
            jobContent.setJobEntity(getBatchJobEntity(schedule, jsonObject, scheduleType));
            for (int i = 0; i < nodes.size(); i++) {
                DtBatchJobNodeEntity jobNodeEntity = new DtBatchJobNodeEntity();
                JSONObject node = nodes.getJSONObject(i);
                jobContent.setScriptNodeDetailEntity(
                        getScriptNodeDetailEntity(JSONUtils.getJSONArrayOrErrorHandler(node, "properties")));
                jobNodeEntity
                        .setFailPolicy(JSONUtils.getStringOrErrorHandler(node, "failPolicy").toLowerCase(Locale.ROOT));
                jobNodeEntity.setMaxExecutionTime(JSONUtils.getIntegerOrErrorHandler(node, "maxExecutionTime"));
                jobNodeEntity.setType("sqlScript");
                jobNodeEntity.setRetry(false);
                Integer retryInterval = JSONUtils.getIntegerOrDefaultZero(node, "retryInterval");
                Integer retryTimes = JSONUtils.getIntegerOrDefaultZero(node, "retryTimes");
                if ((node.getBoolean("retry") != null && node.getBoolean("retry"))
                        || (0 != retryInterval && 0 != retryTimes)) {
                    jobNodeEntity.setRetry(true);
                    jobNodeEntity.setRetryInterval(retryInterval);
                    jobNodeEntity.setRetryTimes(retryTimes);
                }
                jobNodeEntity.setName(JSONUtils.getStringOrErrorHandler(node, "name"));
                jobNodeEntity.setAlarmThreshold(
                        node.getInteger("alarmThreshold") != null ? node.getInteger("alarmThreshold") * 60 : 180);
                jobNodeEntityList.add(jobNodeEntity);
            }
            jobContent.setJobNodeEntities(jobNodeEntityList);
            jobContent.setJobDependenceEntities(
                    getDependenceEntity(schedule, jsonObject.getString("name"), scheduleType));
        } catch (DataToolRuntimeException e) {
            LOGGER.error("Convert datatool batch-job to object format fail.", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Convert datatool batch-job to object format fail.", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_FILE_CONTENT_CONVERT_FAIL);
        }
        return jobContent;
    }

    private DtBatchJobEntity getBatchJobEntity(JSONObject schedule, JSONObject jsonObject, String scheduleType) {
        DtBatchJobEntity jobEntity = new DtBatchJobEntity();
        jobEntity.setName(JSONUtils.getStringOrErrorHandler(jsonObject, "name"));
        if (ScheduleTypeEnum.CRON.type().equals(scheduleType)) {
            JSONObject cron = JSONUtils.getJSONObjectOrErrorHandler(schedule, "cron");
            jobEntity.setEndDate(JSONUtils.getDate(cron, "endTime"));
            jobEntity.setStartDate(JSONUtils.getDateOrErrorHandler(cron, "startTime"));
            String schedulePeriod = JSONUtils.getStringOrErrorHandler(cron, "schedule_period");
            jobEntity.setPeriod(schedulePeriod);
            if (PeriodEnum.MINUTE.type().equals(schedulePeriod) || PeriodEnum.HOUR.type().equals(schedulePeriod)) {
                jobEntity.setPeriodEndTime(JSONUtils.getStringOrDefaultEmpty(cron, "period_endtime"));
                jobEntity.setPeriodInterval(JSONUtils.getIntegerOrErrorHandler(cron, "period_interval"));
            }
            if (PeriodEnum.MONTH.type().equals(schedulePeriod) || PeriodEnum.WEEK.type().equals(schedulePeriod)) {
                jobEntity.setPeriodDay(JSONUtils.getStringOrDefaultEmpty(cron, "period_day"));
            }
            jobEntity.setPeriodStartTime(JSONUtils.getStringOrErrorHandler(cron, "period_starttime"));
            jobEntity.setCronExpr(JSONUtils.getStringOrErrorHandler(cron, "expression"));
            jobEntity.setDependFailPolicy(StringUtils.isEmpty(schedule.getString("dependFailPolicy"))
                    ? DependFailPolicyEnum.SUSPEND.type()
                    : schedule.getString("dependFailPolicy").toLowerCase(Locale.ROOT));
            jobEntity.setSelfDependence(JSONUtils.getBooleanOrErrorHandler(schedule, "dependSelf"));
        }
        jobEntity.setScheduleType(scheduleType);
        return jobEntity;
    }

    private DtSqlScriptNodeDetailEntity getScriptNodeDetailEntity(JSONArray properties) {
        DtSqlScriptNodeDetailEntity scriptNodeDetailEntity = new DtSqlScriptNodeDetailEntity();
        for (int j = 0; j < properties.size(); j++) {
            String name = properties.getJSONObject(j).getString("name");
            String value = properties.getJSONObject(j).getString("value");
            if ("scriptName".equals(name)) {
                scriptNodeDetailEntity.setScriptName(value);
            }
            if ("connectionName".equals(name)) {
                scriptNodeDetailEntity.setConnName(value);
            }
            if ("database".equals(name)) {
                scriptNodeDetailEntity.setDatabase(value);
            }
            if ("scriptArgs".equals(name)) {
                scriptNodeDetailEntity.setScriptArgs(value);
            }
        }
        return scriptNodeDetailEntity;
    }

    private List<DtBatchJobDependenceEntity> getDependenceEntity(JSONObject schedule, String jobName,
            String scheduleType) {
        List<DtBatchJobDependenceEntity> jobDependenceEntities = new ArrayList<>();
        if (ScheduleTypeEnum.CRON.type().equals(scheduleType)) {
            JSONArray dependJobNames = schedule.getJSONArray("dependJobNames");
            for (int i = 0; i < dependJobNames.size(); i++) {
                String dependJobName = "";
                Object object = dependJobNames.get(i);
                if (object instanceof JSONObject) {
                    dependJobName = dependJobNames.getJSONObject(i).getString("jobName");
                }
                if (object instanceof String) {
                    dependJobName = String.valueOf(object);
                }
                DtBatchJobDependenceEntity dependenceEntity = new DtBatchJobDependenceEntity();
                dependenceEntity.setDependJobName(dependJobName);
                dependenceEntity.setJobName(jobName);
                jobDependenceEntities.add(dependenceEntity);
            }
        }
        return jobDependenceEntities;
    }
}