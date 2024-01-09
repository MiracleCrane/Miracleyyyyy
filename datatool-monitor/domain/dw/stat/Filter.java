/*
 * 文 件 名:  Filter.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.stat;

/**
 * 过滤器接口
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/29]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public interface Filter<T> {
    /**
     * 排除符合条件的元素
     * 
     * @param item 被过滤元素
     * @return true/false
     */
    boolean exclude(T item);
}