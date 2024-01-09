/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.dw;

/**
 * 数据库存储使用信息
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DBStorage {
    /*
     * 数据库已使用大小，单位MB
     */
    private String usedSize;

    /*
     * 数据库空闲大小，单位MB
     */
    private String idleSize;

    /*
     * 数据库最大可用大小，单位MB
     */
    private String maximumSize;

    /**
     * dm层已使用大小，单位MB
     */
    private String dmUsedSize;

    /*
     * dwr层已使用大小，单位MB
     */
    private String dwrUsedSize;

    /*
     * dwi层已使用大小，单位MB
     */
    private String dwiUsedSize;

    public String getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(String usedSize) {
        this.usedSize = usedSize;
    }

    public String getIdleSize() {
        return idleSize;
    }

    public void setIdleSize(String idleSize) {
        this.idleSize = idleSize;
    }

    public String getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(String maximumSize) {
        this.maximumSize = maximumSize;
    }

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
}