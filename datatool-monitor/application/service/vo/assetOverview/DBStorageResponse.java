/*
 * 文 件 名:  DBStorageResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview;

/**
 * 数据库存储响应
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/19]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class DBStorageResponse {
    private float usedSize;
    private float idleSize;
    private float maximumSize;
    private String dmUsedSize;
    private String dwrUsedSize;

    public String getDmUsedSize() {
        return dmUsedSize;
    }

    public void setDmUsedSize(String dmUsedSize) {
        this.dmUsedSize = dmUsedSize;
    }

    public String getDwrUsedSize() {
        return dwrUsedSize;
    }

    public void setDwrUsedSize(String dwrUsedSize) {
        this.dwrUsedSize = dwrUsedSize;
    }

    public String getDwiUsedSize() {
        return dwiUsedSize;
    }

    public void setDwiUsedSize(String dwiUsedSize) {
        this.dwiUsedSize = dwiUsedSize;
    }

    private String dwiUsedSize;

    public float getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(float usedSize) {
        this.usedSize = usedSize;
    }

    public float getIdleSize() {
        return idleSize;
    }

    public void setIdleSize(float idleSize) {
        this.idleSize = idleSize;
    }

    public float getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(float maximumSize) {
        this.maximumSize = maximumSize;
    }
}