/*
 * 文 件 名:  AlarmDataCountInfo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmdata;

/**
 * 告警数据数量信息
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/16]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class AlarmDataCountInfo {
    private String alarmName;
    private long count;

    /**
     * 无参构造
     */
    public AlarmDataCountInfo() {
    }

    public AlarmDataCountInfo(String alarmName, long count) {
        this.alarmName = alarmName;
        this.count = count;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AlarmDataCountInfo{" + "alarmName: " + alarmName + ", count: " + count + "}";
    }
}