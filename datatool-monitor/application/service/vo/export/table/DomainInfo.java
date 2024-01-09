/*
 * 文 件 名:  DomainInfo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/27
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export.table;

/**
 * 领域信息
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/27]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class DomainInfo {
    private String layer;
    private String name;
    private String abbreviation;
    private String origin;
    private Integer baselineTableNum;
    private Integer customTableNum;

    public DomainInfo() {
    }

    public DomainInfo(String layer, String name, String abbreviation, String origin, int baselineTableNum, int customTableNum) {
        this.layer = layer;
        this.name = name;
        this.abbreviation = abbreviation;
        this.origin = origin;
        this.baselineTableNum = baselineTableNum;
        this.customTableNum = customTableNum;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getBaselineTableNum() {
        return baselineTableNum;
    }

    public void setBaselineTableNum(Integer baselineTableNum) {
        this.baselineTableNum = baselineTableNum;
    }

    public Integer getCustomTableNum() {
        return customTableNum;
    }

    public void setCustomTableNum(Integer customTableNum) {
        this.customTableNum = customTableNum;
    }
}