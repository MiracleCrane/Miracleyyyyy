/*
 * 文 件 名:  ImportHistoryResp.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/9
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.vo.resp;

import com.huawei.smartcampus.datatool.base.vo.History;

import java.util.List;

/**
 * 导入历史返回响应
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/9]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ImportHistoryResp {
    private List<History> histories;
    private long total;

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}