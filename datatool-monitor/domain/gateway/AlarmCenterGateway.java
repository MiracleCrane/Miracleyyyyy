/*
 * 文 件 名:  AlarmCenterGateway.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.gateway;

import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;

/**
 * 告警中心抽象接口
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/2]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public interface AlarmCenterGateway {

    /**
     * 发送触发告警
     * 
     * @param alarm 告警的实例
     */
    void trigger(Alarm alarm);

    /**
     * 发送恢复告警
     * 
     * @param alarm 告警的实例
     */
    void recover(Alarm alarm);
}