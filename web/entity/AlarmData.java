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

import java.io.Serializable;
import java.util.Objects;

/**
 * <联合主键类>
 * <功能详细描述>
 *
 * @author cWX630741
 * @version [SmartCampus V100R001C00, 2021/10/21]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class AlarmData implements Serializable {
    private static final long serialVersionUID = 2589766491699675794L;

    private String id;
    private String alarmId;

    /**
     * 无参构造
     */
    public AlarmData() {}

    public AlarmData(String apiId, String alarmId) {
        this.id = apiId;
        this.alarmId = alarmId;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AlarmData alarmData = (AlarmData) obj;
        return Objects.equals(id, alarmData.id) && Objects.equals(alarmId, alarmData.alarmId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, alarmId);
    }
}
