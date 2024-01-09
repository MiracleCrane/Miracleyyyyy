/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

package com.huawei.dataservice.sql.udf.udagg.acc;

/**
 * 用于表示被聚合的对象
 * 用于表示被聚合的对象
 *
 * @author zwx632190
 * @version [SmartCampus V100R001C00, 2021/3/30]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 * @param <T> 泛型
 */
public class UnificationAcc<T> {
    /**
     * 定义超类
     *
     * @return 返回值T
     */
    private T obj;

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
