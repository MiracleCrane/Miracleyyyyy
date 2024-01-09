/*
 * 文 件 名:  AlarmProperties.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/4/21
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.common.properties;

/**
 * 告警属性
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/4/21]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class AlarmProperty {
    private boolean alarmEnable;
    private int alarmInterval;

    public AlarmProperty(boolean alarmEnable, int alarmInterval) {
        this.alarmEnable = alarmEnable;
        this.alarmInterval = alarmInterval;
    }

    public boolean isAlarmEnable() {
        return alarmEnable;
    }

    public void setAlarmEnable(boolean alarmEnable) {
        this.alarmEnable = alarmEnable;
    }

    public int getAlarmInterval() {
        return alarmInterval;
    }

    public void setAlarmInterval(int alarmInterval) {
        this.alarmInterval = alarmInterval;
    }
}