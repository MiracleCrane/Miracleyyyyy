/*
 * 文 件 名:  JobStatus.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.enums;

import com.huawei.smartcampus.datatool.utils.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Optional;

/**
 * 作业状态枚举类
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public enum JobStatus {
    STOPPED,
    CREATED,
    FAILED,
    RUNNING,
    FAILING,
    CANCELLING,
    CANCELED,
    FINISHED,
    RESTARTING,
    SUSPENDED,
    SAVEPOINTING,
    EXCEPTION_STOPPED,
    INITIALIZING;

    /**
     * 判断作业是否已删除
     *
     * @return 判断结果
     */
    public boolean judgeBeDeleted() {
        return this == FAILED || this == CANCELED || this == FINISHED;
    }

    /**
     * 判断状态是否为初始化、新建、运行和重启状态
     *
     * @return 判断结果
     */
    public boolean judgeRun() {
        return this == INITIALIZING || this == CREATED || this == RUNNING || this == RESTARTING;
    }

    /**
     * 判断状态为运行中
     *
     * @return 判断结果
     */
    public boolean checkRunning() {
        return this == RUNNING;
    }

    /**
     * 判断是否为可自动删除状态
     *
     * @return 判断结果
     */
    public boolean checkAutoDelete() {
        if (this == CREATED || this == FAILING || this == CANCELLING) {
            return true;
        }
        if (this == SUSPENDED || this == FAILED || this == CANCELED) {
            return true;
        }
        return this == INITIALIZING || this == FINISHED;
    }

    /**
     * 判断是否异常
     *
     * @return 判断结果
     */
    public boolean checkException() {
        return this == FAILED || this == FAILING;
    }

    /**
     * 判断是否处于停止或异常停止状态
     *
     * @return 判断结果
     */
    public boolean checkStopped() {
        return this == STOPPED || this == EXCEPTION_STOPPED;
    }

    /**
     * 字符串反序列化方法
     *
     * @param value 作业状态值
     * @return 作业状态
     */
    @JsonCreator
    public static JobStatus getJobStatus(String value) {
        JobStatus jobStatus = null;
        if (StringUtils.isNotEmpty(value)) {
            jobStatus = Enum.valueOf(JobStatus.class, value);
        }
        return Optional.ofNullable(jobStatus).orElse(null);
    }
}
