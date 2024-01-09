/*
 * 文 件 名:  AlarmCenterGateway.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/4/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.auditlog.gateway;

import com.huawei.hicampus.campuscommon.auditlog.entity.AuditLog;

import java.util.List;

/**
 * 审计日志服务的代理
 *
 * @author g00560618
 * @version [Campus Core 23.0, 2023/4/2]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public interface AuditLogGateway {
    /**
     * 记录单条审计日志
     *
     * @param auditLog 审计日志信息
     * @return 是否成功记录
     */
    boolean sendAuditLog(AuditLog auditLog);

    /**
     * 批量记录审计日志
     *
     * @param auditLogs 审计日志信息
     * @return 是否成功记录
     */
    boolean sendBatchAuditLog(List<AuditLog> auditLogs);
}