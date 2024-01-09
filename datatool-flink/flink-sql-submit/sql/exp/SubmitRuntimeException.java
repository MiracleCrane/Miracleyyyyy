/*
 * 文 件 名:  SubmitRuntimeException.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  Flink任务提交自定义异常
 * 修 改 人:  w00318695
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.exp;

/**
 * Flink任务提交自定义异常
 * Flink任务提交自定义异常，运行时异常。
 *
 * @author w00318695
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @since [SmartCampus V100R001C00]
 */
public class SubmitRuntimeException extends RuntimeException {
    /**
     * 提交运行时异常
     *
     * @param message 信息
     */
    public SubmitRuntimeException(String message) {
        super(message);
    }
}
