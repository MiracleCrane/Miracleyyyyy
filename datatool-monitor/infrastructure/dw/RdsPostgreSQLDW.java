/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.dw;

import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.DBType;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.DWConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RDSPOSTGRESQL数据库，基于pg12的内核，与OpenGauss（pg9）有部分差异
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/12/11]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class RdsPostgreSQLDW extends OpenGaussDW {
    private static final Logger LOGGER = LoggerFactory.getLogger(RdsPostgreSQLDW.class);

    @Override
    public DBType dbType() {
        return DBType.RDSPOSTGRESQL;
    }

    @Override
    public Long getDWMaximumSize(DWConnection dwConnection) {
        // RDSPOSTGRESQL数据库不支持spcmaxsize参数，不支持最大存储空间查询
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RDSPOSTGRESQL not support spcmaxsize, result is null.");
        }
        return null;
    }

    @Override
    public String getDWTableSizeSQL() {
        return "select t.schema, t.table, sum(t.size) as size from(\n"
                // 查询普通表的大小
                + "select i.table_schema as schema, i.table_name as table, pg_total_relation_size(p.relid) as size "
                + "from information_schema.tables i "
                + "inner join pg_stat_user_tables p on i.table_name = p.relname and i.table_schema = p.schemaname "
                + "where not exists (select 1 from pg_inherits t1 where t1.inhrelid = p.relid) \n"
                // 合并
                + "union all\n"
                // 查询分区表的大小
                + "select i.table_schema as schema, i.table_name as table, pg_total_relation_size(t1.inhrelid) as size "
                + "from pg_inherits t1 " + "inner join pg_class t2 on t1.inhparent = t2.oid "
                + "inner join pg_catalog.pg_namespace p on t2.relnamespace = p.oid "
                + "inner join information_schema.tables i on i.table_name = t2.relname and i.table_schema = p.nspname"
                + ") t group by t.schema, t.table";
    }

    @Override
    public String getDWTableRowsSQL() {
        return "select t.schema, t.table, sum(t.rownums) as rownums from(\n"
                // 查询普通表的大小
                + "select i.table_schema as schema, i.table_name as table, p.n_live_tup as rownums "
                + "from information_schema.tables i "
                + "inner join pg_stat_user_tables p on i.table_name = p.relname and i.table_schema = p.schemaname "
                + "where not exists (select 1 from pg_inherits t1 where t1.inhrelid = p.relid)\n"
                // 合并
                + "union all\n"
                // 查询分区表的大小
                + "select i.table_schema as schema, i.table_name as table, p.n_live_tup as rownums "
                + "from pg_inherits t1 " + "inner join pg_class t2 on t1.inhparent = t2.oid "
                + "inner join pg_catalog.pg_namespace t3 on t2.relnamespace = t3.oid "
                + "inner join pg_stat_user_tables p on p.relid = t1.inhrelid "
                + "inner join information_schema.tables i on i.table_name = t2.relname and i.table_schema = t3.nspname\n"
                + ") t group by t.schema, t.table";
    }
}