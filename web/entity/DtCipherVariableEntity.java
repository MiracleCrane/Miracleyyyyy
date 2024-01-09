/*
 * 文 件 名:  CipherVariablesEntity.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2021/2/9
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 密码箱变量实体
 *
 * @author z00569896
 * @version [SmartCampus V100R001C00, 2021/2/9]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Entity
@Table(name = "dt_cipher_variable")
@EntityListeners(AuditingEntityListener.class)
public class DtCipherVariableEntity {
    private String id;
    private String key;
    private String value;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "`key`")
    public String getKey() {
        return key;
    }

    public void setKey(String cipherKey) {
        this.key = cipherKey;
    }

    @Column(name = "`value`")
    public String getValue() {
        return value;
    }

    public void setValue(String cipherValue) {
        this.value = cipherValue;
    }

    @Column(name = "created_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    @CreatedDate
    public Timestamp getCreatedDate() {
        return this.createdDate != null ? new Timestamp(this.createdDate.getTime()) : null;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "last_modified_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @LastModifiedDate
    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate != null ? new Timestamp(lastModifiedDate.getTime()) : null;
    }

    @CreatedBy
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @LastModifiedBy
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DtCipherVariableEntity that = (DtCipherVariableEntity) obj;
        return Objects.equals(id, that.id) && Objects.equals(key, that.key) && Objects.equals(value, that.value)
                && Objects.equals(createdDate, that.createdDate)
                && Objects.equals(lastModifiedDate, that.lastModifiedDate) && Objects.equals(createdBy, that.createdBy)
                && Objects.equals(lastModifiedBy, that.lastModifiedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key, value, createdDate, lastModifiedDate, createdBy, lastModifiedBy);
    }

    @Override
    public String toString() {
        return "CipherVariableEntity{" + "id='" + id + '\'' + ", cipherKey='" + key + '\'' + '}';
    }
}
