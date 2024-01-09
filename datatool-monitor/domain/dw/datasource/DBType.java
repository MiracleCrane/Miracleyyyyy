/*
 * 文 件 名:  DBConnectType.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.datasource;

/**
 * 数仓的数据库类型
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/16]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public enum DBType {
    OPENGAUSS("opengauss", "org.postgresql.Driver", "jdbc:postgresql://%s:%s/%s"),
    RDSPOSTGRESQL("rdspostgresql", "org.postgresql.Driver", "jdbc:postgresql://%s:%s/%s");

    private String value;

    private String driver;

    private String url;

    DBType(String value, String driver, String url) {
        this.value = value;
        this.driver = driver;
        this.url = url;
    }

    public String value() {
        return value;
    }

    public String driver() {
        return driver;
    }

    public String url() {
        return url;
    }
}
