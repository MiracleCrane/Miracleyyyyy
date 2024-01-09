/*
 * 文 件 名:  AlarmServiceUtil.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/6/8
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.AlarmType;

/**
 * 告警服务工具类
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/6/8]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public final class AlarmServiceUtil {
    /**
     * 校验告警类型名称
     *
     * @param alarmTypeStr 告警类型入参
     */
    public static void checkAlarmType(String alarmTypeStr) {
        for (AlarmType alarmType : AlarmType.values()) {
            if (alarmType.alarmName().equals(alarmTypeStr)) {
                return;
            }
        }
        throw new DataToolRuntimeException(ExceptionCode.DATATOOL_ALARM_TYPE_NOT_SUPPORT, alarmTypeStr);
    }
}