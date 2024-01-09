/*
 * 文 件 名:  AlarmMsg.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/26
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm;

/**
 * 告警信息
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/26]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class AlarmMsg {
    private String id;
    private String name;
    private String alarmId;
    private String hostname;
    private String alarmLevel;
    private String alarmDesc;
    private boolean enableManualRecover;
    private String alarmAttr;

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

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

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

    public boolean isEnableManualRecover() {
        return enableManualRecover;
    }

    public void setEnableManualRecover(boolean enableManualRecover) {
        this.enableManualRecover = enableManualRecover;
    }

    public String getAlarmAttr() {
        return alarmAttr;
    }

    public void setAlarmAttr(String alarmAttr) {
        this.alarmAttr = alarmAttr;
    }
}