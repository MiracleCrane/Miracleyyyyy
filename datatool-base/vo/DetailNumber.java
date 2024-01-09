/*
 * 文 件 名:  DetailNumber.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/24
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.vo;

/**
 * 获取成功数、失败数、跳过数
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/24]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class DetailNumber {
    private long success;
    private long failed;
    private long ignored;

    public DetailNumber(long success, long failed, long ignored) {
        this.success = success;
        this.failed = failed;
        this.ignored = ignored;
    }

    public long getSuccess() {
        return success;
    }

    public void setSuccess(long success) {
        this.success = success;
    }

    public long getFailed() {
        return failed;
    }

    public void setFailed(long failed) {
        this.failed = failed;
    }

    public long getIgnored() {
        return ignored;
    }

    public void setIgnored(long ignored) {
        this.ignored = ignored;
    }
}