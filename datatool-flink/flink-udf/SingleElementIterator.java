/*
 * 文 件 名:  SingleElementIterator.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  w00318695
 * 修改时间： 2022/10/9
 * 修改内容:  <新增>
 */

package org.apache.flink.table.planner.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author w00318695
 * @version [SmartCampus V100R001C00, 2022/10/9]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public final class SingleElementIterator<E> implements Iterator<E>, Iterable<E> {
    private E current;
    private boolean available = false;

    /**
     * Resets the element. After this call, the iterator has one element available,
     * which is the
     * given element.
     *
     * @param current The element to make available to the iterator.
     */
    public void set(E current) {
        this.current = current;
        this.available = true;
    }

    @Override
    public boolean hasNext() {
        return available;
    }

    @Override
    public E next() {
        if (available) {
            available = false;
            return current;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        available = true;
        return this;
    }
}
