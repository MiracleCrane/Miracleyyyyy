/*
 * 文 件 名:  DataSourceDriverEnum.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.enums;

import java.util.Locale;

/**
 * 数据库驱动
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/11/14]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public enum DataSourceDriverEnum {
    OPENGAUSS("org.postgresql.Driver", "jdbc:postgresql://%s:%s/%s"),
    RDSPOSTGRESQL("org.postgresql.Driver", "jdbc:postgresql://%s:%s/%s");
    private String driver;
    private String url;

    DataSourceDriverEnum(String driver, String url) {
        this.driver = driver;
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl(String host, int port, String database) {
        return String.format(Locale.ROOT, url, host, port, database);
    }
}