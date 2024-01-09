/*
 * 文 件 名:  AssetListInfo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export;

/**
 * 资产清单信息
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class AssetListInfo {
    private String dataConn;
    private String database;
    private String exportTime;

    public AssetListInfo() {
    }

    public AssetListInfo(String dataConn, String database, String exportTime) {
        this.dataConn = dataConn;
        this.database = database;
        this.exportTime = exportTime;
    }

    public String getDataConn() {
        return dataConn;
    }

    public void setDataConn(String dataConn) {
        this.dataConn = dataConn;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getExportTime() {
        return exportTime;
    }

    public void setExportTime(String exportTime) {
        this.exportTime = exportTime;
    }
}