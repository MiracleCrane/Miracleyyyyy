/*
 * 文 件 名:  CreateDynamicTableAspectJ.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/9/6
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.jdbc.table.factory.handler;

import org.apache.flink.table.factories.DynamicTableFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * handler执行器
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/9/6]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class HandlerExecutionChain {
    private List<Handler> handlers = new ArrayList<>();

    /**
     * 执行handler的before动作，注意执行的顺序是，先放入list的handler会先执行before
     * 
     * @param factory 工厂类
     * @param context 上下文
     */
    public void doBefore(DynamicTableFactory factory, DynamicTableFactory.Context context) {
        for (int i = 0; i < handlers.size(); i++) {
            handlers.get(i).before(factory, context);
        }
    }

    public void addHandler(Handler around) {
        handlers.add(around);
    }
}