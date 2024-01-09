/*
 * 文 件 名:  BusinessDbRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.gateway;

import com.huawei.smartcampus.datatool.entity.DtConnectionEntity;
import com.huawei.smartcampus.datatool.entity.DtTableUsageRecordEntity;
import com.huawei.smartcampus.datatool.enums.OverviewDbKeysEnum;
import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;
import com.huawei.smartcampus.datatool.enums.SysConfigSuffixesKeysEnum;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.domain.overview.AssetOriginEnum;
import com.huawei.smartcampus.datatool.monitor.domain.overview.TableDetailVo;
import com.huawei.smartcampus.datatool.monitor.domain.overview.TableStateEnum;
import com.huawei.smartcampus.datatool.monitor.infrastructure.datasource.BusinessDatabaseGateway;
import com.huawei.smartcampus.datatool.repository.DtTableRecordRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 业务库方法
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/11/16]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Component
public class BusinessDbRepository implements BusinessDbInfoGateway {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessDbRepository.class);

    private static final int MAX_INTERVAL_DAYS = 33;

    private static final String TABLE_DETAIL_SQL = "select  pgd.description as \"domain\",i.table_schema as \"schema\", i.table_name as \"table_name\", a.attrelid as table_id,d.description as table_name_cn, a.attname as field, col_description(a.attrelid, a.attnum) as field_description, i.data_type as field_type, a.attnum = any(p.partkey) as is_part_key, case when con.contype = 'p' then true else false end as is_primary_key from pg_class as c left join pg_attribute as a on a.attrelid = c.oid left join information_schema.columns as i on i.table_name = c.relname and i.column_name = a.attname left join pg_namespace as n on c.relnamespace = n.oid and i.table_schema = n.nspname left join pg_partition as p on p.relname = c.relname and p.parentid = c.oid left join pg_constraint AS con on con.conrelid = c.oid and a.attnum = any(con.conkey) left join pg_description d on c.oid = d.objoid and d.objsubid=0 left join pg_description pgd on n.oid = pgd.objoid where table_schema like 'dm\\_%' escape '\\' or table_schema like 'dwi\\_%' escape '\\' or table_schema like 'dwr\\_%' escape '\\' order by i.table_schema asc,i.table_name asc,a.attnum asc;";

    private static final String TABLE_DETAIL_RDS_SQL = "select  pgd.description as \"domain\",i.table_schema as \"schema\", i.table_name as \"table_name\", a.attrelid as table_id,d.description as table_name_cn, a.attname as field, col_description(a.attrelid, a.attnum) as field_description, i.data_type as field_type,position(a.attname in pg_get_partkeydef(c.oid)) > 0 as is_part_key, case when con.contype = 'p' then true else false end as is_primary_key from pg_class as c left join pg_attribute as a on a.attrelid = c.oid left join information_schema.columns as i on i.table_name = c.relname and i.column_name = a.attname left join pg_namespace as n on c.relnamespace = n.oid and i.table_schema = n.nspname left join pg_partitioned_table as p on  p.partrelid = c.oid left join pg_constraint AS con on con.conrelid = c.oid and a.attnum = any(con.conkey) left join pg_description d on c.oid = d.objoid and d.objsubid=0 left join pg_description pgd on n.oid = pgd.objoid where table_schema like 'dm_%' escape '\\' or table_schema like 'dwi_%' escape '\\' or table_schema like 'dwr_%' escape '\\' order by i.table_schema asc,i.table_name asc,a.attnum asc;";

    private static final String VERSION_SQL = "select version();";

    private static final String TABLE_QUERY_RECORD_SQL = "select relid as table_id, relname as table_name, schemaname as schema_name,coalesce(idx_tup_fetch, 0) + coalesce(seq_tup_read, 0) as total_tup_read from pg_stat_user_tables where schemaname like 'dm_%' escape '\\'  or schemaname like 'dwi_%' escape '\\' or schemaname like 'dwr_%' escape '\\';";

    private static final String ALL_SCHEMA_QUERY_SQL = "SELECT nspname as schema, obj_description(pg_namespace.oid) AS domain FROM pg_namespace where nspname like 'dm_%' escape '\\' or nspname like 'dwi_%' escape '\\' or nspname like 'dwr_%' escape '\\';";

    private static final String KEY_SEP = "##";

    @Autowired
    private SysConfigGateWay sysConfigGateWay;

    @Autowired
    private BusinessDatabaseGateway businessDatabaseGateway;

    @Autowired
    private DtTableRecordRepository dtTableRecordRepository;

    @Override
    public List<TableDetailVo> getAllTableSchema() {
        List<TableDetailVo> details = new ArrayList<>();
        try (Connection conn = getConnBySysConfig();
                PreparedStatement pstmt = conn.prepareStatement(ALL_SCHEMA_QUERY_SQL);
                ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                TableDetailVo detail = new TableDetailVo();
                detail.setSchema(resultSet.getString("schema"));
                detail.setDomain(resultSet.getString("domain"));
                details.add(detail);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to execute the SQL: {}.", ALL_SCHEMA_QUERY_SQL);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_DATABASE_CONNECTION_ERROR);
        }
        Set<String> suffixes = new HashSet<>();
        List<String> customConfig = sysConfigGateWay.getConfig(SysConfigNamesEnum.CUSTOM_FLAG.value(),
                SysConfigSuffixesKeysEnum.SUFFIX.value(), List.class);
        if (customConfig != null) {
            for (String suffix : customConfig) {
                suffixes.add(suffix);
            }
        }
        processCustom(suffixes, details);
        return details;
    }

    @Override
    public List<TableDetailVo> getTableDetail() {
        List<TableDetailVo> details = new ArrayList<>();
        // rds的pg版本>=10,分区表字段标识查询方法不同。
        try (Connection conn = getConnBySysConfig()) {
            if (getVersion(conn) >= 10) {
                executeQuery(conn, TABLE_DETAIL_RDS_SQL, details);
            } else {
                executeQuery(conn, TABLE_DETAIL_SQL, details);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to connect to database.");
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_DATABASE_CONNECTION_ERROR);
        }
        Set<String> suffixes = new HashSet<>();
        List<String> customConfig = sysConfigGateWay.getConfig(SysConfigNamesEnum.CUSTOM_FLAG.value(),
                SysConfigSuffixesKeysEnum.SUFFIX.value(), List.class);
        if (customConfig != null) {
            for (String suffix : customConfig) {
                suffixes.add(suffix);
            }
        }
        processCustom(suffixes, details);
        return details;
    }

    private void executeQuery(Connection connection, String sql, List<TableDetailVo> details) {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    TableDetailVo detail = new TableDetailVo();
                    detail.setSchema(resultSet.getString("schema"));
                    detail.setTableName(resultSet.getString("table_name"));
                    detail.setTableNameCN(resultSet.getString("table_name_cn"));
                    detail.setField(resultSet.getString("field"));
                    detail.setFieldDescription(resultSet.getString("field_description"));
                    detail.setFieldType(resultSet.getString("field_type"));
                    detail.setIsPartKey(resultSet.getBoolean("is_part_key"));
                    detail.setIsPrimaryKey(resultSet.getBoolean("is_primary_key"));
                    detail.setDomain(resultSet.getString("domain"));
                    detail.setTableId(resultSet.getLong("table_id"));
                    details.add(detail);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to execute the SQL: {}.", sql);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_DATABASE_CONNECTION_ERROR);
        }
    }

    @Override
    public void processTableState(List<TableDetailVo> details) {
        Set<Long> tableIds = details.stream().map(TableDetailVo::getTableId).collect(Collectors.toSet());
        List<DtTableUsageRecordEntity> historyRecord = dtTableRecordRepository.findByTableIdIn(tableIds);
        if (historyRecord.isEmpty()) {
            recordRealTimeTableRecord();
        }
        Map<Long, TableStateEnum> tableStateMap = new HashMap<>();
        Date currentTime = new Date();
        // 先计算表的使用情况
        for (DtTableUsageRecordEntity record : historyRecord) {
            long internalDays = (currentTime.getTime() - record.getLastModifiedDate().getTime())
                    / (24 * 60 * 60 * 1000);
            // 新建表查询记录0是未使用的
            // 有使用记录但是大于33天也是未使用的
            boolean used = record.getTotalTupRead() != null && record.getTotalTupRead() > 0
                    && internalDays <= (MAX_INTERVAL_DAYS - 1);
            tableStateMap.put(record.getTableId(), used ? TableStateEnum.USED : TableStateEnum.UNUSED);
        }

        // 记录分区表
        Set<Long> partTableSet = new HashSet<>();
        for (TableDetailVo detail : details) {
            if (detail.getPartKey()) {
                partTableSet.add(detail.getTableId());
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Partition tableId: {}.", partTableSet);
        }
        for (TableDetailVo detail : details) {
            if (!partTableSet.isEmpty() && partTableSet.contains(detail.getTableId())) {
                // 如果是分区表，无法确认使用状态
                detail.setUsed(TableStateEnum.UNKNOWN);
                continue;
            }
            TableStateEnum isUsed = tableStateMap.get(detail.getTableId());
            if (isUsed == null) {
                // 没有记录说明是新表
                detail.setUsed(TableStateEnum.UNUSED);
            } else {
                detail.setUsed(isUsed);
            }
        }
    }

    private List<DtTableUsageRecordEntity> getRealTimeTableRecord(Connection connection) {
        List<DtTableUsageRecordEntity> records = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(TABLE_QUERY_RECORD_SQL);
                ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                DtTableUsageRecordEntity record = new DtTableUsageRecordEntity();
                record.setTableId(resultSet.getLong("table_id"));
                record.setTotalTupRead(resultSet.getLong("total_tup_read"));
                record.setTableName(resultSet.getString("table_name"));
                record.setSchemaName(resultSet.getString("schema_name"));
                records.add(record);
            }
        } catch (SQLException ex) {
            LOGGER.error("Failed to process the resultSet of {}.", TABLE_QUERY_RECORD_SQL);
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_DATABASE_CONNECTION_ERROR);
        }
        return records;
    }

    private void processCustom(Set<String> suffixes, List<TableDetailVo> details) {
        Set<String> customTableSet = new HashSet<>();
        Map<String, Boolean> schemaMap = new HashMap<>();
        // 这里只对确定是定制的detail的source处理；如果字段是定制的，只对字段在的detail，后面再遍历的时候字段所在的整张表做补充
        for (TableDetailVo detail : details) {
            detail.setModelLayering(detail.getSchema().split("_")[0]);
            if (schemaMap.get(detail.getSchema()) == null) {
                // 如果schema带定制后缀，那么schema和里面的表都是定制的
                if (suffixes.stream().anyMatch(suffix -> detail.getSchema().endsWith(suffix))) {
                    detail.setSource(AssetOriginEnum.CUSTOM.value());
                    schemaMap.put(detail.getSchema(), true);
                    continue;
                } else {
                    // schema是基线的，里面的表还要检查
                    schemaMap.put(detail.getSchema(), false);
                }
            }
            // 如果schema是定制的，后面不用检查了
            if (schemaMap.get(detail.getSchema())) {
                detail.setSource(AssetOriginEnum.CUSTOM.value());
                continue;
            }

            // 走到这里说明schema是基线的
            // schema没有表的时候
            if (detail.getTableName() == null) {
                continue;
            }
            // 检查表和字段
            String customKey = getCustomKey(detail);
            if (customTableSet.contains(customKey)) {
                detail.setSource(AssetOriginEnum.CUSTOM.value());
                continue;
            }
            if (suffixes.stream()
                    .anyMatch(suffix -> detail.getTableName().endsWith(suffix) || detail.getField().endsWith(suffix))) {
                detail.setSource(AssetOriginEnum.CUSTOM.value());
                customTableSet.add(customKey);
            }
        }
        for (TableDetailVo detail : details) {
            if (detail.getSource() != null) {
                continue;
            }
            if (customTableSet.contains(getCustomKey(detail))) {
                detail.setSource(AssetOriginEnum.CUSTOM.value());
            } else {
                detail.setSource(AssetOriginEnum.BASELINE.value());
            }
        }
    }

    private String getCustomKey(TableDetailVo detail) {
        return detail.getSchema() + KEY_SEP + detail.getTableName();
    }

    @Override
    public Connection getConnBySysConfig() {
        String database = sysConfigGateWay.getConfig(SysConfigNamesEnum.OVERVIEW_DB.value(),
                OverviewDbKeysEnum.DATABASE.value(), String.class);
        String connId = sysConfigGateWay.getConfig(SysConfigNamesEnum.OVERVIEW_DB.value(),
                OverviewDbKeysEnum.CONNID.value(), String.class);
        // 配置是否存在
        if (database == null || connId == null) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SYSTEM_CONFIG_OVERVIEW_MISSING);
        }

        DtConnectionEntity connEntity = sysConfigGateWay.getConnInfo(connId);
        if (connEntity == null) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CONNECTION_NOT_EXIST);
        }
        return businessDatabaseGateway.getConnection(connEntity, database);
    }

    @Override
    public void recordRealTimeTableRecord() {
        try (Connection conn = getConnBySysConfig()) {
            List<DtTableUsageRecordEntity> realTimeRecord = getRealTimeTableRecord(conn);
            List<DtTableUsageRecordEntity> historyRecord = dtTableRecordRepository.findAll();
            businessDatabaseGateway.shutdown(conn);
            updateRecord(realTimeRecord, historyRecord);
        } catch (SQLException e) {
            LOGGER.error("Failed to connect to database.");
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_DATABASE_CONNECTION_ERROR);
        }
    }

    private void updateRecord(List<DtTableUsageRecordEntity> realTimeRecord,
            List<DtTableUsageRecordEntity> historyRecord) {
        // 要删除已经不存在的表记录
        Set<Long> newTableIds = realTimeRecord.stream().map(DtTableUsageRecordEntity::getTableId)
                .collect(Collectors.toSet());
        List<String> deleteIds = historyRecord.stream().filter(entity -> !newTableIds.contains(entity.getTableId()))
                .map(DtTableUsageRecordEntity::getId).collect(Collectors.toList());
        dtTableRecordRepository.deleteAllById(deleteIds);
        Map<Long, DtTableUsageRecordEntity> historyMap = historyRecord.stream()
                .collect(Collectors.toMap(DtTableUsageRecordEntity::getTableId, Function.identity()));
        List<DtTableUsageRecordEntity> updateEntities = new ArrayList<>();
        for (DtTableUsageRecordEntity newEntity : realTimeRecord) {
            DtTableUsageRecordEntity oldEntity = historyMap.get(newEntity.getTableId());
            if (oldEntity != null) {
                if (!oldEntity.getTableName().equals(newEntity.getTableName())
                        || !oldEntity.getSchemaName().equals(newEntity.getSchemaName())
                        || !Objects.equals(oldEntity.getTotalTupRead(), newEntity.getTotalTupRead())) {
                    // 更新已有记录
                    newEntity.setId(oldEntity.getId());
                    updateEntities.add(newEntity);
                }
            } else {
                // 新增记录
                updateEntities.add(newEntity);
            }
        }
        dtTableRecordRepository.saveAll(updateEntities);
    }

    private Integer getVersion(Connection connection) {
        try (PreparedStatement pstmt = connection.prepareStatement(VERSION_SQL)) {
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    String version = resultSet.getString("version");
                    String[] versionArr = version.split("\\.");
                    String firstPart = versionArr[0].substring(11);
                    return Integer.parseInt(firstPart);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to execute the SQL: {}.", VERSION_SQL);
        }
        throw new DataToolRuntimeException(ExceptionCode.DATATOOL_DATABASE_CONNECTION_ERROR);
    }
}