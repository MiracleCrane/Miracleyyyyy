/*
 * 文 件 名:  DataToolDynamicTableSinkFactory.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/9/5
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.jdbc.table.factory;

import com.huawei.dataservice.sql.connector.jdbc.table.factory.handler.Handler;
import com.huawei.dataservice.sql.connector.jdbc.table.factory.handler.HandlerExecutionChain;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.factories.DynamicTableSinkFactory;
import org.apache.flink.table.factories.DynamicTableSourceFactory;

/**
 * DataTool自定义DynamicTableSinkFactory基类，提供额外扩展点，支持在创建source和sink前定制业务逻辑
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/9/5]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public abstract class DataToolDynamicTableFactory implements DynamicTableSinkFactory, DynamicTableSourceFactory {

    private HandlerExecutionChain sinkHandlerExecutionChain = new HandlerExecutionChain();

    private HandlerExecutionChain sourceHandlerExecutionChain = new HandlerExecutionChain();

    @Override
    public final DynamicTableSink createDynamicTableSink(Context context) {
        sinkHandlerExecutionChain.doBefore(this, context);
        return doCreateDynamicTableSink(context);
    }

    @Override
    public DynamicTableSource createDynamicTableSource(Context context) {
        sourceHandlerExecutionChain.doBefore(this, context);
        return doCreateDynamicTableSource(context);
    }

    public void addSinkHandler(Handler handler) {
        sinkHandlerExecutionChain.addHandler(handler);
    }

    public void addSourceHandler(Handler handler) {
        sourceHandlerExecutionChain.addHandler(handler);
    }

    protected abstract DynamicTableSink doCreateDynamicTableSink(Context context);

    protected abstract DynamicTableSource doCreateDynamicTableSource(Context context);
}