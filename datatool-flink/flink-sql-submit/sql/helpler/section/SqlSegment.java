/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

package com.huawei.dataservice.sql.helpler.section;

/**
 * sql片段类
 *
 * @since 2020-12-29 21:48:36
 */
public class SqlSegment {
    /**
     * sql语句
     */
    protected String sql;

    public SqlSegment(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return sql;
    }
}
