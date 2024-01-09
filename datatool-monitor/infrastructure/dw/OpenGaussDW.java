/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.dw;

import com.huawei.smartcampus.datatool.monitor.domain.dw.DWSchema;
import com.huawei.smartcampus.datatool.monitor.domain.dw.DWTable;
import com.huawei.smartcampus.datatool.monitor.domain.dw.DWTableRow;
import com.huawei.smartcampus.datatool.monitor.domain.dw.DWTableSize;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.DBType;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.DWConnection;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.DWRecordSet;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.DWGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 连接OpenGauss类型数据仓库
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/20]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class OpenGaussDW implements DWGateway {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenGaussDW.class);

    public static final String USED_SIZE_SQL = "select pg_database_size(?)";

    public static final String MAXIMUM_SIZE_SQL = "select t2.spcmaxsize from pg_database t1 join pg_tablespace t2 on t1.dattablespace = t2.oid where t1.datname = ? and spcmaxsize is not null";

    public static final String TABLE_SIZE_SQL = "select p.schemaname as schema, p.relname as table, pg_total_relation_size(p.relid) as size from information_schema.tables i inner join pg_stat_user_tables p  on i.table_name = p.relname";

    public static final String TABLE_ROWS_SQL = "select p.schemaname as schema, p.relname as table, p.n_live_tup as rownums from information_schema.tables i inner join pg_stat_user_tables p  on i.table_name = p.relname";

    @Override
    public DBType dbType() {
        return DBType.OPENGAUSS;
    }

    @Override
    public Long getDWUsedSize(DWConnection dwConnection) {
        List<Object> params = new ArrayList<>();
        params.add(dwConnection.getDwConnInfo().getDatabase());

        DWRecordSet records = dwConnection.executeQuery(getDWUsedSizeSQL(), params);
        if (!records.hasNext()) {
            LOGGER.error("query database usedsize failed, record row num is 0, may be database not exists.");
            // 异常情况记录日志，返回结果null
            return null;
        }
        records.next();

        String temp = String.valueOf(records.getColumnsValue("pg_database_size"));
        return Long.parseLong(temp);
    }

    @Override
    public Long getDWMaximumSize(DWConnection dwConnection) {
        List<Object> params = new ArrayList<>();
        params.add(dwConnection.getDwConnInfo().getDatabase());

        DWRecordSet records = dwConnection.executeQuery(getDWMaximumSizeSQL(), params);
        if (!records.hasNext()) {
            LOGGER.error("query database maximumsize failed, record row num is 0, may be spcmaxsize is not config.");
            // 异常情况记录日志，返回结果 null
            return null;
        }
        records.next();

        // 返回的格式 是 3221225472 K 这样的，需要去掉单位
        String temp = String.valueOf(records.getColumnsValue("spcmaxsize"));
        long spcmaxsize = Long.parseLong(temp.split(" ")[0]);
        // spcmaxsize的单位是 KB，需要转换成B
        return spcmaxsize * 1024;
    }

    @Override
    public List<DWTableSize> getDWTableSize(DWConnection dwConnection) {
        List<Object> params = new ArrayList<>();
        DWRecordSet records = dwConnection.executeQuery(getDWTableSizeSQL(), params);

        List<DWTableSize> result = new ArrayList<>();
        while (records.hasNext()) {
            records.next();
            // col ： schema、table、size
            String schemaName = String.valueOf(records.getColumnsValue("schema"));
            String tableName = String.valueOf(records.getColumnsValue("table"));
            long tableSize = Long.parseLong(String.valueOf(records.getColumnsValue("size")));

            // 从dwr_res等sechma中获取前缀
            String tier = schemaName.split("_")[0];
            DWSchema dwSchema = DWSchema.from(schemaName, tier);
            DWTable dwTable = DWTable.from(tableName, dwSchema);
            DWTableSize dwTableSize = DWTableSize.from(dwTable, tableSize);
            result.add(dwTableSize);
        }
        return result;
    }

    @Override
    public List<DWTableRow> getDWTableRows(DWConnection dwConnection) {
        List<Object> params = new ArrayList<>();
        DWRecordSet records = dwConnection.executeQuery(getDWTableRowsSQL(), params);

        List<DWTableRow> result = new ArrayList<>();
        while (records.hasNext()) {
            records.next();
            // col ： schema、table、rownums
            String schemaName = String.valueOf(records.getColumnsValue("schema"));
            String tableName = String.valueOf(records.getColumnsValue("table"));
            long rownum = Long.parseLong(String.valueOf(records.getColumnsValue("rownums")));

            // 从dwr_res等sechma中获取前缀
            String tier = schemaName.split("_")[0];
            DWSchema dwSchema = DWSchema.from(schemaName, tier);
            DWTable dwTable = DWTable.from(tableName, dwSchema);
            DWTableRow dwTableSize = DWTableRow.from(dwTable, rownum);
            result.add(dwTableSize);
        }
        return result;
    }

    protected String getDWUsedSizeSQL() {
        return USED_SIZE_SQL;
    }

    protected String getDWMaximumSizeSQL() {
        return MAXIMUM_SIZE_SQL;
    }

    protected String getDWTableSizeSQL() {
        return TABLE_SIZE_SQL;
    }

    protected String getDWTableRowsSQL() {
        return TABLE_ROWS_SQL;
    }
}