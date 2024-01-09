/*
 * 文 件 名:  ImportBatchJobDgcHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.handler.importdata;

import com.huawei.smartcampus.datatool.base.enumeration.DetailStatusEnum;
import com.huawei.smartcampus.datatool.base.enumeration.FileSuffixEnum;
import com.huawei.smartcampus.datatool.base.enumeration.ResourceOriginEnum;
import com.huawei.smartcampus.datatool.base.enumeration.ScheduleTypeEnum;
import com.huawei.smartcampus.datatool.base.handler.importdata.base.AbstractImportHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.helper.ImportHelper;
import com.huawei.smartcampus.datatool.base.model.TaskModel;
import com.huawei.smartcampus.datatool.base.utils.CheckUtils;
import com.huawei.smartcampus.datatool.base.vo.job.ImportJobContent;
import com.huawei.smartcampus.datatool.base.vo.req.ReqIds;
import com.huawei.smartcampus.datatool.entity.DtBatchJobDependenceEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobNodeEntity;
import com.huawei.smartcampus.datatool.entity.DtSqlScriptNodeDetailEntity;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;

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
 * 该处理器负责导入来自dgc的批处理作业资产
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/10]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ImportBatchJobDgcHandler extends AbstractImportHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportBatchJobDgcHandler.class);

    @Autowired
    private ImportHelper importHelper;

    @Override
    public void handleImportTask(TaskModel taskModel) {
        if (!(ResourceOriginEnum.DGC.origin().equals(taskModel.getResourceOrigin())
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
            LOGGER.error("Import dgc batch-job fail.", e);
            errorHandle(taskModel, e);
        } catch (Exception e) {
            LOGGER.error("Import dgc batch-job fail.", e);
            errorHandle(taskModel, ExceptionCode.DATATOOL_SYSTEM_ERROR);
        }
    }

    private ImportJobContent convertBatchJobFormat(TaskModel taskModel) {
        ImportJobContent jobContent = new ImportJobContent();
        List<DtBatchJobDependenceEntity> jobDependenceEntities = new ArrayList<>();
        List<DtBatchJobNodeEntity> jobNodeEntityList = new ArrayList<>();
        try {
            JSONObject jsonObject = JSONObject.parseObject(taskModel.getFileContent());
            JSONArray nodes = jsonObject.getJSONArray("nodes");
            JSONObject schedule = jsonObject.getJSONObject("schedule");
            jobContent.setDir(jsonObject.getString("directory"));
            JSONObject cron = schedule.getJSONObject("cron");
            jobContent.setJobEntity(getDgcBatchJobEntity(schedule, jsonObject, cron));
            for (int i = 0; i < nodes.size(); i++) {
                DtBatchJobNodeEntity jobNodeEntity = new DtBatchJobNodeEntity();
                JSONObject node = nodes.getJSONObject(i);
                jobNodeEntity.setName(node.getString("name"));
                jobNodeEntity.setFailPolicy("fail");
                jobNodeEntity.setMaxExecutionTime(node.getInteger("maxExecutionTime") * 60);
                jobNodeEntity.setType("sqlScript");
                jobNodeEntity.setRetry(false);
                if (0 != node.getInteger("retryInterval") && 0 != node.getInteger("retryTimes")) {
                    jobNodeEntity.setRetry(true);
                    jobNodeEntity.setRetryInterval(node.getInteger("retryInterval"));
                    jobNodeEntity.setRetryTimes(node.getInteger("retryTimes"));
                }
                // 将dgc的脚本参数转为datatool的脚本参数
                jobContent.setScriptNodeDetailEntity(getScriptNodeDetailEntity(node.getJSONArray("properties")));
                // 告警默认3分钟
                jobNodeEntity.setAlarmThreshold(180);
                jobNodeEntityList.add(jobNodeEntity);
            }
            jobContent.setJobNodeEntities(jobNodeEntityList);
            if (cron != null) {
                JSONArray dependJobNames = cron.getJSONObject("dependJobs").getJSONArray("jobs");
                for (int i = 0; i < dependJobNames.size(); i++) {
                    DtBatchJobDependenceEntity dependenceEntity = new DtBatchJobDependenceEntity();
                    dependenceEntity.setDependJobName(dependJobNames.getString(i));
                    dependenceEntity.setJobName(jsonObject.getString("name"));
                    jobDependenceEntities.add(dependenceEntity);
                }
            }
            jobContent.setJobDependenceEntities(jobDependenceEntities);
        } catch (Exception e) {
            LOGGER.error("Convert datatool batch-job to object format fail.", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_FILE_CONTENT_CONVERT_FAIL);
        }
        return jobContent;
    }

    private DtBatchJobEntity getDgcBatchJobEntity(JSONObject schedule, JSONObject jsonObject, JSONObject cron) {
        DtBatchJobEntity jobEntity = new DtBatchJobEntity();
        jobEntity.setName(jsonObject.getString("name"));
        String scheduleType = schedule.getString("type").toLowerCase(Locale.ROOT);
        if (!ScheduleTypeEnum.getDgcScheduleTypeList().contains(scheduleType)) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SCHEDULE_TYPE_NOT_SUPPORT, scheduleType);
        }
        if (ScheduleTypeEnum.CRON.type().equalsIgnoreCase(scheduleType)) {
            jobEntity.setDependFailPolicy("suspend");
            jobEntity.setEndDate(cron.getDate("endTime"));
            jobEntity.setStartDate(cron.getDate("startTime"));
            jobEntity.setSelfDependence(cron.getBoolean("dependPrePeriod"));
            // cron表达式需要转换成datatool接收的格式
            importHelper.setCronExpressionAndPeriod(jobEntity, cron.getString("expression"),
                    cron.getString("intervalType"));
            jobEntity.setScheduleType(ScheduleTypeEnum.CRON.type());
        } else {
            jobEntity.setScheduleType(ScheduleTypeEnum.ONCE.type());
        }
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
                scriptNodeDetailEntity.setDatabase(value.startsWith("#{Env") && value.endsWith("}")
                        ? value.replace("#{Env.get(\"", "{{Env.get(").replace("\")}", ")}}")
                        : value);
            }
            if ("scriptArgs".equals(name)) {
                scriptNodeDetailEntity.setScriptArgs(importHelper.transScriptArgs(value));
            }
        }
        return scriptNodeDetailEntity;
    }
}