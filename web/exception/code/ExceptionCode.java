/*
 * 文 件 名:  ExceptionCode.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.0.T22
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/7
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.exception.code;

/**
 * 错误码定义
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/7]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public interface ExceptionCode {
    /**
     * 系统错误。
     */
    String DATATOOL_SYSTEM_ERROR = "DATATOOL_SYSTEM_ERROR";

    /**
     * 未授权的访问。
     */
    String DATATOOL_ACCESS_TOKEN_AUTH_FAILED = "DATATOOL_ACCESS_TOKEN_AUTH_FAILED";

    /**
     * 请求获取用户接口失败。
     */
    String DATATOOL_REQUEST_USER_INTERFACE_FAIL = "DATATOOL_REQUEST_USER_INTERFACE_FAIL";

    /**
     * 作业使用中，无法修改。
     */
    String DATATOOL_JOB_IN_USE = "DATATOOL_JOB_IN_USE";

    /**
     * 作业不存在。
     */
    String DATATOOL_JOB_NOT_EXIST = "DATATOOL_JOB_NOT_EXIST";

    /**
     * 作业名称已存在。
     */
    String DATATOOL_JOB_NAME_ALREADY_EXIST = "DATATOOL_JOB_NAME_ALREADY_EXIST";

    /**
     * 连接flink服务异常。
     */
    String DATATOOL_CONNECT_TO_FLINK_FAIL = "DATATOOL_CONNECT_TO_FLINK_FAIL";

    /**
     * 获取集群id失败。
     */
    String DATATOOL_GET_CLUSTER_ID_FAIL = "DATATOOL_GET_CLUSTER_ID_FAIL";

    /**
     * 获取jar id失败。
     */
    String DATATOOL_GET_JAR_ID_FAIL = "DATATOOL_GET_JAR_ID_FAIL";

    /**
     * 下载日志异常。
     */
    String DATATOOL_FLINK_LOG_DOWNLOAD_ERROR = "DATATOOL_FLINK_LOG_DOWNLOAD_ERROR";

    /**
     * 提交flink作业异常。
     */
    String DATATOOL_CREATE_FLINK_JOB_FAIL = "DATATOOL_CREATE_FLINK_JOB_FAIL";

    /**
     * 未知的变量或key：{0}。
     */
    String DATATOOL_UNKNOWN_VAR_OR_KEY = "DATATOOL_UNKNOWN_VAR_OR_KEY";

    /**
     * 获取作业状态异常，原因：{0}。
     */
    String DATATOOL_GET_JOB_STATUS_ERROR = "DATATOOL_GET_JOB_STATUS_ERROR";

    /**
     * 解密错误，原因：{0}。
     */
    String DATATOOL_DECODE_ERROR = "DATATOOL_DECODE_ERROR";

    /**
     * 获取当前用户失败。
     */
    String DATATOOL_GET_CURRENT_USER_FAIL = "DATATOOL_GET_CURRENT_USER_FAIL";

    /**
     * 获取当前用户类型失败。
     */
    String DATATOOL_GET_CURRENT_USER_TYPE_FAIL = "DATATOOL_GET_CURRENT_USER_TYPE_FAIL";

    /**
     * 压缩文件解压后大小超过10MB。
     */
    String DATATOOL_ZIP_FILE_UNZIPPED_SIZE_EXCEED_LIMIT = "DATATOOL_ZIP_FILE_UNZIPPED_SIZE_EXCEED_LIMIT";

    /**
     * 压缩文件大小超过1MB。
     */
    String DATATOOL_ZIP_FILE_SIZE_EXCEED_LIMIT = "DATATOOL_ZIP_FILE_SIZE_EXCEED_LIMIT";

    /**
     * 压缩文件解压后文件数量超过500个。
     */
    String DATATOOL_ZIP_FILE_UNZIPPED_NUM_EXCEED_LIMIT = "DATATOOL_ZIP_FILE_UNZIPPED_NUM_EXCEED_LIMIT";

    /**
     * 压缩文件不能为空。
     */
    String DATATOOL_ZIP_FILE_CANNOT_EMPTY = "DATATOOL_ZIP_FILE_CANNOT_EMPTY";

    /**
     * ZIP里文件名有非法字符
     */
    String DATATOOL_ZIP_CHILD_FILE_PATH_INVALID = "DATATOOL_ZIP_CHILD_FILE_PATH_INVALID";

    /**
     * 非法的文件名
     */
    String DATATOOL_ILLEGAL_IMPORT_FILE_NAME = "DATATOOL_ILLEGAL_IMPORT_FILE_NAME";

    /**
     * 非法的文件类型
     */
    String DATATOOL_ILLEGAL_FILE_TYPE = "DATATOOL_ILLEGAL_FILE_TYPE";

    /**
     * 不支持的上传文件类型，只支持zip类型的文件。
     */
    String DATATOOL_UPLOAD_FILE_TYPE_NOT_SUPPORT = "DATATOOL_UPLOAD_FILE_TYPE_NOT_SUPPORT";

    /**
     * 上传文件名称不合法。
     */
    String DATATOOL_ILLEGAL_UPLOAD_FILE_NAME = "DATATOOL_ILLEGAL_UPLOAD_FILE_NAME";

    /**
     * 不支持的上传文件来源，只支持{0}的文件来源。
     */
    String DATATOOL_UPLOAD_FILE_ORIGIN_NOT_SUPPORT = "DATATOOL_UPLOAD_FILE_ORIGIN_NOT_SUPPORT";

    /**
     * 不支持导入策略，只支持“跳过”和“覆盖”。
     */
    String DATATOOL_IMPORT_POLICY_NOT_SUPPORT = "DATATOOL_IMPORT_POLICY_NOT_SUPPORT";

    /**
     * 不支持导入模式，只支持“MANUAL”和“AUTO”。
     */
    String DATATOOL_IMPORT_MODE_NOT_SUPPORT = "DATATOOL_IMPORT_MODE_NOT_SUPPORT";

    /**
     * 不支持的导入资源类型。
     */
    String DATATOOL_IMPORT_RESOURCE_TYPE_NOT_SUPPORT = "DATATOOL_IMPORT_RESOURCE_TYPE_NOT_SUPPORT";

    /**
     * 数据连接不能覆盖导入。
     */
    String DATATOOL_IMPORT_CONNECTION_TYPE_NOT_SUPPORT = "DATATOOL_IMPORT_CONNECTION_TYPE_NOT_SUPPORT";

    /**
     * 不支持的导出资源类型。
     */
    String DATATOOL_EXPORT_RESOURCE_TYPE_NOT_SUPPORT = "DATATOOL_EXPORT_RESOURCE_TYPE_NOT_SUPPORT";

    /**
     * 查询流处理作业详情失败。
     */
    String DATATOOL_QUERY_STREAM_JOB_DETAIL_FAIL = "DATATOOL_QUERY_STREAM_JOB_DETAIL_FAIL";

    /**
     * 不支持的告警类型：{0}。
     */
    String DATATOOL_ALARM_TYPE_NOT_SUPPORT = "DATATOOL_ALARM_TYPE_NOT_SUPPORT";

    /**
     * 不支持的时间间隔类型：{0}。
     */
    String DATATOOL_TIME_INTERVAL_TYPE_NOT_SUPPORT = "DATATOOL_TIME_INTERVAL_TYPE_NOT_SUPPORT";

    /**
     * 删除数据库记录失败。
     */
    String DATATOOL_DELETE_DB_RECORDS_FAIL = "DATATOOL_DELETE_DB_RECORDS_FAIL";

    /**
     * 查询流处理作业失败。
     */
    String DATATOOL_QUERY_STREAM_JOB_FAIL = "DATATOOL_QUERY_STREAM_JOB_FAIL";

    /**
     * 启动流处理作业失败。
     */
    String DATATOOL_START_STREAM_JOB_FAIL = "DATATOOL_START_STREAM_JOB_FAIL";

    /**
     * 停止流处理作业失败。
     */
    String DATATOOL_STOP_STREAM_JOB_FAIL = "DATATOOL_STOP_STREAM_JOB_FAIL";

    /**
     * 启动批处理作业失败。
     */
    String DATATOOL_START_BATCH_JOB_FAIL = "DATATOOL_START_BATCH_JOB_FAIL";

    /**
     * 日期格式不合法。
     */
    String DATATOOL_TIME_FORMAT_INVALID = "DATATOOL_TIME_FORMAT_INVALID";

    /**
     * 创建savepoint失败。
     */
    String DATATOOL_SAVEPOINT_CREATE_FAIL = "DATATOOL_SAVEPOINT_CREATE_FAIL";

    /**
     * 请求url错误。
     */
    String DATATOOL_REQUEST_URL_ERROR = "DATATOOL_REQUEST_URL_ERROR";

    /**
     * 指标值不能为空。
     */
    String DATATOOL_FLINK_JOB_VERTEX_METRICS_CANNOT_EMPTY = "DATATOOL_FLINK_JOB_VERTEX_METRICS_CANNOT_EMPTY";

    /**
     * 指标数量{0}个，超过最大数量8个。
     */
    String DATATOOL_FLINK_JOB_VERTEX_METRICS_NUM_EXCEED_LIMIT = "DATATOOL_FLINK_JOB_VERTEX_METRICS_NUM_EXCEED_LIMIT";

    /**
     * 指查询算子不能为空。
     */
    String DATATOOL_FLINK_JOB_VERTEX_ID_CANNOT_EMPTY = "DATATOOL_FLINK_JOB_VERTEX_ID_CANNOT_EMPTY";

    /**
     * 查询算子不存在。
     */
    String DATATOOL_FLINK_JOB_VERTEX_ID_NOT_EXIST = "DATATOOL_FLINK_JOB_VERTEX_ID_NOT_EXIST";

    /**
     * 调用Flink服务发生异常
     */
    String DATATOOL_FLINK_SERVICE_ERROR = "DATATOOL_FLINK_SERVICE_ERROR";

    /**
     * 调用Flink服务url请求方式异常
     */
    String DATATOOL_URL_SCHEME_ERROR = "DATATOOL_URL_SCHEME_ERROR";

    /**
     * 调用url的端口异常
     */
    String DATATOOL_URL_PORT_ERROR = "DATATOOL_URL_PORT_ERROR";

    /**
     * 流作业数量超过上限。
     */
    String DATATOOL_STREAM_JOBS_NUM_EXCEED_LIMIT = "DATATOOL_STREAM_JOBS_NUM_EXCEED_LIMIT";

    /**
     * flink作业可用资源不足。
     */
    String DATATOOL_JOBS_SLOTS_EXCEED_LIMIT = "DATATOOL_JOBS_SLOTS_EXCEED_LIMIT";

    /**
     * 批量启、停、删除，导入详情，导入历史，成功响应。
     */
    String DATATOOL_SUCCESS = "DATATOOL_SUCCESS";

    /**
     * 批量启、停、删除，导入详情，导入历史，失败响应。
     */
    String DATATOOL_FAILURE = "DATATOOL_FAILURE";

    /**
     * 作业状态非法。
     */
    String DATATOOL_STREAM_JOB_STATE_ERROR = "DATATOOL_STREAM_JOB_STATE_ERROR";

    /**
     * 不支持的强制删除状态。
     */
    String DATATOOL_FORCE_DELETE_STATE_NOT_SUPPORT = "DATATOOL_FORCE_DELETE_STATE_NOT_SUPPORT";

    /**
     * 跨站脚本token鉴权失败。
     */
    String DATATOOL_INVALID_CROSS_DOMAIN = "DATATOOL_INVALID_CROSS_DOMAIN";

    /**
     * 文件内容转换失败。
     */
    String DATATOOL_FILE_CONTENT_CONVERT_FAIL = "DATATOOL_FILE_CONTENT_CONVERT_FAIL";

    /**
     * 历史表id不存在
     */
    String DATATOOL_HISTORY_NOT_EXIST = "DATATOOL_HISTORY_NOT_EXIST";

    /**
     * 参数必填且非空。
     */
    String DATATOOL_PARAM_EMPTY = "DATATOOL_PARAM_EMPTY";

    /**
     * 数据连接名称不合法。
     */
    String DATATOOL_ILLEGAL_CONNECTION_NAME = "DATATOOL_ILLEGAL_CONNECTION_NAME";

    /**
     * 数据连接数据库不合法
     */
    String DATATOOL_ILLEGAL_CONNECTION_DATABASE = "DATATOOL_ILLEGAL_CONNECTION_DATABASE";

    /**
     * 数据库不能为空。
     */
    String DATATOOL_CONNECTION_DATABASE_EMPTY = "DATATOOL_CONNECTION_DATABASE_EMPTY";

    /**
     * 数据连接数据名称不能为空。
     */
    String DATATOOL_CONNECTION_NAME_EMPTY = "DATATOOL_CONNECTION_NAME_EMPTY";

    /**
     * 数据连接类型不支持。
     */
    String DATATOOL_CONNECTION_TYPE_NOT_SUPPORT = "DATATOOL_CONNECTION_TYPE_NOT_SUPPORT";

    /**
     * 服务器端口不正确。
     */
    String DATATOOL_ILLEGAL_PORT = "DATATOOL_ILLEGAL_PORT";

    /**
     * IP地址不合法。
     */
    String DATATOOL_ILLEGAL_IP = "DATATOOL_ILLEGAL_IP";

    /**
     * 数据连接用户名不合法。
     */
    String DATATOOL_ILLEGAL_USERNAME = "DATATOOL_ILLEGAL_USERNAME";

    /**
     * 密码箱名称不合法。
     */
    String DATATOOL_ILLEGAL_CIPHER_VAR_NAME = "DATATOOL_ILLEGAL_CIPHER_VAR_NAME";

    /**
     * 环境变量名称不合法。
     */
    String DATATOOL_ILLEGAL_ENV_VAR_NAME = "DATATOOL_ILLEGAL_ENV_VAR_NAME";

    /**
     * 环境变量值不合法
     */
    String DATATOOL_ILLEGAL_ENV_VAR_VALUE = "DATATOOL_ILLEGAL_ENV_VAR_VALUE";

    /**
     * 系统配置类型不支持
     */
    String DATATOOL_SYS_CONFIG_TYPE_NOT_SUPPORT = "DATATOOL_SYS_CONFIG_TYPE_NOT_SUPPORT";

    /**
     * 获取最大作业数量失败
     */
    String DATATOOL_QUERY_MAX_JOB_NUMBER_FAIL = "DATATOOL_QUERY_MAX_JOB_NUMBER_FAIL";

    /**
     * 作业类型不支持
     */
    String DATATOOL_JOB_TYPE_NOT_SUPPORT = "DATATOOL_JOB_TYPE_NOT_SUPPORT";

    /**
     * 资产分组类型不支持
     */
    String DATATOOL_ASSET_GROUP_TYPE_NOT_SUPPORT = "DATATOOL_ASSET_GROUP_TYPE_NOT_SUPPORT";

    /**
     * 缺少概览页系统配置
     */
    String DATATOOL_SYSTEM_CONFIG_OVERVIEW_MISSING = "DATATOOL_SYSTEM_CONFIG_OVERVIEW_MISSING";

    /**
     * 数据连接不存在
     */
    String DATATOOL_CONNECTION_NOT_EXIST = "DATATOOL_CONNECTION_NOT_EXIST";

    /**
     * 流处理chkMode不合法
     */
    String DATATOOL_STREAM_JOB_CHKMODE_INVALID = "DATATOOL_STREAM_JOB_CHKMODE_INVALID";

    /**
     * 流处理chkMode不合法，这个是用在导入的校验中
     */
    String DATATOOL_STREAM_JOB_CHKMODE_INVALID2 = "DATATOOL_STREAM_JOB_CHKMODE_INVALID2";

    /**
     * 流处理作业名称不合法
     */
    String DATATOOL_ILLEGAL_STREAM_NAME = "DATATOOL_ILLEGAL_STREAM_NAME";

    /**
     * 流处理作业描述不合法
     */
    String DATATOOL_ILLEGAL_STREAM_DESCRIPTION = "DATATOOL_ILLEGAL_STREAM_DESCRIPTION";

    /**
     * 流处理作业并行度不合法
     */
    String DATATOOL_ILLEGAL_STREAM_PARALLELISM = "DATATOOL_ILLEGAL_STREAM_PARALLELISM";

    /**
     * 流处理作业重试次数不合法
     */
    String DATATOOL_ILLEGAL_STREAM_RETRY_TIMES = "DATATOOL_ILLEGAL_STREAM_RETRY_TIMES";

    /**
     * 流处理作业检查间隔不合法
     */
    String DATATOOL_ILLEGAL_STREAM_CHECK_INTERVAL = "DATATOOL_ILLEGAL_STREAM_CHECK_INTERVAL";

    /**
     * 数据库连接异常
     */
    String DATATOOL_DATABASE_CONNECTION_ERROR = "DATATOOL_DATABASE_CONNECTION_ERROR";

    /**
     * cron表达式不合法
     */
    String DATATOOL_ILLEGAL_CRON_EXPRESSION = "DATATOOL_ILLEGAL_CRON_EXPRESSION";

    /**
     * 目录路径不合法
     */
    String DATATOOL_ILLEGAL_DIRECTORY_PATH = "DATATOOL_ILLEGAL_DIRECTORY_PATH";

    /**
     * 目录名称不合法
     */
    String DATATOOL_ILLEGAL_DIRECTORY_NAME = "DATATOOL_ILLEGAL_DIRECTORY_NAME";

    /**
     * 脚本名称不合法
     */
    String DATATOOL_ILLEGAL_SCRIPT_NAME = "DATATOOL_ILLEGAL_SCRIPT_NAME";

    /**
     * 作业名称不合法
     */
    String DATATOOL_ILLEGAL_JOB_NAME = "DATATOOL_ILLEGAL_JOB_NAME";

    /**
     * 不支持的调度类型
     */
    String DATATOOL_SCHEDULE_TYPE_NOT_SUPPORT = "DATATOOL_SCHEDULE_TYPE_NOT_SUPPORT";

    /**
     * 不支持的依赖失败策略
     */
    String DATATOOL_DEPEND_FAIL_POLICY_NOT_SUPPORT = "DATATOOL_DEPEND_FAIL_POLICY_NOT_SUPPORT";

    /**
     * 不支持的周期
     */
    String DATATOOL_PERIOD_NOT_SUPPORT = "DATATOOL_PERIOD_NOT_SUPPORT";

    /**
     * 节点名称不合法
     */
    String DATATOOL_ILLEGAL_NODE_NAME = "DATATOOL_ILLEGAL_NODE_NAME";

    /**
     * 失败策略
     */
    String DATATOOL_FAIL_POLICY_TYPE_NOT_SUPPORT = "DATATOOL_FAIL_POLICY_TYPE_NOT_SUPPORT";

    /**
     * 告警阈值不合法
     */
    String DATATOOL_ILLEGAL_JOB_ALARM_THRESHOLD = "DATATOOL_ILLEGAL_JOB_ALARM_THRESHOLD";

    /**
     * 数据连接数量超过上限
     */
    String DATATOOL_CONNECTION_NUM_EXCEED_LIMIT = "DATATOOL_CONNECTION_NUM_EXCEED_LIMIT";

    /**
     * 环境变量数量超过上限
     */
    String DATATOOL_ENV_VAR_NUM_EXCEED_LIMIT = "DATATOOL_ENV_VAR_NUM_EXCEED_LIMIT";

    /**
     * 脚本数量超过上限
     */
    String DATATOOL_SCRIPT_NUM_EXCEED_LIMIT = "DATATOOL_SCRIPT_NUM_EXCEED_LIMIT";

    /**
     * 脚本目录数量超过上限
     */
    String DATATOOL_SCRIPT_DIRECTORY_NUM_EXCEED_LIMIT = "DATATOOL_SCRIPT_DIRECTORY_NUM_EXCEED_LIMIT";

    /**
     * 密码箱变量数量超过上限
     */
    String DATATOOL_CIPHER_VAR_NUM_EXCEED_LIMIT = "DATATOOL_CIPHER_VAR_NUM_EXCEED_LIMIT";

    /**
     * 作业数量超过上限
     */
    String DATATOOL_JOBS_NUM_EXCEED_LIMIT = "DATATOOL_JOBS_NUM_EXCEED_LIMIT";

    /**
     * 作业目录数量超过上限
     */
    String DATATOOL_JOBS_DIR_NUM_EXCEED_LIMIT = "DATATOOL_JOBS_DIR_NUM_EXCEED_LIMIT";

    /**
     * 作业最大执行时间不合法
     */
    String DATATOOL_ILLEGAL_JOB_EXECUTION_TIME = "DATATOOL_ILLEGAL_JOB_EXECUTION_TIME";

    /**
     * 作业重试不合法
     */
    String DATATOOL_ILLEGAL_JOB_RETRY = "DATATOOL_ILLEGAL_JOB_RETRY";

    /**
     * 缺少必要参数
     */
    String DATATOOL_PARAM_MISSING = "DATATOOL_PARAM_MISSING";

    /**
     * 参数列表为空
     */
    String DATATOOL_PARAM_LIST_EMPTY = "DATATOOL_PARAM_LIST_EMPTY";

    /**
     * 无效的参数类型
     */
    String DATATOOL_INVALID_PARAM_TYPE = "DATATOOL_INVALID_PARAM_TYPE";

    /**
     * 未知异常
     */
    String DATATOOL_PARAM_UNKNOWN_ERROR = "DATATOOL_PARAM_UNKNOWN_ERROR";

    /**
     * 文件内容非json格式
     */
    String DATATOOL_FILE_CONTENT_NOT_JSON_TYPE = "DATATOOL_FILE_CONTENT_NOT_JSON_TYPE";

    /**
     * 不安全的路径
     */
    String DATATOOL_ILLEGAL_PATH = "DATATOOL_ILLEGAL_PATH";

    /**
     * id不合法
     */
    String DATATOOL_ILLEGAL_ID = "DATATOOL_ILLEGAL_ID";

    /**
     * 系统配置参数中存在重复的key
     */
    String DATATOOL_SYSTEM_CONFIGURATION_DUPLICATE_KEY = "DATATOOL_SYSTEM_CONFIGURATION_DUPLICATE_KEY";

    /**
     * 通用配置中资产后缀参数错误
     */
    String DATATOOL_ILLEGAL_COMMON_CONFIGURATION_SUFFIXES = "DATATOOL_ILLEGAL_COMMON_CONFIGURATION_SUFFIXES";

    /**
     * 通用配置中资产后缀标识校验失败
     */
    String DATATOOL_ILLEGAL_COMMON_CONFIGURATION_SUFFIX_ID = "DATATOOL_ILLEGAL_COMMON_CONFIGURATION_SUFFIX_ID";

    /**
     * 并发控制
     */
    String DATATOOL_CURRENT_REQUEST_EXCEED_LIMIT = "DATATOOL_CURRENT_REQUEST_EXCEED_LIMIT";

    /**
     * 系统、通用配置类型错误
     */
    String DATATOOL_CONFIG_TYPE_ERROR = "DATATOOL_CONFIG_TYPE_ERROR";

    /**
     * 批处理脚本不存在
     */
    String DATATOOL_SCRIPT_NOT_EXIST = "DATATOOL_SCRIPT_NOT_EXIST";

    /**
     * 暂无数据
     */
    String DATATOOL_NA = "DATATOOL_NA";
}