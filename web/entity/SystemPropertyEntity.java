/*
 * 文 件 名:  SystemPropertiesEntity.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  w00318695
 * 修改时间： 2022/6/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.entity;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统变量实体
 * <功能详细描述>
 *
 * @author w00318695
 * @version [SmartCampus V100R001C00, 2022/6/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Entity
@Table(name = "dt_system_properties")
public class SystemPropertyEntity {
    private String id;
    private String name;
    private String description;
    private String confirmTips;
    private String range;
    private SystemPropertyCategoryEnum category;
    private SystemPropertyDataTypeEnum dataType;
    private String currentValue;
    private String defaultValue;
    private Date lastModifiedDate;
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

    @Basic
    @Column(name = "name", unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description", columnDefinition = "text")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "confirm_tips", columnDefinition = "text")
    public String getConfirmTips() {
        return confirmTips;
    }

    public void setConfirmTips(String confirmTips) {
        this.confirmTips = confirmTips;
    }

    @Basic
    @Column(name = "range")
    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    public SystemPropertyCategoryEnum getCategory() {
        return category;
    }

    public void setCategory(SystemPropertyCategoryEnum category) {
        this.category = category;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type")
    public SystemPropertyDataTypeEnum getDataType() {
        return dataType;
    }

    public void setDataType(SystemPropertyDataTypeEnum dataType) {
        this.dataType = dataType;
    }

    @Basic
    @Column(name = "current_value")
    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    @Basic
    @Column(name = "default_value")
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Basic
    @Column(name = "last_modified_date")
    public Date getLastModifiedDate() {
        return this.lastModifiedDate != null ? new Date(this.lastModifiedDate.getTime()) : null;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate != null ? new Date(lastModifiedDate.getTime()) : null;
    }

    @Basic
    @Column(name = "last_modified_by")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
