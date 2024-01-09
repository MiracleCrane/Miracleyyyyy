/*
 * 文 件 名:  BatchRunningJobInstanceCountResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.batchjob;

import java.util.List;

/**
 * 近24小时作业实例运行数量响应
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/23]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class BatchRunningJobInstanceCountResponse {
    private List<String> timeList;
    private List<Long> countList;

    public List<String> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<String> timeList) {
        this.timeList = timeList;
    }

    public List<Long> getCountList() {
        return countList;
    }

    public void setCountList(List<Long> countList) {
        this.countList = countList;
    }
}