/*
 * 文 件 名:  AlarmActionExecutor.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.alarm.task;

import com.huawei.smartcampus.datatool.monitor.domain.alarm.Alarm;
import com.huawei.smartcampus.datatool.monitor.domain.alarm.rule.action.AlarmAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Set;

/**
 * 告警动作执行器
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/2]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
@Component
public class AlarmActionExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger("Alarm");

    @Transactional
    public void execute(Alarm alarm) {
        Set<AlarmAction> actions = alarm.getActions();
        for (AlarmAction action : actions) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("alarm {} action execute begin.", alarm.getRule().type());
            }
            action.execute(alarm);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("alarm {} action execute end.", alarm.getRule().type());
            }
        }
    }
}