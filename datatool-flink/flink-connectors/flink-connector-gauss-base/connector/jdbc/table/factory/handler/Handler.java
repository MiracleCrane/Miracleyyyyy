/*
 * 文 件 名:  CreateDynamicTableAround.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/9/5
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.jdbc.table.factory.handler;

import org.apache.flink.table.factories.DynamicTableFactory;

/**
 * CreateDynamicTableSink和CreateDynamicTableSource的扩展点
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/9/5]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public interface Handler {

    /**
     * 在create前执行的逻辑
     * 
     * @param factory 工厂类
     * @param context 上下文
     */
    void before(DynamicTableFactory factory, DynamicTableFactory.Context context);
}