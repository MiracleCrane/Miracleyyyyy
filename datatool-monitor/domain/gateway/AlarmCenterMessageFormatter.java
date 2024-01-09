/*
 * 文 件 名:  AlarmCenterMessageFormatter.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/6/6
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.gateway;

import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;

/**
 * 告警中心信息格式化工具
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/6/6]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public interface AlarmCenterMessageFormatter {
    /**
     * 格式化告警Id
     *
     * @param alarm 告警实例
     * @return 告警中心格式化后的告警Id
     */
    String formAlarmId(Alarm alarm);

    /**
     * 格式化告警描述
     *
     * @param alarm 告警实例
     * @param actionType 告警执行类型
     * @return 告警中心格式化后的告警描述
     */
    String formAlarmDescription(Alarm alarm, String actionType);
}