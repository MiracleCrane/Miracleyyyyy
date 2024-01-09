/*
 * 文 件 名:  ImportHelper.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/13
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.handler.importdata.helper;

import com.huawei.smartcampus.datatool.base.enumeration.DetailStatusEnum;
import com.huawei.smartcampus.datatool.base.enumeration.HistoryStatusEnum;
import com.huawei.smartcampus.datatool.base.enumeration.PeriodEnum;
import com.huawei.smartcampus.datatool.base.enumeration.StreamStateEnum;
import com.huawei.smartcampus.datatool.base.model.TaskModel;
import com.huawei.smartcampus.datatool.base.pattern.ImportExportPattern;
import com.huawei.smartcampus.datatool.base.properties.ImportExportConfig;
import com.huawei.smartcampus.datatool.base.utils.CommonUtils;
import com.huawei.smartcampus.datatool.base.vo.DetailNumber;
import com.huawei.smartcampus.datatool.base.vo.dlivar.DliVar;
import com.huawei.smartcampus.datatool.base.vo.job.ImportJobContent;
import com.huawei.smartcampus.datatool.base.vo.req.ImportReq;
import com.huawei.smartcampus.datatool.base.vo.req.ReqIds;
import com.huawei.smartcampus.datatool.entity.DtBatchJobDependenceEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobDirEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobNodeEntity;
import com.huawei.smartcampus.datatool.entity.DtCipherVariableEntity;
import com.huawei.smartcampus.datatool.entity.DtConnectionEntity;
import com.huawei.smartcampus.datatool.entity.DtEnvironmentVariableEntity;
import com.huawei.smartcampus.datatool.entity.DtScriptDirEntity;
import com.huawei.smartcampus.datatool.entity.DtScriptEntity;
import com.huawei.smartcampus.datatool.entity.ImportDetailEntity;
import com.huawei.smartcampus.datatool.entity.ImportHistoryEntity;
import com.huawei.smartcampus.datatool.entity.StreamJobEntity;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
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
import com.huawei.smartcampus.datatool.repository.ImportDetailRepository;
import com.huawei.smartcampus.datatool.repository.ImportHistoryRepository;
import com.huawei.smartcampus.datatool.repository.StreamJobRepository;
import com.huawei.smartcampus.datatool.utils.CommonUtil;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;
import com.huawei.smartcampus.datatool.utils.StringUtils;
import com.huawei.smartcampus.datatool.utils.http.HttpRequestSender;
import com.huawei.smartcampus.datatool.vo.flink.StartStreamJobsReq;
import com.huawei.smartcampus.datatool.vo.flink.StopStreamJobsReq;

import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * 导入帮助类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/13]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportHelper.class);
    private ImportHistoryRepository historyRepository = SpringContextHelper.getBean(ImportHistoryRepository.class);
    private ImportDetailRepository detailRepository = SpringContextHelper.getBean(ImportDetailRepository.class);
    private StreamJobRepository streamJobRepository = SpringContextHelper.getBean(StreamJobRepository.class);
    private DtEnvironmentVariableRepository dtEnvironmentVariableRepository = SpringContextHelper
            .getBean(DtEnvironmentVariableRepository.class);
    private DtCipherVariableRepository dtCipherVariableRepository = SpringContextHelper
            .getBean(DtCipherVariableRepository.class);
    private DtConnectionRepository dtConnectionRepository = SpringContextHelper.getBean(DtConnectionRepository.class);
    private DtScriptDirRepository scriptDirRepository = SpringContextHelper.getBean(DtScriptDirRepository.class);
    private DtBatchJobDirRepository jobDirRepository = SpringContextHelper.getBean(DtBatchJobDirRepository.class);
    private DtScriptRepository scriptRepository = SpringContextHelper.getBean(DtScriptRepository.class);
    private DtBatchJobRepository jobRepository = SpringContextHelper.getBean(DtBatchJobRepository.class);
    private DtBatchJobNodeRepository jobNodeRepository = SpringContextHelper.getBean(DtBatchJobNodeRepository.class);
    private DtBatchJobDependenceRepository jobDependenceRepository = SpringContextHelper
            .getBean(DtBatchJobDependenceRepository.class);
    private DtSqlScriptNodeDetailRepository scriptNodeDetailRepository = SpringContextHelper
            .getBean(DtSqlScriptNodeDetailRepository.class);

    /**
     * 存储到history表
     *
     * @param importReq 导入请求参数
     * @param total 总数
     * @return 历史表主键id
     */
    @Transactional
    public String saveImportHistoryEntity(ImportReq importReq, int total) {
        ImportHistoryEntity importHistoryEntity = new ImportHistoryEntity();
        importHistoryEntity.setResourceName(importReq.getResource().getOriginalFilename());
        importHistoryEntity.setResourceOrigin(importReq.getResourceOrigin());
        importHistoryEntity.setResourceType(importReq.getResourceType());
        importHistoryEntity.setImportMode(CommonUtils.importMode(importReq));
        importHistoryEntity.setStatus(HistoryStatusEnum.RUNNING.status());
        importHistoryEntity.setTotal(total);
        ImportHistoryEntity entity = historyRepository.save(importHistoryEntity);
        return entity.getId();
    }

    /**
     * 存储到history表，根据total和id更新history
     *
     * @param id 导入请求参数
     * @param total 总数
     */
    @Transactional
    public void updateImportHistoryEntity(String id, int total) {
        ImportHistoryEntity importHistoryEntity = findImportHistoryEntityById(id);
        importHistoryEntity.setTotal(total);
        historyRepository.save(importHistoryEntity);
    }

    /**
     * 根据id，查询历史导入记录
     *
     * @param id 历史id
     * @return 历史导入记录实体
     */
    private ImportHistoryEntity findImportHistoryEntityById(String id) {
        return historyRepository.findImportHistoryEntityById(id);
    }

    /**
     * 根据名称，查询流处理作业实体
     *
     * @param name 作业名称
     * @return 流处理作业实体
     */
    public StreamJobEntity findStreamJobEntityByName(String name) {
        return streamJobRepository.findStreamJobEntityByName(name);
    }

    /**
     * 根据key，查询环境变量实体
     *
     * @param key key
     * @return 环境变量实体
     */
    public DtEnvironmentVariableEntity findEnvironmentVariableEntityByKey(String key) {
        return dtEnvironmentVariableRepository.findDtEnvironmentVariableEntityByKey(key);
    }

    /**
     * 根据key，查询密码箱实体
     *
     * @param key key
     * @return 密码箱实体
     */
    public DtCipherVariableEntity findCipherVariableEntityByKey(String key) {
        return dtCipherVariableRepository.findCipherVariableEntityByKey(key);
    }

    /**
     * 根据数据连接名称，查询数据连接实体
     *
     * @param name 连接名
     * @return 数据连接实体
     */
    public DtConnectionEntity findDtConnectionEntityByName(String name) {
        return dtConnectionRepository.findDtConnectionEntityByName(name);
    }

    /**
     * 根据name，查询脚本实体
     *
     * @param name 名称
     * @return 脚本实体
     */
    public DtScriptEntity findDtScriptEntityByName(String name) {
        return scriptRepository.findDtScriptEntityByName(name);
    }

    /**
     * 根据name，查询作业实体
     *
     * @param name 名称
     * @return 作业实体
     */
    public DtBatchJobEntity findDtBatchJobEntityByName(String name) {
        return jobRepository.findDtBatchJobEntityByName(name);
    }

    /**
     * 数据连接保存
     *
     * @param dtConnectionEntity 数据连接实体
     */
    @Transactional
    public void saveConnectionEntity(DtConnectionEntity dtConnectionEntity) {
        dtConnectionRepository.save(dtConnectionEntity);
    }

    /**
     * 保存或者覆盖更新密码箱实体
     *
     * @param entity 原始实体
     * @param isSkip 是否跳过
     * @param data 参数数据
     * @param <T> 参数类型为泛型
     */
    @Transactional
    public <T> void updateOrSaveCipherVariableEntity(T data, DtCipherVariableEntity entity, boolean isSkip) {
        DtCipherVariableEntity updateOrSaveEntity = new DtCipherVariableEntity();
        if (data instanceof DliVar) {
            updateOrSaveEntity.setKey(((DliVar) data).getName());
        }
        if (data instanceof DtCipherVariableEntity) {
            updateOrSaveEntity.setKey(((DtCipherVariableEntity) data).getKey());
        }
        // 如果原来就不存在，就新增
        if (entity == null) {
            updateOrSaveEntity.setValue("");
            dtCipherVariableRepository.save(updateOrSaveEntity);
        }
        // 覆盖更新
        if (entity != null && !isSkip) {
            updateOrSaveEntity.setId(entity.getId());
            updateOrSaveEntity.setValue(entity.getValue());
            updateOrSaveEntity.setCreatedBy(entity.getCreatedBy());
            dtCipherVariableRepository.save(updateOrSaveEntity);
        }
    }

    /**
     * 存储到detail表
     *
     * @param taskModel 任务模型
     * @param status 单条导入状态
     */
    @Transactional
    public void saveImportDetailEntity(TaskModel taskModel, String status) {
        ImportDetailEntity importDetailEntity = new ImportDetailEntity();
        importDetailEntity.setImportHistoryId(taskModel.getTaskId());
        importDetailEntity.setFileName(CommonUtils
                .getNameWithinRange(StringUtils.isEmpty(taskModel.getDetailName()) && taskModel.isSingleFile()
                        ? taskModel.getFileName()
                        : taskModel.getDetailName()));
        if (taskModel.getErrorCode() != null) {
            importDetailEntity.setErrorCode(taskModel.getErrorCode());
        }
        if (taskModel.getParams() != null) {
            importDetailEntity.setParams(taskModel.getParams());
        }
        importDetailEntity.setStatus(status);
        detailRepository.save(importDetailEntity);
    }

    /**
     * 保存流处理作业
     *
     * @param streamJobEntity 流处理作业文件内容
     */
    @Transactional
    public void saveStreamJobEntity(StreamJobEntity streamJobEntity) {
        streamJobEntity.setState(StreamStateEnum.UNSUBMITTED.name());
        streamJobEntity.setFlinkSql(CommonUtil.base64EncodeString(streamJobEntity.getFlinkSql()));
        streamJobRepository.save(streamJobEntity);
    }

    /**
     * 更新流处理作业
     *
     * @param streamJobEntity 流处理作业文件内容
     * @param entity 历史实体的id
     */
    @Transactional
    public void updateStreamJobEntityByOldEntity(StreamJobEntity streamJobEntity, StreamJobEntity entity) {
        streamJobEntity.setId(entity.getId());
        streamJobEntity.setState(StreamStateEnum.UNSUBMITTED.name());
        streamJobEntity.setFlinkSql(CommonUtil.base64EncodeString(streamJobEntity.getFlinkSql()));
        streamJobEntity.setCreatedBy(entity.getCreatedBy());
        streamJobRepository.save(streamJobEntity);
    }

    /**
     * 调用stream的停止接口
     *
     * @param stopStreamJobsReq 请求停止接口入参
     */
    private void requestStreamJobStop(StopStreamJobsReq stopStreamJobsReq) {
        JSONObject response;
        try {
            response = HttpRequestSender.doPostWithLocale(ImportExportConfig.streamStopUrl(), stopStreamJobsReq);
        } catch (Exception e) {
            LOGGER.error("request StreamJob Stop failed: ", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_ERROR);
        }
        if (response.getJSONObject("result").getInteger("failure") == 1) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_STOP_STREAM_JOB_FAIL, response);
        }
    }

    /**
     * 调用stream的启动接口，如果启动失败，则恢复历史entity
     *
     * @param startStreamJobsReq 请求启动接口入参
     */
    private void requestStreamJobStart(StartStreamJobsReq startStreamJobsReq) {
        JSONObject response;
        try {
            response = HttpRequestSender.doPostWithLocale(ImportExportConfig.streamStartUrl(), startStreamJobsReq);
        } catch (Exception e) {
            LOGGER.error("request StreamJob Start failed: ", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_ERROR);
        }
        if (response.getJSONObject("result").getInteger("failure") == 1) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_STOP_STREAM_JOB_FAIL, response);
        }
    }

    /**
     * 通过调接口，更新流处理作业实体
     *
     * @param entity 数据库查到的历史实体
     * @param streamJobEntity 文件中新内容实体
     * @throws InterruptedException 中断异常
     */
    public void updateStreamJobEntity(StreamJobEntity entity, StreamJobEntity streamJobEntity)
            throws InterruptedException {
        // 如果不是跳过，就更新，更新时需要判断是否是启动中，如果是SUBMITTED启动中，先停止
        if (StreamStateEnum.SUBMITTED.name().equals(entity.getState())) {
            // 停止接口调用
            requestStreamJobStop(new StopStreamJobsReq(Collections.singletonList(entity.getId()), false));
            // 启用线程去查询停止后，数据库的status字段是否变为UNSUBMITTED
            Thread queryThread = new Thread(() -> {
                int num = 0;
                try {
                    while (num < 5) {
                        StreamJobEntity tem = streamJobRepository.findStreamJobEntityByName(entity.getName());
                        if (StreamStateEnum.UNSUBMITTED.name().equals(tem.getState())) {
                            break;
                        } else {
                            num++;
                            Thread.sleep(5000);
                        }
                    }
                } catch (InterruptedException exception) {
                    LOGGER.error("waiting thread is too long: ", exception);
                }
            });
            queryThread.setUncaughtExceptionHandler((t, e) -> {
                LOGGER.error("Thread name: {}, exception: ", t.getName(), e);
            });
            queryThread.start();
            queryThread.join();
            // 更新
            updateStreamJobEntityByOldEntity(streamJobEntity, entity);
            // 然后再重启，使更新生效
            requestStreamJobStart(new StartStreamJobsReq(Collections.singletonList(entity.getId()), false));
        } else {
            // 如果不是SUBMITTED，直接更新
            updateStreamJobEntityByOldEntity(streamJobEntity, entity);
        }
    }

    /**
     * 保存环境变量到数据库
     *
     * @param dtEnvironmentVariableEntity 环境变量实体
     */
    @Transactional
    public void saveDtEnvironmentVariableEntity(DtEnvironmentVariableEntity dtEnvironmentVariableEntity) {
        dtEnvironmentVariableRepository.save(dtEnvironmentVariableEntity);
    }

    /**
     * 覆盖更新环境变量
     *
     * @param entity 传入历史环境变量实体
     * @param isSkip 重名策略
     * @param data 参数数据
     * @param <T> 参数类型为泛型
     */
    @Transactional
    public <T> void updateEnvWithOverWrite(DtEnvironmentVariableEntity entity, boolean isSkip, T data) {
        if (entity != null && !isSkip) {
            if (data instanceof DtEnvironmentVariableEntity) {
                DtEnvironmentVariableEntity temp = (DtEnvironmentVariableEntity) data;
                temp.setId(entity.getId());
                temp.setCreatedBy(entity.getCreatedBy());
                dtEnvironmentVariableRepository.save(temp);
            }
            if (data instanceof DliVar) {
                DliVar temp = (DliVar) data;
                DtEnvironmentVariableEntity dtEnvironmentVariableEntity = new DtEnvironmentVariableEntity();
                dtEnvironmentVariableEntity.setId(entity.getId());
                dtEnvironmentVariableEntity.setKey(temp.getName());
                dtEnvironmentVariableEntity.setValue(temp.getValue());
                dtEnvironmentVariableEntity.setCreatedBy(entity.getCreatedBy());
                dtEnvironmentVariableRepository.save(dtEnvironmentVariableEntity);
            }
        }
    }

    public String createScriptDir(String dir) {
        String currentParentId = "-1";
        if (StringUtils.isNotEmpty(dir) && !File.separator.equals(dir)) {
            String[] dirs = dir.substring(1).split(File.separatorChar == '\\' ? "\\\\" : File.separator);
            for (int i = 0; i < dirs.length; i++) {
                String dirName = dirs[i];
                currentParentId = i == 0 ? "-1" : currentParentId;
                DtScriptDirEntity scriptDirEntity = scriptDirRepository.findDtScriptDirEntityByNameAndParentId(dirName,
                        currentParentId);
                if (scriptDirEntity == null) {
                    DtScriptDirEntity temp = new DtScriptDirEntity();
                    temp.setName(dirName);
                    temp.setParentId(currentParentId);
                    currentParentId = scriptDirRepository.save(temp).getId();
                } else {
                    currentParentId = scriptDirEntity.getId();
                }
            }
        }
        return currentParentId;
    }

    public String createJobDir(String dir) {
        String currentParentId = "-1";
        if (StringUtils.isNotEmpty(dir) && !File.separator.equals(dir)) {
            String[] dirs = dir.substring(1).split(File.separatorChar == '\\' ? "\\\\" : File.separator);
            for (int i = 0; i < dirs.length; i++) {
                String dirName = dirs[i];
                currentParentId = i == 0 ? "-1" : currentParentId;
                DtBatchJobDirEntity batchJobDirEntity = jobDirRepository
                        .findDtBatchJobDirEntityByNameAndParentId(dirName, currentParentId);
                if (batchJobDirEntity == null) {
                    DtBatchJobDirEntity temp = new DtBatchJobDirEntity();
                    temp.setName(dirName);
                    temp.setParentId(currentParentId);
                    currentParentId = jobDirRepository.save(temp).getId();
                } else {
                    currentParentId = batchJobDirEntity.getId();
                }
            }
        }
        return currentParentId;
    }

    @Transactional
    public void confirm(TaskModel taskModel) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("The total number of tasks is {}, and the current is the {} task", taskModel.getTotal(),
                    taskModel.getNumber());
        }
        ImportHistoryEntity importHistoryEntity = findImportHistoryEntityById(taskModel.getTaskId());
        // 如果当前是最后一个，确认整体结果
        if (taskModel.getTotal() == taskModel.getNumber()) {
            DetailNumber detailNumber = detailNumber(taskModel.getTaskId());
            if (taskModel.getTotal() == detailNumber.getSuccess() + detailNumber.getIgnored()
                    + detailNumber.getFailed()) {
                // 如果有成功，有失败，则部分成功
                if (detailNumber.getFailed() > 0 && (detailNumber.getSuccess() > 0 || detailNumber.getIgnored() > 0)) {
                    importHistoryEntity.setStatus(HistoryStatusEnum.PARTIAL_FAILURE.status());
                }
                // 如果只有成功和跳过，则置为成功
                if (taskModel.getTotal() == detailNumber.getSuccess() + detailNumber.getIgnored()) {
                    importHistoryEntity.setStatus(HistoryStatusEnum.SUCCESS.status());
                }
                // 如果只有失败，则全失败
                if (taskModel.getTotal() == detailNumber.getFailed()) {
                    importHistoryEntity.setStatus(HistoryStatusEnum.FAILURE.status());
                }
            }
        } else {
            // 获取当前时间与数据库中的最后修改时间，如果大于5min，则置为异常中断
            int lastModifiedTime = (int) (importHistoryEntity.getLastModifiedDate().getTime() / (60 * 1000));
            int currentTime = (int) (System.currentTimeMillis() / (60 * 1000));
            if (Math.abs(currentTime - lastModifiedTime) > 5) {
                importHistoryEntity.setStatus(HistoryStatusEnum.BREAK.status());
            }
        }
        historyRepository.save(importHistoryEntity);
    }

    /**
     * 获取detail表中成功、失败、跳过数量
     *
     * @param historyId 历史id
     * @return 数量对象
     */
    public DetailNumber detailNumber(String historyId) {
        List<ImportDetailEntity> detailEntities = findAllByImportHistoryId(historyId);
        return new DetailNumber(
                detailEntities.stream().filter(o1 -> o1.getStatus().equals(DetailStatusEnum.SUCCESS.status())).count(),
                detailEntities.stream().filter(o1 -> o1.getStatus().equals(DetailStatusEnum.FAILURE.status())).count(),
                detailEntities.stream().filter(o1 -> o1.getStatus().equals(DetailStatusEnum.SKIPPED.status())).count());
    }

    /**
     * 根据历史id，获取详情实体
     *
     * @param historyId 历史id
     * @return 详情实体列表
     */
    public List<ImportDetailEntity> findAllByImportHistoryId(String historyId) {
        return detailRepository.findAllByImportHistoryId(historyId);
    }

    /**
     * 解析脚本中的脚本参数
     *
     * @param content 脚本内容
     * @return 脚本参数
     */
    public String parseScriptContent(String content) {
        if (StringUtils.isEmpty(content)) {
            return "";
        }
        Map<String, String> scriptArgs = new HashMap<>();
        Matcher matcher = ImportExportPattern.SCRIPT_ARGS_PATTERN.matcher(content);
        while (matcher.find()) {
            scriptArgs.put(matcher.group().replaceAll("\\$|\\{|\\}", ""), "");
        }
        return JSONObject.toJSONString(scriptArgs);
    }

    /**
     * 根据cron表达式，获取period信息
     *
     * @param jobEntity 作业实体
     * @param cronExpression cron表达式
     * @param period 周期
     */
    public void setCronExpressionAndPeriod(DtBatchJobEntity jobEntity, String cronExpression, String period) {
        String[] cronExp = cronExpression.split(" ");
        String minute = cronExp[1];
        String hour = cronExp[2];
        String day = cronExp[3];
        String week = cronExp[5];
        if (period == null) {
            period = getCronPeriod(minute, hour, day, week);
        }
        jobEntity.setCronExpr(cronExpression.substring(cronExpression.indexOf(" ")).replace("?", "*"));
        switch (period) {
            case "days":
                jobEntity.setPeriod(PeriodEnum.DAY.type());
                jobEntity.setPeriodStartTime(leftAddZero(hour) + ":" + leftAddZero(minute));
                jobEntity.setPeriodInterval(1);
                break;
            case "hours":
                jobEntity.setPeriod(PeriodEnum.HOUR.type());
                jobEntity.setPeriodStartTime(leftAddZero(hour.split("-")[0]) + ":" + leftAddZero(minute));
                jobEntity.setPeriodEndTime(leftAddZero(hour.split("-")[1].split("/")[0]) + ":59");
                jobEntity.setPeriodInterval(Integer.parseInt(hour.split("-")[1].split("/")[1]));
                break;
            case "minutes":
                jobEntity.setPeriod(PeriodEnum.MINUTE.type());
                jobEntity.setPeriodStartTime(leftAddZero(hour.split("-")[0]) + ":00");
                jobEntity.setPeriodEndTime(leftAddZero(hour.split("-")[1]) + ":59");
                jobEntity.setPeriodInterval(Integer.parseInt(minute.split("/")[1]));
                break;
            case "months":
                jobEntity.setPeriod(PeriodEnum.MONTH.type());
                jobEntity.setPeriodStartTime(leftAddZero(hour) + ":" + leftAddZero(minute));
                jobEntity.setPeriodDay(day);
                jobEntity.setPeriodInterval(1);
                break;
            case "weeks":
                jobEntity.setPeriod(PeriodEnum.WEEK.type());
                jobEntity.setPeriodStartTime(leftAddZero(hour) + ":" + leftAddZero(minute));
                jobEntity.setPeriodDay(week);
                jobEntity.setPeriodInterval(1);
                break;
            default:
                LOGGER.error("Unknown period.");
        }
    }

    /**
     * 左侧自动补零
     *
     * @param str 字符串
     * @return 补0后字符串
     */
    private String leftAddZero(String str) {
        return String.format("%02d", Integer.parseInt(str));
    }

    /**
     * 根据cron表达式获取周期是按分钟、小时、天、月、周
     *
     * @param minute 分钟
     * @param hour 小时
     * @param day 天
     * @param week 周
     * @return 周期
     */
    private String getCronPeriod(String minute, String hour, String day, String week) {
        String period = "";
        if (!week.equals("?")) {
            period = "weeks";
        } else if (!(day.equals("*") || day.equals("?"))) {
            period = "months";
        } else if (!minute.contains("/") && !hour.contains("/")) {
            period = "days";
        } else if (hour.contains("/")) {
            period = "hours";
        } else if (minute.contains("/")) {
            period = "minutes";
        }
        return period;
    }

    /**
     * 转换dgc脚本参数
     *
     * @param value 传入的参数信息
     * @return 转换后的参数
     */
    public String transScriptArgs(String value) {
        Map<String, String> result = new HashMap<>();
        String[] keyValues = value.split("\n");
        for (String keyValue : keyValues) {
            if (keyValue.contains("=")) {
                String[] param = keyValue.split("=");
                String paramName = param[0];
                String paramValue = param[1];
                if ("#{Job.name}".equals(paramValue)) {
                    result.put(paramName, "{{Sys.get(batch_job_name)}}");
                } else if ("#{Job.planTime}".equals(paramValue)
                        || paramValue.contains("#{DateUtil.format(Job.planTime")) {
                    result.put(paramName, "{{Sys.get(batch_job_plan_time)}}");
                } else {
                    result.put(paramName, paramValue);
                }
            }
        }
        return JSONObject.toJSONString(result);
    }

    /**
     * 保存作业以及依赖信息
     *
     * @param jobContent 作业以及依赖信息
     */
    @Transactional
    public void saveJobContent(ImportJobContent jobContent) {
        for (DtBatchJobNodeEntity jobNodeEntity : jobContent.getJobNodeEntities()) {
            jobContent.getJobEntity().setState(false);
            jobNodeEntity.setJobId(jobRepository.save(jobContent.getJobEntity()).getId());
            DtBatchJobNodeEntity batchJobNodeEntity = jobNodeRepository.save(jobNodeEntity);
            jobContent.getScriptNodeDetailEntity().setJobNodeId(batchJobNodeEntity.getId());
            scriptNodeDetailRepository.save(jobContent.getScriptNodeDetailEntity());
        }
        for (DtBatchJobDependenceEntity dtBatchJobDependenceEntity : jobContent.getJobDependenceEntities()) {
            jobDependenceRepository.save(dtBatchJobDependenceEntity);
        }
    }

    /**
     * 更新作业及依赖信息
     *
     * @param jobContent 作业以及依赖信息
     * @param jobEntity 作业实体
     */
    @Transactional
    public void updateJobContent(ImportJobContent jobContent, DtBatchJobEntity jobEntity) {
        // 更新
        jobContent.getJobEntity().setId(jobEntity.getId());
        jobContent.getJobEntity().setState(jobEntity.getState());
        jobContent.getJobEntity().setCreatedBy(jobEntity.getCreatedBy());
        jobRepository.save(jobContent.getJobEntity());
        for (DtBatchJobNodeEntity jobNodeEntity : jobContent.getJobNodeEntities()) {
            String jobNodeId = jobNodeRepository.findDtBatchJobNodeEntitiesByJobId(jobEntity.getId()).get(0).getId();
            jobNodeEntity.setId(jobNodeId);
            jobNodeEntity.setJobId(jobEntity.getId());
            jobNodeRepository.save(jobNodeEntity);
            jobContent.getScriptNodeDetailEntity()
                    .setId(scriptNodeDetailRepository.findDtSqlScriptNodeDetailEntityByJobNodeId(jobNodeId).getId());
            jobContent.getScriptNodeDetailEntity().setJobNodeId(jobNodeId);
            scriptNodeDetailRepository.save(jobContent.getScriptNodeDetailEntity());
        }
        jobDependenceRepository.deleteAllByJobName(jobEntity.getName());
        for (DtBatchJobDependenceEntity dtBatchJobDependenceEntity : jobContent.getJobDependenceEntities()) {
            jobDependenceRepository.save(dtBatchJobDependenceEntity);
        }
    }

    /**
     * 调用作业的启动接口
     *
     * @param ids 请求启动接口入参
     */
    public void requestBatchJobStart(ReqIds ids) {
        JSONObject response;
        try {
            response = HttpRequestSender.doPostWithLocale(ImportExportConfig.getBatchStartUrl(), ids);
        } catch (Exception e) {
            LOGGER.error("request Batch-Job Start failed: ", e);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_ERROR);
        }
        if (response.getJSONObject("result").getInteger("failure") == 1) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_START_BATCH_JOB_FAIL, response);
        }
    }
}