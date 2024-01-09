/*
 * 文 件 名:  DWGateway.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.gateway;

import com.huawei.smartcampus.datatool.monitor.domain.dw.DWTableRow;
import com.huawei.smartcampus.datatool.monitor.domain.dw.DWTableSize;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.DBType;
import com.huawei.smartcampus.datatool.monitor.domain.dw.datasource.DWConnection;

import java.util.List;

/**
 * 访问数仓的接口
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/18]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public interface DWGateway {
    /**
     * 数据库类型
     * 
     * @return 返回数据库类型
     */
    DBType dbType();

    /**
     * 获取数据库已经使用的存储空间大小，单位Byte
     *
     * @param dwConnection 数仓的连接器
     * @return 存储大小
     */
    Long getDWUsedSize(DWConnection dwConnection);

    /**
     * 获取数据仓库最大可使用存储空间大小，单位Byte
     *
     * @param dwConnection 数仓的连接器
     * @return 存储大小/null
     */
    Long getDWMaximumSize(DWConnection dwConnection);

    /**
     * 获取所有属于数据仓库的表的已使用的存储空间大小，包含索引大小，单位Byte
     * 该方法需要保证返回的表都是数仓分层中的表，没有其他的表
     *
     * @param dwConnection 数仓的连接器
     * @return 存储大小
     */
    List<DWTableSize> getDWTableSize(DWConnection dwConnection);

    /**
     * 获取所有表的行数
     * 
     * @param dwConnection 数仓的连接器
     * @return 行数
     */
    List<DWTableRow> getDWTableRows(DWConnection dwConnection);
}