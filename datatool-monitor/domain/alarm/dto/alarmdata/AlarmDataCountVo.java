/*
 * 文 件 名:  AlarmDataCountVo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.dto.alarmdata;

/**
 * 告警数据数量类
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/16]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class AlarmDataCountVo {
    private String alarmId;
    private long count;

    public AlarmDataCountVo() {
    }

    public AlarmDataCountVo(String alarmId, long count) {
        this.alarmId = alarmId;
        this.count = count;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}