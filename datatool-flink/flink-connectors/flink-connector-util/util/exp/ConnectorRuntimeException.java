/*
 * 文 件 名:  ConnectorRuntimeException.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  23.0.0
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2023/3/25
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.util.exp;

/**
 * 连接器运行异常
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2023/3/25]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class ConnectorRuntimeException extends RuntimeException {
    /**
     * 连接器运行异常
     *
     * @param msg 异常描述
     */
    public ConnectorRuntimeException(String msg) {
        super(msg);
    }

    /**
     * 连接器运行异常
     *
     * @param msg 异常描述
     * @param error 原始异常
     */
    public ConnectorRuntimeException(String msg, Throwable error) {
        super(msg, error);
    }
}