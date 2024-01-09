/*
 * 文 件 名:  ExportBatchJobHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/11
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.handler.exportdata;

import com.huawei.smartcampus.datatool.base.enumeration.DataToolResourceTypeEnum;
import com.huawei.smartcampus.datatool.base.enumeration.FileSuffixEnum;
import com.huawei.smartcampus.datatool.base.enumeration.ScheduleTypeEnum;
import com.huawei.smartcampus.datatool.base.handler.exportdata.base.ExportHandler;
import com.huawei.smartcampus.datatool.base.handler.exportdata.helper.ExportHelper;
import com.huawei.smartcampus.datatool.base.utils.CommonUtils;
import com.huawei.smartcampus.datatool.base.utils.FileOperateUtils;
import com.huawei.smartcampus.datatool.base.vo.job.Cron;
import com.huawei.smartcampus.datatool.base.vo.job.ExportJobContent;
import com.huawei.smartcampus.datatool.base.vo.job.JobNode;
import com.huawei.smartcampus.datatool.base.vo.job.JobNodeProperty;
import com.huawei.smartcampus.datatool.base.vo.job.JobSchedule;
import com.huawei.smartcampus.datatool.entity.DtBatchJobDependenceEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobNodeEntity;
import com.huawei.smartcampus.datatool.entity.DtSqlScriptNodeDetailEntity;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import org.springframework.beans.BeanUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 导出批处理作业处理器
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/11]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ExportBatchJobHandler implements ExportHandler {
    private ExportHelper exportHelper = SpringContextHelper.getBean(ExportHelper.class);
    @Override
    public void handleExportTask(List<String> ids, String srcPath) {
        for (DtBatchJobEntity jobEntity : exportHelper.findDtBatchJobEntitiesByIdIn(ids)) {
            ExportJobContent jobContent = new ExportJobContent();
            jobContent.setNodes(constructJobNodes(jobEntity));
            jobContent.setName(jobEntity.getName());
            jobContent.setSchedule(constructJobSchedule(jobEntity));
            String dir = exportHelper.getDirectory(jobEntity.getDirId(), DataToolResourceTypeEnum.JOB.type());
            jobContent.setDirectory(dir);
            String filePath = srcPath + dir + File.separator + jobEntity.getName() + FileSuffixEnum.JOB.suffix();
            // 将json写入name.job文件
            FileOperateUtils.writeJsonToFile(CommonUtils.transObjectToJsonString(jobContent), filePath);
        }
    }

    /**
     * 组装节点信息
     *
     * @param jobEntity 作业实体
     * @return 节点信息列表
     */
    private List<JobNode> constructJobNodes(DtBatchJobEntity jobEntity) {
        List<DtBatchJobNodeEntity> nodeEntities = exportHelper.findDtBatchJobNodeEntitiesByJobId(jobEntity.getId());
        List<JobNode> jobNodes = new ArrayList<>();
        for (DtBatchJobNodeEntity jobNodeEntity : nodeEntities) {
            JobNode jobNode = new JobNode();
            BeanUtils.copyProperties(jobNodeEntity, jobNode);
            List<JobNodeProperty> properties = new ArrayList<>();
            DtSqlScriptNodeDetailEntity detailEntity = exportHelper
                    .findDtSqlScriptNodeDetailEntityByJobNodeId(jobNodeEntity.getId());
            properties.add(new JobNodeProperty("scriptName", detailEntity.getScriptName()));
            properties.add(new JobNodeProperty("connectionName", detailEntity.getConnName()));
            properties.add(new JobNodeProperty("database", detailEntity.getDatabase()));
            properties.add(new JobNodeProperty("scriptArgs", detailEntity.getScriptArgs()));
            jobNode.setProperties(properties);
            jobNode.setAlarmThreshold(jobNodeEntity.getAlarmThreshold() / 60);
            jobNodes.add(jobNode);
        }
        return jobNodes;
    }

    /**
     * 组装调度信息
     *
     * @param jobEntity 作业实体
     * @return 调度信息
     */
    private JobSchedule constructJobSchedule(DtBatchJobEntity jobEntity) {
        JobSchedule jobSchedule = new JobSchedule();
        if (ScheduleTypeEnum.CRON.type().equals(jobEntity.getScheduleType())) {
            Cron cron = new Cron(jobEntity.getCronExpr(), jobEntity.getPeriodDay(), jobEntity.getPeriodEndTime(),
                    jobEntity.getPeriodInterval(), jobEntity.getPeriodStartTime(), jobEntity.getPeriod(),
                    jobEntity.getStartDate(), jobEntity.getEndDate());
            jobSchedule.setCron(cron);
            List<String> dependJobNames = exportHelper.findDtBatchJobDependenceEntitiesByJobName(jobEntity.getName())
                    .stream().map(DtBatchJobDependenceEntity::getDependJobName).collect(Collectors.toList());
            jobSchedule.setDependFailPolicy(jobEntity.getDependFailPolicy());
            jobSchedule.setDependJobNames(dependJobNames);
            jobSchedule.setDependSelf(jobEntity.getSelfDependence());
        }
        jobSchedule.setType(jobEntity.getScheduleType());
        return jobSchedule;
    }
}