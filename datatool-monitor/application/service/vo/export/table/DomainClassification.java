/*
 * 文 件 名:  DomainClassification.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/27
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.export.table;

import java.util.ArrayList;
import java.util.List;

/**
 * 领域分类汇总
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/27]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class DomainClassification {
    List<DomainInfo> dm;
    List<DomainInfo> dwr;
    List<DomainInfo> dwi;

    public DomainClassification() {
    }

    public DomainClassification(List<DomainInfo> dm, List<DomainInfo> dwr, List<DomainInfo> dwi) {
        this.dm = new ArrayList<>(dm);
        this.dwr = new ArrayList<>(dwr);
        this.dwi = new ArrayList<>(dwi);
    }

    public List<DomainInfo> getDm() {
        return dm;
    }

    public void setDm(List<DomainInfo> dm) {
        this.dm = dm;
    }

    public List<DomainInfo> getDwr() {
        return dwr;
    }

    public void setDwr(List<DomainInfo> dwr) {
        this.dwr = dwr;
    }

    public List<DomainInfo> getDwi() {
        return dwi;
    }

    public void setDwi(List<DomainInfo> dwi) {
        this.dwi = dwi;
    }
}