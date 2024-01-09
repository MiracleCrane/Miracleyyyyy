/*
 * 文 件 名:  AlarmAction.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action;

import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;

/**
 * 告警规则中要执行的动作
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/1]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public interface AlarmAction {

    /**
     * 执行动作
     * 
     * @param alarm 告警的实例
     */
    void execute(Alarm alarm);
}