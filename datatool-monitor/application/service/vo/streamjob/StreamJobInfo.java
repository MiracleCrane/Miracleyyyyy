/*
 * 文 件 名:  StreamJobInfo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/13
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.streamjob;

/**
 * 流处理作业信息
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/13]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class StreamJobInfo {
    private String id;
    private String name;
    private String status;

    public StreamJobInfo() {
    }

    public StreamJobInfo(String id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}