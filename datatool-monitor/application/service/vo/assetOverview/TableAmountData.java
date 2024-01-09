/*
 * 文 件 名:  TableAmountData.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview;

/**
 * 表数据
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/11/2]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class TableAmountData<T> {
    private T dm;
    private T dwr;
    private T dwi;

    public T getDm() {
        return dm;
    }

    public TableAmountData<T> setDm(T dm) {
        this.dm = dm;
        return this;
    }

    public T getDwr() {
        return dwr;
    }

    public TableAmountData<T> setDwr(T dwr) {
        this.dwr = dwr;
        return this;
    }

    public T getDwi() {
        return dwi;
    }

    public TableAmountData<T> setDwi(T dwi) {
        this.dwi = dwi;
        return this;
    }
}