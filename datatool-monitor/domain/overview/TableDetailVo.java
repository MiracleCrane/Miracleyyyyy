/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.monitor.domain.overview;

/**
 * 表明细
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/11/14]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class TableDetailVo {
    private String modelLayering;

    private String domain;

    private String schema;

    private String source;

    private TableStateEnum used;

    private String tableName;

    private String tableNameCN;

    private String field;

    private String fieldDescription;

    private String fieldType;

    private Boolean isPartKey;

    private Boolean isPrimaryKey;

    private Long tableId;

    public Boolean getPartKey() {
        return isPartKey;
    }

    public void setPartKey(Boolean partKey) {
        isPartKey = partKey;
    }

    public Boolean getPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getModelLayering() {
        return modelLayering;
    }

    public void setModelLayering(String modelLayering) {
        this.modelLayering = modelLayering;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public TableStateEnum getUsed() {
        return used;
    }

    public void setUsed(TableStateEnum used) {
        this.used = used;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableNameCN() {
        return tableNameCN;
    }

    public void setTableNameCN(String tableNameCN) {
        this.tableNameCN = tableNameCN;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Boolean getIsPartKey() {
        return isPartKey;
    }

    public void setIsPartKey(Boolean isPartKey) {
        this.isPartKey = isPartKey;
    }

    public Boolean getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(Boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    @Override
    public String toString() {
        return "TableDetailVo{" + "modelLayering='" + modelLayering + '\'' + ", domain='" + domain + '\'' + ", schema='"
                + schema + '\'' + ", source='" + source + '\'' + ", used=" + used + ", tableName='" + tableName + '\''
                + ", tableNameCN='" + tableNameCN + '\'' + ", field='" + field + '\'' + ", fieldDescription='"
                + fieldDescription + '\'' + ", fieldType='" + fieldType + '\'' + ", isPartKey=" + isPartKey
                + ", isPrimaryKey=" + isPrimaryKey + ", tableId=" + tableId + '}';
    }
}