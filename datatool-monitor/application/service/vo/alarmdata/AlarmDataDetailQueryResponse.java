/*
 * 文 件 名:  AlarmDataDetailQueryResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.alarmdata;

import java.util.List;

/**
 * 告警数据详情查询响应
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/16]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class AlarmDataDetailQueryResponse {
    private List<AlarmDataDetailInfo> alarmList;
    private long total;

    public List<AlarmDataDetailInfo> getAlarmList() {
        return alarmList;
    }

    public void setAlarmList(List<AlarmDataDetailInfo> alarmList) {
        this.alarmList = alarmList;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}