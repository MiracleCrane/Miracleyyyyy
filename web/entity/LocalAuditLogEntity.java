/*
 * 文 件 名:  ApiAlarmData.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  cWX630741
 * 修改时间： 2021/10/21
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.entity;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <联合主键类>
 * <功能详细描述>
 *
 * @author cWX630741
 * @version [SmartCampus V100R001C00, 2021/10/21]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Entity
@Table(name = "dt_local_audit_log")
public class LocalAuditLogEntity {
    private Long id;

    private String uniqueId;

    private Timestamp createdDate;

    private String content;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setCreatedDate(Timestamp createDate) {
        this.createdDate = createDate;
    }

    @Column(name = "unique_id")
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "created_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    @Generated(GenerationTime.INSERT)
    public Timestamp getCreatedDate() {
        return createdDate;
    }
}
