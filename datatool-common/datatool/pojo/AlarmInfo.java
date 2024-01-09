/*
 * 文 件 名:  AlarmInfo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  cWX630741
 * 修改时间： 2021/3/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.pojo;

/**
 * 告警信息
 *
 * @author cWX630741
 * @version [SmartCampus V100R001C00, 2021/3/18]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class AlarmInfo {
    private String alarmLevel;
    private String alarmDesc;
    private String alarmId;
    private boolean manualClear;
    private String alarmStatus;

    public String getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmDesc() {
        return alarmDesc;
    }

    public void setAlarmDesc(String alarmDesc) {
        this.alarmDesc = alarmDesc;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public boolean isManualClear() {
        return manualClear;
    }

    public void setManualClear(boolean manualClear) {
        this.manualClear = manualClear;
    }

    public String getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    @Override
    public String toString() {
        return "AlarmInfo{" +
                "alarmLevel='" + alarmLevel + '\'' +
                ", alarmDesc='" + alarmDesc + '\'' +
                ", alarmId='" + alarmId + '\'' +
                ", manualClear=" + manualClear +
                ", alarmStatus='" + alarmStatus + '\'' +
                '}';
    }
}
