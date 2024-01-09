/*
 * 文 件 名:  StreamJobDetail.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job;

/**
 * 流处理作业明细
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class StreamJobDetail {
    private String domain;
    private String name;
    private String origin;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}