/*
 * 文 件 名:  BatchJobInstanceCountVo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/17
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.batchjob;

/**
 * 批处理作业实例状态数量
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/17]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class BatchJobInstanceStateCountVo {
    private String state;
    private long count;

    /**
     *  无参构造
     */
    public BatchJobInstanceStateCountVo() {
    }

    public BatchJobInstanceStateCountVo(String state, long count) {
        this.state = state;
        this.count = count;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "BatchJobInstanceStateCountVo{state: " + state + ", count: " + count + "}";
    }
}