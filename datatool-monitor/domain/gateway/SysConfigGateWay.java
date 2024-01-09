/*
 * 文 件 名:  SysConfigGateWay.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/10/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.gateway;

import com.huawei.smartcampus.datatool.entity.DtConnectionEntity;
import com.huawei.smartcampus.datatool.monitor.domain.dw.connector.DWConnInfo;

/**
 * 数据库概览服务
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public interface SysConfigGateWay {
    DtConnectionEntity getConnInfo(String id);

    Object getConfig(String name, String key);

    <T> T getConfig(String name, String key, Class<T> clazz);

    /**
     * 获取概览配置中监控的数据连接信息，然后在通过connId查询数据连接信息，进行补齐，如果全部或某些字段值获取不到，字段值需要填null
     *
     * @return 连接信息
     */
    DWConnInfo getMonitorDBInfo();
}