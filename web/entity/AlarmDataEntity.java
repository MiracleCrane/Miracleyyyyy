/*
 * 文 件 名:  ApiAccessDataEntity.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  yWX895180
 * 修改时间： 2021/7/30
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.entity;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author cwx630741
 * @version [SmartCampus V100R001C00, 2021/7/30]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Entity
@IdClass(AlarmData.class)
@Table(name = "dt_alarm_data")
public class AlarmDataEntity {
    @Id
    private String id;

    @Id
    private String alarmId;

    private String name;

    private String hostname;

    private Date createDate;

    /**
     * 无参构造
     */
    public AlarmDataEntity() {}

    public AlarmDataEntity(String apiId, String alarmId, String name, Date createDate) {
        this.id = apiId;
        this.alarmId = alarmId;
        this.name = name;
        this.createDate = createDate != null ? new Date(createDate.getTime()) : null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "hostname")
    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Basic
    @Column(
            name = "create_date",
            insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            updatable = false)
    @Generated(GenerationTime.INSERT)
    public Date getCreateDate() {
        return this.createDate != null ? new Date(this.createDate.getTime()) : null;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate != null ? new Date(createDate.getTime()) : null;
    }
}
