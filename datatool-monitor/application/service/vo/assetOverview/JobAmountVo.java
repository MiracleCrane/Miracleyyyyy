/*
 * 文 件 名:  JobAmountVo.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.application.service.vo.assetOverview;

/**
 * 作业数量响应
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/19]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class JobAmountVo {
    private int batchJobNum;
    private int streamJobNum;
    private int batchScriptNum;

    public int getBatchJobNum() {
        return batchJobNum;
    }

    public void setBatchJobNum(int batchJobNum) {
        this.batchJobNum = batchJobNum;
    }

    public int getStreamJobNum() {
        return streamJobNum;
    }

    public void setStreamJobNum(int streamJobNum) {
        this.streamJobNum = streamJobNum;
    }

    public int getBatchScriptNum() {
        return batchScriptNum;
    }

    public void setBatchScriptNum(int batchScriptNum) {
        this.batchScriptNum = batchScriptNum;
    }

    public JobAmountVo(int batchJobNum, int streamJobNum, int batchScriptNum) {
        this.batchJobNum = batchJobNum;
        this.streamJobNum = streamJobNum;
        this.batchScriptNum = batchScriptNum;
    }
}