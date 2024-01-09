/*
 * 文 件 名:  DWStorage.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/28
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.stat;

/**
 * 数据仓库整体存储空间信息
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/19]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWStorage {
    /**
     * 数据库已经使用的存储空间大小，单位Byte
     */
    private Long usedSize;

    /**
     * 数据仓库最大可使用存储空间大小，单位Byte
     * 由于实际情况数据库磁盘还有日志等文件，最大使用存储空间不一定能完全用于表存储
     * 由于最大存储空间有可能出现没有配置的情况，所以在没有配时返回null
     */
    private Long maximumSize;

    public Long getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(Long usedSize) {
        this.usedSize = usedSize;
    }

    public Long getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(Long maximumSize) {
        this.maximumSize = maximumSize;
    }
}