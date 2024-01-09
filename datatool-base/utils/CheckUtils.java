/*
 * 文 件 名:  CheckUtils.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/10/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.utils;

import com.huawei.smartcampus.datatool.base.enumeration.ConnectionTypeEnum;
import com.huawei.smartcampus.datatool.base.enumeration.DataToolResourceTypeEnum;
import com.huawei.smartcampus.datatool.base.enumeration.DependFailPolicyEnum;
import com.huawei.smartcampus.datatool.base.enumeration.DgcResourceTypeEnum;
import com.huawei.smartcampus.datatool.base.enumeration.DuplicatePolicyEnum;
import com.huawei.smartcampus.datatool.base.enumeration.FailPolicyEnum;
import com.huawei.smartcampus.datatool.base.enumeration.FileSuffixEnum;
import com.huawei.smartcampus.datatool.base.enumeration.ImportModeEnum;
import com.huawei.smartcampus.datatool.base.enumeration.PeriodEnum;
import com.huawei.smartcampus.datatool.base.enumeration.ResourceOriginEnum;
import com.huawei.smartcampus.datatool.base.enumeration.ScheduleTypeEnum;
import com.huawei.smartcampus.datatool.base.model.TaskModel;
import com.huawei.smartcampus.datatool.base.pattern.ImportExportPattern;
import com.huawei.smartcampus.datatool.base.vo.dlivar.DliVar;
import com.huawei.smartcampus.datatool.base.vo.job.ImportJobContent;
import com.huawei.smartcampus.datatool.base.vo.req.ImportReq;
import com.huawei.smartcampus.datatool.base.vo.script.ImportScriptContent;
import com.huawei.smartcampus.datatool.entity.DtBatchJobEntity;
import com.huawei.smartcampus.datatool.entity.DtBatchJobNodeEntity;
import com.huawei.smartcampus.datatool.entity.DtCipherVariableEntity;
import com.huawei.smartcampus.datatool.entity.DtConnectionEntity;
import com.huawei.smartcampus.datatool.entity.DtEnvironmentVariableEntity;
import com.huawei.smartcampus.datatool.entity.DtSqlScriptNodeDetailEntity;
import com.huawei.smartcampus.datatool.entity.StreamJobEntity;
import com.huawei.smartcampus.datatool.exception.DataToolImportZipException;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.properties.QuantitativeConfig;
import com.huawei.smartcampus.datatool.repository.DtBatchJobRepository;
import com.huawei.smartcampus.datatool.repository.DtCipherVariableRepository;
import com.huawei.smartcampus.datatool.repository.DtConnectionRepository;
import com.huawei.smartcampus.datatool.repository.DtEnvironmentVariableRepository;
import com.huawei.smartcampus.datatool.repository.DtScriptRepository;
import com.huawei.smartcampus.datatool.repository.StreamJobRepository;
import com.huawei.smartcampus.datatool.utils.EnumUtils;
import com.huawei.smartcampus.datatool.utils.NormalizerUtil;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * 校验类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/10/10]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class CheckUtils {
    /**
     * zip包缓冲区大小
     */
    private static final int BUFFER = 512;

    /**
     * zip包中包含的文件个数限制
     */
    private static final int ZIP_CHILD_FILE_COUNT_MAX = 500;

    /**
     * zip解压最大大小，10MB
     */
    private static final long MAX_SIZE = 10485760L;

    /**
     * zip文件
     */
    public static final String ZIP_FILE = "zip";

    /**
     * 跨目录攻击，黑名单校验
     */
    private static final List<String> unsafePaths = Arrays.asList("./", ".\\", "..", ";");
    private static StreamJobRepository streamJobRepository = SpringContextHelper.getBean(StreamJobRepository.class);
    private static DtBatchJobRepository jobRepository = SpringContextHelper.getBean(DtBatchJobRepository.class);
    private static DtScriptRepository scriptRepository = SpringContextHelper.getBean(DtScriptRepository.class);
    private static DtConnectionRepository dtConnectionRepository = SpringContextHelper
            .getBean(DtConnectionRepository.class);
    private static DtEnvironmentVariableRepository dtEnvironmentVariableRepository = SpringContextHelper
            .getBean(DtEnvironmentVariableRepository.class);
    private static DtCipherVariableRepository dtCipherVariableRepository = SpringContextHelper
            .getBean(DtCipherVariableRepository.class);

    /**
     * 解压前参数校验
     *
     * @param importReq 导入入参
     */
    public static void checkImportParams(ImportReq importReq) {
        // 校验导入文件的文件后缀，只能为.zip后缀文件
        if (!ZIP_FILE.equals(FileOperateUtils.getFileSuffix(importReq.getResource()))) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_UPLOAD_FILE_TYPE_NOT_SUPPORT, ZIP_FILE);
        }
        // 校验zip文件名称
        checkPatternParam(FileOperateUtils.getFileNameWithoutSuffix(importReq.getResource()),
                ImportExportPattern.ZIP_FILE_NAME_PATTERN, ExceptionCode.DATATOOL_ILLEGAL_UPLOAD_FILE_NAME);
        // 校验资源来源
        if (!EnumUtils.isInclude(ResourceOriginEnum.class, importReq.getResourceOrigin().toLowerCase(Locale.ROOT),
                "origin")) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_UPLOAD_FILE_ORIGIN_NOT_SUPPORT);
        }
        // 校验资源来源为datatool时，只能为“conn”，“stream”，“job”，“script”，“env”，“cipher”
        // 校验资源来源为dgc时，只能为"conn", "env", "job", "script", "job_script", "dli", "dli_var"
        if ((ResourceOriginEnum.DATATOOL.origin().equalsIgnoreCase(importReq.getResourceOrigin())
                && !EnumUtils.isInclude(DataToolResourceTypeEnum.class,
                        importReq.getResourceType().toLowerCase(Locale.ROOT), "type"))
                || (ResourceOriginEnum.DGC.origin().equalsIgnoreCase(importReq.getResourceOrigin())
                        && !EnumUtils.isInclude(DgcResourceTypeEnum.class,
                                importReq.getResourceType().toLowerCase(Locale.ROOT), "type"))) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_IMPORT_RESOURCE_TYPE_NOT_SUPPORT);
        }
        // 校验导入策略，只支持“跳过”和“覆盖”。
        if (!EnumUtils.isInclude(DuplicatePolicyEnum.class, importReq.getDuplicatePolicy().toLowerCase(Locale.ROOT),
                "duplicatePolicy")) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_IMPORT_POLICY_NOT_SUPPORT);
        }
        // 校验导入模式，只支持“手动”和“自动”。
        if (importReq.getImportMode() != null && !EnumUtils.isInclude(ImportModeEnum.class,
                importReq.getImportMode().toUpperCase(Locale.ROOT), "importMode")) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_IMPORT_MODE_NOT_SUPPORT);
        }
        // 校验是数据连接时，只能是”跳过“，不支持”覆盖“
        if (DataToolResourceTypeEnum.CONN.type().equalsIgnoreCase(importReq.getResourceType())
                && DuplicatePolicyEnum.OVERWRITE.duplicatePolicy().equalsIgnoreCase(importReq.getDuplicatePolicy())) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_IMPORT_CONNECTION_TYPE_NOT_SUPPORT);
        }
    }

    /**
     * 校验数据连接
     *
     * @param dtConnectionEntity 数据连接实体
     * @param taskModel 任务模型
     */
    public static void checkConnParams(DtConnectionEntity dtConnectionEntity, TaskModel taskModel) {
        // 校验name、type、username必填非空
        checkEmptyParam(dtConnectionEntity.getName(), "name");
        checkEmptyParam(dtConnectionEntity.getType(), "type");
        checkEmptyParam(dtConnectionEntity.getUser(), "username");

        // 校验数据连接名称
        checkPatternParam(dtConnectionEntity.getName(), ImportExportPattern.CONN_NAME_PATTERN,
                ExceptionCode.DATATOOL_ILLEGAL_CONNECTION_NAME);
        // 校验数据连接类型
        if ((ResourceOriginEnum.DGC.origin().equals(taskModel.getResourceOrigin())
                && !ConnectionTypeEnum.OPENGAUSS.type().equals(dtConnectionEntity.getType()))
                || (ResourceOriginEnum.DATATOOL.origin().equals(taskModel.getResourceOrigin())
                        && !EnumUtils.isInclude(ConnectionTypeEnum.class, dtConnectionEntity.getType(), "type"))) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECTION_TYPE_NOT_SUPPORT);
        }
        // 校验端口
        if (dtConnectionEntity.getPort() <= 0 || dtConnectionEntity.getPort() > 65535) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_PORT);
        }
        // 校验ip，dgc的json资产时，host为集群，不校验
        if (!(ResourceOriginEnum.DGC.origin().equals(taskModel.getResourceOrigin())
                && FileSuffixEnum.JSON.suffix().equals(taskModel.getFileSuffix()))) {
            checkEmptyParam(dtConnectionEntity.getHost(), "host");
            checkPatternParam(dtConnectionEntity.getHost(), ImportExportPattern.CONN_HOST_PATTERN,
                    ExceptionCode.DATATOOL_ILLEGAL_IP);
        }
        // 校验userName
        checkPatternParam(dtConnectionEntity.getUser(), ImportExportPattern.NAME_PATTERN,
                ExceptionCode.DATATOOL_ILLEGAL_USERNAME);
        checkConnectionNum();
    }

    /**
     * 校验密码箱
     *
     * @param data 密码箱信息
     */
    public static <T> void checkCipherParams(T data) {
        if (data instanceof DtCipherVariableEntity) {
            DtCipherVariableEntity temp = (DtCipherVariableEntity) data;
            checkEmptyParam(temp.getKey(), "cipher_key");
            // 校验userName
            checkPatternParam(temp.getKey(), ImportExportPattern.CIPHER_NAME_PATTERN,
                    ExceptionCode.DATATOOL_ILLEGAL_CIPHER_VAR_NAME);
        }
        if (data instanceof DliVar) {
            DliVar temp = (DliVar) data;
            checkEmptyParam(temp.getName(), "var_name");
            // 校验userName
            checkPatternParam(temp.getName(), ImportExportPattern.CIPHER_NAME_PATTERN,
                    ExceptionCode.DATATOOL_ILLEGAL_CIPHER_VAR_NAME);
        }
        checkCipherVariablesNum();
    }

    /**
     * 校验环境变量
     *
     * @param data 环境变量信息
     */
    public static <T> void checkEnvParams(T data) {
        if (data instanceof DtEnvironmentVariableEntity) {
            DtEnvironmentVariableEntity temp = (DtEnvironmentVariableEntity) data;
            checkEmptyParam(temp.getKey(), "name");
            // 校验name
            checkPatternParam(temp.getKey(), ImportExportPattern.NAME_PATTERN,
                    ExceptionCode.DATATOOL_ILLEGAL_ENV_VAR_NAME);
            // 校验value
            checkEmptyParam(temp.getValue(), "value");
            if (temp.getValue().length() > 256) {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_ENV_VAR_VALUE);
            }
        }
        if (data instanceof DliVar) {
            DliVar temp = (DliVar) data;
            checkEmptyParam(temp.getName(), "var_name");
            // 校验name
            checkPatternParam(temp.getName(), ImportExportPattern.NAME_PATTERN,
                    ExceptionCode.DATATOOL_ILLEGAL_ENV_VAR_NAME);
            // 校验value
            checkEmptyParam(temp.getValue(), "var_value");
            if (temp.getValue().length() > 256) {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_ENV_VAR_VALUE);
            }
        }
        checkEnvironmentVariablesNum();
    }

    /**
     * 校验流处理作业
     *
     * @param streamJobEntity 流处理作业实体
     * @param type 类型
     */
    public static void checkStreamParams(StreamJobEntity streamJobEntity, String type) {
        checkEmptyParam(streamJobEntity.getName(), "name");
        checkMandatoryParam(streamJobEntity.getFlinkSql(), ExceptionCode.DATATOOL_PARAM_MISSING,
                DgcResourceTypeEnum.DLI.type().equals(type) ? "sql_body" : "flinkSql");
        if (DataToolResourceTypeEnum.STREAM.type().equals(type)) {
            checkPatternParam(String.valueOf(streamJobEntity.getRetryTimes()),
                    ImportExportPattern.STREAM_RETRY_TIMES_PATTERN, ExceptionCode.DATATOOL_ILLEGAL_STREAM_RETRY_TIMES);
        }
        checkPatternParam(streamJobEntity.getName(), ImportExportPattern.STREAM_NAME_PATTERN,
                ExceptionCode.DATATOOL_ILLEGAL_STREAM_NAME);
        checkPatternParam(streamJobEntity.getDescription(), ImportExportPattern.STREAM_DESC_PATTERN,
                ExceptionCode.DATATOOL_ILLEGAL_STREAM_DESCRIPTION);
        checkPatternParam(String.valueOf(streamJobEntity.getParallelism()),
                ImportExportPattern.STREAM_PARALLELISM_PATTERN, ExceptionCode.DATATOOL_ILLEGAL_STREAM_PARALLELISM);
        checkPatternParam(String.valueOf(streamJobEntity.getChkMode()), ImportExportPattern.CHK_MODE_PATTERN,
                ExceptionCode.DATATOOL_STREAM_JOB_CHKMODE_INVALID2, "[0,1]");
        // 单位是ms
        if (streamJobEntity.getChkInterval() > 2140000000) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_STREAM_CHECK_INTERVAL);
        }
        checkStreamJobsNum();
    }

    /**
     * 校验目录
     *
     * @param directory 目录
     * @param num 目录层级深度
     */
    private static void checkDirectory(String directory, int num) {
        if (StringUtils.isEmpty(directory)) {
            return;
        }
        checkPatternParam(directory, ImportExportPattern.PATH_PATTERN, ExceptionCode.DATATOOL_ILLEGAL_DIRECTORY_PATH,
                String.valueOf(num));
        if (!File.separator.equals(directory)) {
            String[] dirNames = directory.substring(1).split(File.separatorChar == '\\' ? "\\\\" : File.separator);
            if (dirNames.length > num) {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_DIRECTORY_PATH, num);
            }
            for (String dirName : dirNames) {
                checkPatternParam(dirName, ImportExportPattern.DIR_NAME_PATTERN,
                        ExceptionCode.DATATOOL_ILLEGAL_DIRECTORY_NAME);
            }
        }
    }

    /**
     * 校验脚本
     *
     * @param scriptContent 脚本信息
     */
    public static void checkScriptParams(ImportScriptContent scriptContent) {
        checkEmptyParam(scriptContent.getDtScriptEntity().getName(), "name");
        checkPatternParam(scriptContent.getDtScriptEntity().getName(), ImportExportPattern.SCRIPT_NAME_PATTERN,
                ExceptionCode.DATATOOL_ILLEGAL_SCRIPT_NAME);
        checkDirectory(scriptContent.getDir(), QuantitativeConfig.getMaxScriptDirectoryDepth());
        // 校验数据连接名称
        checkPatternParam(scriptContent.getDtScriptEntity().getConnName(), ImportExportPattern.CONN_NAME_PATTERN,
                ExceptionCode.DATATOOL_ILLEGAL_CONNECTION_NAME);
        checkScriptsNum();
    }

    /**
     * 校验作业
     *
     * @param jobContent 作业信息
     */
    public static void checkJobParams(ImportJobContent jobContent) {
        DtBatchJobEntity dtBatchJobEntity = jobContent.getJobEntity();
        checkEmptyParam(dtBatchJobEntity.getName(), "name");
        checkPatternParam(dtBatchJobEntity.getName(), ImportExportPattern.JOB_NAME_PATTERN,
                ExceptionCode.DATATOOL_ILLEGAL_JOB_NAME);
        checkDirectory(jobContent.getDir(), QuantitativeConfig.getMaxJobDirectoryDepth());
        // 校验调度类型
        if (!EnumUtils.isInclude(ScheduleTypeEnum.class, dtBatchJobEntity.getScheduleType(), "type")) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SCHEDULE_TYPE_NOT_SUPPORT,
                    dtBatchJobEntity.getScheduleType());
        }
        // 如果是周期调度的校验
        if (ScheduleTypeEnum.CRON.type().equals(dtBatchJobEntity.getScheduleType())) {
            // 校验依赖作业失败策略类型
            if (!EnumUtils.isInclude(DependFailPolicyEnum.class, dtBatchJobEntity.getDependFailPolicy(), "type")) {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_DEPEND_FAIL_POLICY_NOT_SUPPORT,
                        dtBatchJobEntity.getDependFailPolicy());
            }
            // 校验cron表达式
            checkPatternParam(dtBatchJobEntity.getCronExpr(), ImportExportPattern.CRON_EXPRESSION_PATTERN,
                    ExceptionCode.DATATOOL_ILLEGAL_CRON_EXPRESSION);

            // 校验周期
            if (!EnumUtils.isInclude(PeriodEnum.class, dtBatchJobEntity.getPeriod(), "type")) {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PERIOD_NOT_SUPPORT, dtBatchJobEntity.getPeriod());
            }
        }
        // 校验节点信息
        for (DtBatchJobNodeEntity dtBatchJobNodeEntity : jobContent.getJobNodeEntities()) {
            checkJobNodeParams(dtBatchJobNodeEntity, jobContent.getScriptNodeDetailEntity());
        }
        checkBatchJobsNum();
    }

    private static void checkJobNodeParams(DtBatchJobNodeEntity jobNodeEntity,
            DtSqlScriptNodeDetailEntity detailEntity) {
        // 节点名称
        checkPatternParam(jobNodeEntity.getName(), ImportExportPattern.NODE_NAME_PATTERN,
                ExceptionCode.DATATOOL_ILLEGAL_NODE_NAME);
        if (!FailPolicyEnum.FAIL.type().equalsIgnoreCase(jobNodeEntity.getFailPolicy())) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_FAIL_POLICY_TYPE_NOT_SUPPORT,
                    jobNodeEntity.getFailPolicy());
        }
        // 告警阈值最大为1h
        if (jobNodeEntity.getAlarmThreshold() < 180 || jobNodeEntity.getAlarmThreshold() > 3600) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_JOB_ALARM_THRESHOLD);
        }
        // 最大执行时间
        if (jobNodeEntity.getMaxExecutionTime() < 1 || jobNodeEntity.getMaxExecutionTime() > 3600) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ILLEGAL_JOB_EXECUTION_TIME);
        }
        // 校验脚本名称
        checkEmptyParam(detailEntity.getScriptName(), "scriptName");
        checkPatternParam(detailEntity.getScriptName(), ImportExportPattern.SCRIPT_NAME_PATTERN,
                ExceptionCode.DATATOOL_ILLEGAL_SCRIPT_NAME);
        // 校验数据连接名称
        checkEmptyParam(detailEntity.getConnName(), "connectionName");
        checkPatternParam(detailEntity.getConnName(), ImportExportPattern.CONN_NAME_PATTERN,
                ExceptionCode.DATATOOL_ILLEGAL_CONNECTION_NAME);
        // 校验数据库名称
        checkEmptyParam(detailEntity.getDatabase(), "database");
        // 校验脚本参数，必填非空
        checkEmptyParam(detailEntity.getScriptArgs(), "scriptArgs");
    }

    /**
     * 校验流作业数量是否超过配置个数
     */
    private static void checkStreamJobsNum() {
        if (streamJobRepository.findAll().size() >= QuantitativeConfig.getMaxStreamJobs()) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_STREAM_JOBS_NUM_EXCEED_LIMIT,
                    QuantitativeConfig.getMaxStreamJobs());
        }
    }

    /**
     * 校验批作业数量是否超过配置个数
     */
    private static void checkBatchJobsNum() {
        if (jobRepository.findAll().size() >= QuantitativeConfig.getMaxJobs()) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_JOBS_NUM_EXCEED_LIMIT,
                    QuantitativeConfig.getMaxJobs());
        }
    }

    /**
     * 校验数据连接数量是否超过配置个数
     */
    private static void checkConnectionNum() {
        if (dtConnectionRepository.findAll().size() >= QuantitativeConfig.getMaxConnections()) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECTION_NUM_EXCEED_LIMIT,
                    QuantitativeConfig.getMaxConnections());
        }
    }

    /**
     * 校验脚本数量是否超过配置个数
     */
    private static void checkScriptsNum() {
        if (scriptRepository.findAll().size() >= QuantitativeConfig.getMaxScripts()) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SCRIPT_NUM_EXCEED_LIMIT,
                    QuantitativeConfig.getMaxScripts());
        }
    }

    /**
     * 校验密码箱数量是否超过配置个数
     */
    private static void checkCipherVariablesNum() {
        if (dtCipherVariableRepository.findAll().size() >= QuantitativeConfig.getMaxCipherVariables()) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CIPHER_VAR_NUM_EXCEED_LIMIT,
                    QuantitativeConfig.getMaxCipherVariables());
        }
    }

    /**
     * 校验环境变量数量是否超过配置个数
     */
    private static void checkEnvironmentVariablesNum() {
        if (dtEnvironmentVariableRepository.findAll().size() >= QuantitativeConfig.getMaxEnvironmentVariables()) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ENV_VAR_NUM_EXCEED_LIMIT,
                    QuantitativeConfig.getMaxEnvironmentVariables());
        }
    }

    /**
     * 正则校验入参
     *
     * @param param 参数
     * @param pattern 正则
     * @param errorCode 错误码
     * @param errorParams 错误码携带参数
     */
    private static void checkPatternParam(String param, Pattern pattern, String errorCode, String... errorParams) {
        // 如果参数为空，不校验
        if (StringUtils.isEmpty(param)) {
            return;
        }
        if (!pattern.matcher(param).find()) {
            throw new DataToolRuntimeException(errorCode, errorParams);
        }
    }

    /**
     * 校验参数非空
     *
     * @param param 传入参数
     * @param paramName 参数名
     */
    private static void checkEmptyParam(String param, String paramName) {
        if (StringUtils.isEmpty(param)) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_PARAM_EMPTY, paramName);
        }
    }

    /**
     * 校验参数必填，不能为null，但可以为空
     *
     * @param param 传入参数
     * @param errorCode 异常码
     * @param paramName 参数名
     */
    private static void checkMandatoryParam(String param, String errorCode, String paramName) {
        if (param == null) {
            throw new DataToolRuntimeException(errorCode, paramName);
        }
    }

    /**
     * zip炸弹检查
     *
     * @param file 文件
     * @param totalSize 累积大小
     * @throws IOException 异常
     */
    public static void checkSize(Path file, AtomicLong totalSize) throws IOException {
        try (InputStream inputStream = Files.newInputStream(file)) {
            byte[] data = new byte[BUFFER];
            int size;
            while ((size = inputStream.read(data)) != -1) {
                totalSize.addAndGet(size);
                if (totalSize.get() > MAX_SIZE) {
                    throw new DataToolImportZipException(ExceptionCode.DATATOOL_ZIP_FILE_UNZIPPED_SIZE_EXCEED_LIMIT);
                }
            }
        }
    }

    /**
     * checkFileCount
     *
     * @param entriesCount 数量
     */
    public static void checkFileCount(AtomicInteger entriesCount) {
        if (entriesCount.incrementAndGet() > ZIP_CHILD_FILE_COUNT_MAX) {
            throw new DataToolImportZipException(ExceptionCode.DATATOOL_ZIP_FILE_UNZIPPED_NUM_EXCEED_LIMIT);
        }
    }

    /**
     * 检查解压后文件名
     *
     * @param file 文件
     */
    public static void checkZipChildFilePath(Path file) {
        // 白名单字符校验
        checkPatternParam(file.toString(), ImportExportPattern.ZIP_CHILD_FILE_PATH_PATTERN,
                ExceptionCode.DATATOOL_ZIP_CHILD_FILE_PATH_INVALID);
    }

    /**
     * 检查路径，防止跨目录攻击
     *
     * @param path 文件路径
     */
    public static void checkDirectoryTraversal(String path) {
        if (StringUtils.isEmpty(path)) {
            return;
        }
        String normalizeStr = NormalizerUtil.normalizeForString(path);
        for (String unsafePath : unsafePaths) {
            if (normalizeStr.contains(unsafePath)) {
                throw new DataToolImportZipException(ExceptionCode.DATATOOL_ILLEGAL_PATH);
            }
        }
    }

    /**
     * 检查文件路径是否存在
     *
     * @param target 目标路径
     * @throws IOException io异常
     */
    public static void checkFileExist(Path target) throws IOException {
        if (Files.notExists(target.getParent())) {
            Files.createDirectories(target.getParent());
        }
    }
}