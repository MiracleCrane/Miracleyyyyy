/*
 * 文 件 名:  UsageAmount.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/27
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export;

/**
 * 使用数量
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/27]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class UsageAmount {
    private int used;
    private int unused;

    public UsageAmount() {
    }

    public UsageAmount(int used, int unused) {
        this.used = used;
        this.unused = unused;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getUnused() {
        return unused;
    }

    public void setUnused(int unused) {
        this.unused = unused;
    }
}