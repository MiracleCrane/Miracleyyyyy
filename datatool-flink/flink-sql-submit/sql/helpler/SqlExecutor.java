/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

package com.huawei.dataservice.sql.helpler;

import com.huawei.dataservice.sql.exp.SubmitRuntimeException;
import com.huawei.dataservice.sql.helpler.section.SqlSegment;
import com.huawei.dataservice.sql.utils.SqlParser;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.StatementSet;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Flink Sql submit tool
 *
 * @author zwx632190
 * @version [SmartCampus V100R001C00, 2020/12/30]
 * @see FlinkJobOptions
 * @since [SmartCampus V100R001C00]
 */
public class SqlExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlExecutor.class);

    /**
     * 正则表达式忽略大小写
     */
    private static final int DEFAULT_PATTERN_FLAGS = Pattern.CASE_INSENSITIVE | Pattern.DOTALL;

    /**
     * Sql Insert语句识别字符串
     */
    private static final String INSERT_PATTERN = ".*\\s?INSERT\\s+INTO.*";

    /**
     * 正则实例化
     */
    private static Pattern insertPattern = Pattern.compile(INSERT_PATTERN, DEFAULT_PATTERN_FLAGS);

    /**
     * 实时TableEnvironment
     */
    private TableEnvironment tEnv;

    /**
     * 外部接受到的变量值
     */
    private String param;

    /**
     * 外部接受的字符是否是SQL
     */
    private boolean isSql;

    /**
     * Flink任务启动参数
     */
    private FlinkJobOptions options;

    /**
     * 构造函数，入参可以是sql文件路径或者sql
     *
     * @param param 路径或sql
     * @param isSql 是否为sql
     * @param options options
     */
    public SqlExecutor(String param, boolean isSql, FlinkJobOptions options) {
        this.param = param;
        this.isSql = isSql;
        this.options = options;
    }

    /**
     * Flink SQL执行入口，需要传入sql及flink作业的参数。参考{@link SqlExecutor#initFlinkJobOptions}
     *
     * @param args 作业运行参数列表
     */
    public static void main(String[] args) {
        ParameterTool parameterTool = ParameterTool.fromArgs(args);

        // sql file path
        String sqlPath = parameterTool.get("sqlFile", "");

        // sql string
        String sql = parameterTool.get("sql", "");

        FlinkJobOptions flinkJobOptions = SqlExecutor.initFlinkJobOptions(parameterTool);

        SqlExecutor executor = null;
        if (!StringUtils.isBlank(sqlPath)) {
            executor = new SqlExecutor(sqlPath, false, flinkJobOptions);
        } else if (!StringUtils.isBlank(sql)) {
            executor = new SqlExecutor(sql, true, flinkJobOptions);
        } else {
            throw new IllegalArgumentException(
                    "Parameter sqlFile or sql is null,you must chose one.Use --sqlFile  or --sql.");
        }

        executor.run();
    }

    /**
     * 初始化Flink Job参数<br>
     * <b>参数说明</b>
     * <ul>
     * <li>jobname - flink作业名称</li>
     * <li>retry-times - 作业重启次数，默认值为：0</li>
     * <li>restart-delay - 作业重启时间间隔，单位s，默认值为：10s</li>
     * <li>enable-chk - 是否开启CheckPoint，取值范围：[true,false]，默认值为：false</li>
     * <li>chk-mode - CheckPoint的模式，0代表AT_LEAST_ONCE，1代表EXACTLY_ONCE，默认值为：0</li>
     * <li>chk-interval - Checkpoint触发间隔，单位毫秒，默认值为：60000</li>
     * <li>chk-min-pause - 两次检查点之间的最小时间间隔，单位毫秒，默认值为：1000</li>
     * <li>chk-timeout - 检查点的超时时间，如果超时则放弃，单位毫秒，默认值为：60000</li>
     * </ul>
     *
     * @param paramTool 配置项
     * @return FlinkJobOptions对象
     */
    private static FlinkJobOptions initFlinkJobOptions(ParameterTool paramTool) {
        FlinkJobOptions flinkJobOptions = new FlinkJobOptions();

        // 设置Flink作业名称
        flinkJobOptions.setJobName(paramTool.get("jobname"));

        // 初始化重启配置参数
        flinkJobOptions.setRestartAttempts(paramTool.getInt("retry-times", 0));
        flinkJobOptions.setRestartDelayInterval(paramTool.getLong("restart-delay", 10));

        // 初始化CheckPoint配置参数
        flinkJobOptions.setEnableCheckPoint(paramTool.getBoolean("enable-chk", false));
        int chkMode = paramTool.getInt("chk-mode", 0);
        if (chkMode == 1) {
            flinkJobOptions.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        } else {
            flinkJobOptions.setCheckpointingMode(CheckpointingMode.AT_LEAST_ONCE);
        }
        flinkJobOptions.setCheckpointInterval(paramTool.getLong("chk-interval", 60000));
        flinkJobOptions.setMinPauseBetweenCheckpoints(paramTool.getLong("chk-min-pause", 1000));
        flinkJobOptions.setCheckpointTimeout(paramTool.getLong("chk-timeout", 60000));

        return flinkJobOptions;
    }

    private void run() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 禁止算子合并
        env.disableOperatorChaining();

        if (this.options.getRestartAttempts() >= 0) {
            // 重启策略为固定间隔重启
            env.setRestartStrategy(RestartStrategies.fixedDelayRestart(this.options.getRestartAttempts(),
                    Time.seconds(this.options.getRestartDelayInterval())));
        } else {
            throw new IllegalArgumentException("The parameter '--retry-times' or '-retry-times' cannot be negative .");
        }

        // CheckPoint配置
        if (options.isEnableCheckPoint()) {
            // 开启CheckPoint
            env.enableCheckpointing(options.getCheckpointInterval());

            // 设置模式为精确一次 (这是默认值)
            env.getCheckpointConfig().setCheckpointingMode(options.getCheckpointingMode());

            // 确认 checkpoints 之间的时间会进行 500 ms
            env.getCheckpointConfig().setMinPauseBetweenCheckpoints(options.getMinPauseBetweenCheckpoints());

            // Checkpoint 必须在一分钟内完成，否则就会被抛弃
            env.getCheckpointConfig().setCheckpointTimeout(options.getCheckpointTimeout());

            // 同一时间只允许一个 checkpoint 进行
            env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);

            // 开启在 job 中止后仍然保留的 externalized checkpoints
            env.getCheckpointConfig().enableExternalizedCheckpoints(
                    CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION);
        }

        EnvironmentSettings bsSettings = EnvironmentSettings.newInstance().inStreamingMode().build();
        tEnv = StreamTableEnvironment.create(env, bsSettings);

        // 设置jobName
        if (StringUtils.isNotEmpty(options.getJobName())) {
            tEnv.getConfig().getConfiguration().setString("pipeline.name", options.getJobName());
        }

        tEnv.getConfig().set("table.exec.legacy-cast-behaviour", "ENABLED");

        List<SqlSegment> sqls;
        if (!this.isSql) {
            sqls = SqlParser.getSqlListFromFile(this.param);
        } else {
            sqls = SqlParser.getSqlListFromSql(this.param);
        }
        if (!sqls.isEmpty()) {
            execute(sqls);
        } else {
            LOGGER.error("There is no sql to execute,check your Sql.");
            throw new SubmitRuntimeException("There is no sql to execute,check your Sql.");
        }
    }

    /**
     * 执行所有sql片段
     *
     * @param sqlSegments 分割后的sql片段集合
     */
    public void execute(List<SqlSegment> sqlSegments) {
        StatementSet stmtSet = tEnv.createStatementSet();
        sqlSegments.forEach((SqlSegment sqlSegment) -> {
            if (isInsertSql(sqlSegment.getSql())) {
                // 如果是Insert语句
                stmtSet.addInsertSql(sqlSegment.getSql());
            } else {
                // DML语句执行此方法
                tEnv.executeSql(sqlSegment.getSql()).print();
            }
        });
        stmtSet.execute();
    }

    private boolean isInsertSql(String sql) {
        Matcher matcher = insertPattern.matcher(sql);
        return matcher.matches();
    }
}
