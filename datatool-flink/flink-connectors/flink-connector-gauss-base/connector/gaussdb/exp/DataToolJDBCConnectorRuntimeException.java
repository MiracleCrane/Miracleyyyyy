/*
 * 文 件 名:  GaussBaseConnectorRuntimeException.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  22.2.T18.B010
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2022/8/29
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.gaussdb.exp;

/**
 * 自定义JDBC Connector异常
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/9/4]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DataToolJDBCConnectorRuntimeException extends RuntimeException {
    /**
     * 构造函数
     *
     * @param errorMsg errorMsg
     */
    public DataToolJDBCConnectorRuntimeException(String errorMsg) {
        super(errorMsg);
    }

    /**
     * 构造函数
     *
     * @param msg message
     * @param error exception
     */
    public DataToolJDBCConnectorRuntimeException(String msg, Throwable error) {
        super(msg, error);
    }
}
