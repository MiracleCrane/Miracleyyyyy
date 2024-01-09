/*
 * 文 件 名:  StreamJobStatusCountQueryResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.0
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.streamjob;

/**
 * 流作业状态数量查询响应
 *
 * @author j00826364
 * @version [Campus Core 23.0, 2023/5/18]
 * @see [相关类/方法]
 * @since [Campus Core 23.0]
 */
public class StreamJobStatusCountQueryResponse {
    private int stopped;
    private int failed;
    private int running;
    private int exceptionStopped;
    private int others;

    public StreamJobStatusCountQueryResponse() {
    }

    public StreamJobStatusCountQueryResponse(int stopped, int failed, int running, int exceptionStopped, int others) {
        this.stopped = stopped;
        this.failed = failed;
        this.running = running;
        this.exceptionStopped = exceptionStopped;
        this.others = others;
    }

    public int getStopped() {
        return stopped;
    }

    public void setStopped(int stopped) {
        this.stopped = stopped;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getRunning() {
        return running;
    }

    public void setRunning(int running) {
        this.running = running;
    }

    public int getExceptionStopped() {
        return exceptionStopped;
    }

    public void setExceptionStopped(int exceptionStopped) {
        this.exceptionStopped = exceptionStopped;
    }

    public int getOthers() {
        return others;
    }

    public void setOthers(int others) {
        this.others = others;
    }
}