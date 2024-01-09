/*
 * 文 件 名:  ReliabilityVariableEntity.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.entity;

import com.huawei.smartcampus.datatool.utils.StringUtils;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 可靠性变量实体
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Entity
@Table(name = "dt_reliability_variable")
public class ReliabilityVariableEntity {
    private int id;
    private String varKey;
    private String varValue;

    /**
     * 构造方法
     */
    public ReliabilityVariableEntity() {
    }

    public ReliabilityVariableEntity(String varKey, String varValue) {
        this.varKey = varKey;
        this.varValue = varValue;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "var_key")
    public String getVarKey() {
        return varKey;
    }

    public void setVarKey(String varKey) {
        this.varKey = varKey;
    }

    @Basic
    @Column(name = "var_value")
    public String getVarValue() {
        return varValue;
    }

    public void setVarValue(String varValue) {
        this.varValue = varValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ReliabilityVariableEntity that = (ReliabilityVariableEntity) obj;

        if (id != that.id) {
            return false;
        }
        if (StringUtils.isNotEmpty(varKey) ? (!varKey.equals(that.varKey)) : StringUtils.isNotEmpty(that.varKey)) {
            return false;
        }
        return !(StringUtils.isNotEmpty(varValue)
                ? (!varValue.equals(that.varValue))
                : (StringUtils.isNotEmpty(that.varValue)));
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (StringUtils.isNotEmpty(varKey) ? varKey.hashCode() : 0);
        return 31 * result + (StringUtils.isNotEmpty(varKey) ? varValue.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "ReliabilityVariableEntity{" + "id='" + id + '\'' + ", varKey='" + varKey + '\'' + '}';
    }
}
