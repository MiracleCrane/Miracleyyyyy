/*
 * 文 件 名:  LiquibaseUtil.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  22.1.0
 * 描    述:  <描述>
 * 修 改 人:  l30006786
 * 修改时间： 2022/11/4
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.util.liquibase;

import com.huawei.seccomponent.common.SCException;
import com.huawei.seccomponent.crypt.CryptoAPI;
import com.huawei.seccomponent.crypt.CryptoFactory;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * 调用liquibase接口维护数据库版本
 *
 * @author l30006786
 * @version [22.2.0, 2022/11/4]
 * @see [相关类/方法]
 * @since [22.2.0]
 */
public class LiquibaseUtil {
    private String sqlAlchemyType;
    private String sqlAlchemyIp;
    private String sqlAlchemyPort;
    private String sqlAlchemyDB;
    private String sqlAlchemySchema;
    private String sqlAlchemyUser;
    private String sqlAlchemyPwdEncrypted;
    private String changelogPath;
    private ResourceAccessor resourceAccessor;
    private CryptoAPI api;

    public LiquibaseUtil(String sqlAlchemyType, String sqlAlchemyIp, String sqlAlchemyPort, String sqlAlchemyDB,
            String sqlAlchemySchema, String sqlAlchemyUser, String sqlAlchemyPwdEncrypted, String changelogPath)
            throws SCException, FileNotFoundException {
        this.resourceAccessor = new CompositeResourceAccessor(
                new DirectoryResourceAccessor(Paths.get(System.getProperty("user.dir")).toAbsolutePath()));
        this.sqlAlchemyType = sqlAlchemyType;
        this.sqlAlchemyIp = sqlAlchemyIp;
        this.sqlAlchemyPort = sqlAlchemyPort;
        this.sqlAlchemyDB = sqlAlchemyDB;
        this.sqlAlchemySchema = sqlAlchemySchema;
        this.sqlAlchemyUser = sqlAlchemyUser;
        this.sqlAlchemyPwdEncrypted = sqlAlchemyPwdEncrypted;
        this.changelogPath = changelogPath;
        // 执行时保证前置步骤中已经将配置文件路径设置到环境变量中，此处直接从环境变量获取值
        this.api = CryptoFactory.getInstance(System.getenv("SCC_CONF"));
    }

    /**
     * 调用liquibase，执行配置库版本维护逻辑
     * 
     * @throws LiquibaseException 执行liquibase发生异常
     */
    public void upgrade() throws LiquibaseException {
        try (Liquibase liquibase = new Liquibase(changelogPath, resourceAccessor, getDatabase())) {
            liquibase.update("");
        } catch (LiquibaseException e) {
            throw new LiquibaseException("Update liquibase failed", e);
        }
    }

    private Database getDatabase() throws LiquibaseException {
        String urlFormat;
        String driver;
        if (isMySQL()) {
            urlFormat = "jdbc:mariadb://%s:%s/%s";
            driver = "org.mariadb.jdbc.Driver";
        } else if (isOpenGauss()) {
            urlFormat = "jdbc:postgresql://%s:%s/%s?currentSchema=%s";
            driver = "org.postgresql.Driver";
        } else {
            throw new LiquibaseException("unsupported database type! please check!");
        }
        String url = String.format(Locale.ROOT, urlFormat, sqlAlchemyIp, sqlAlchemyPort, sqlAlchemyDB,
                sqlAlchemySchema);
        DatabaseFactory.getInstance().getDatabase("");
        String sqlAlchemyPwdDecrypted;
        try {
            // 解密密码
            sqlAlchemyPwdDecrypted = this.api.decrypt(sqlAlchemyPwdEncrypted).getString();
        } catch (Exception exception) {
            throw new LiquibaseException("decrypt password failed, please check", exception);
        }
        return DatabaseFactory.getInstance().openDatabase(url, sqlAlchemyUser, sqlAlchemyPwdDecrypted, driver, null,
                null, null, resourceAccessor);
    }

    /**
     * cube场景下
     * 安装场景create_table.sh脚本调用时，拓展参数中传入的数据库类型为opengauss
     * 升级场景create_table.sh脚本以及容器启动执行flash_db.sh时，是从airflow.cfg中读取配置，为postgresql
     * 
     * @return True: 配置库是openguss； False：配置库非opengauss
     */
    private boolean isOpenGauss() {
        return "opengauss".equalsIgnoreCase(this.sqlAlchemyType) || "postgresql".equalsIgnoreCase(this.sqlAlchemyType);
    }

    private boolean isMySQL() {
        return "mysql".equalsIgnoreCase(this.sqlAlchemyType);
    }
}
