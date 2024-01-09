/*
 * 文 件 名:  FlinkTask.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  vo类
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo.flink;

/**
 * 定义vo
 * flink 任务的属性
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class FlinkTask {
    private String taskId;
    private String name;
    private int parallelism;
    private String status;
    private long startTime;
    private long endTime;
    private int duration;
    private String writeRecords;
    private String writeBytes;
    private String readRecords;
    private String readBytes;
    private String backpressure;

    /**
     * 无参构造
     */
    public FlinkTask() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getParallelism() {
        return parallelism;
    }

    public void setParallelism(int parallelism) {
        this.parallelism = parallelism;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getWriteRecords() {
        return writeRecords;
    }

    public void setWriteRecords(String writeRecords) {
        this.writeRecords = writeRecords;
    }

    public String getWriteBytes() {
        return writeBytes;
    }

    public void setWriteBytes(String writeBytes) {
        this.writeBytes = writeBytes;
    }

    public String getReadRecords() {
        return readRecords;
    }

    public void setReadRecords(String readRecords) {
        this.readRecords = readRecords;
    }

    public String getReadBytes() {
        return readBytes;
    }

    public void setReadBytes(String readBytes) {
        this.readBytes = readBytes;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getBackpressure() {
        return backpressure;
    }

    public void setBackpressure(String backpressure) {
        this.backpressure = backpressure;
    }
}
