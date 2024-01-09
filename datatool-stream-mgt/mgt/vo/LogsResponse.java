/*
 * 文 件 名:  LogsResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/12
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo;

import java.util.List;

/**
 * logs接口返回响应类，日志列表
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/12]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class LogsResponse {
    private List<LogResponse> logs;

    public List<LogResponse> getLogs() {
        return logs;
    }

    public void setLogs(List<LogResponse> logs) {
        this.logs = logs;
    }
}