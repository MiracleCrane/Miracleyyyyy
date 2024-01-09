/*
 * 文 件 名:  MqsConnectorRuntimeException.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  23.0.0
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2023/3/16
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.mqs.exp;

/**
 * Mqs连接器运行异常
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2023/3/16]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class MqsConnectorRuntimeException extends RuntimeException {
    /**
     * Mqs连接器运行异常
     *
     * @param msg 异常描述
     */
    public MqsConnectorRuntimeException(String msg) {
        super(msg);
    }

    /**
     * Mqs连接器运行异常
     *
     * @param msg 异常描述
     * @param error 原始异常
     */
    public MqsConnectorRuntimeException(String msg, Throwable error) {
        super(msg, error);
    }
}