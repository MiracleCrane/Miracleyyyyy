/*
 * 文 件 名:  AlarmInfo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm;

import java.util.Objects;

/**
 * 告警基础信息
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/23]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class AlarmBasicInfo {
    private String id;
    private String name;
    private String extraInfo;

    public AlarmBasicInfo() {
    }

    public AlarmBasicInfo(String id, String name, String extraInfo) {
        this.id = id;
        this.name = name;
        this.extraInfo = extraInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        AlarmBasicInfo alarmBasicInfo = (AlarmBasicInfo) object;
        return Objects.equals(this.id, alarmBasicInfo.id) && Objects.equals(this.name, alarmBasicInfo.name)
                && Objects.equals(this.extraInfo, alarmBasicInfo.extraInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.extraInfo);
    }

    @Override
    public String toString() {
        return "AlarmBasicInfo{id: " + id + ", name: " + name + ", extraInfo: " + extraInfo + "}";
    }
}