/*
 * 文 件 名:  ReliablityKey.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.service.key;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public final class ReliablityKey {
    /**
     * 独立任务管理器名称
     */
    public static final String STANDALONE_TASKMANGER_NAME = "standalone_taskmanger_name";

    /**
     * 提交状态
     */
    public static final String SUBMITTED_STATE = "SUBMITTED";

    /**
     * 未提交状态
     */
    public static final String UNSUBMITTED_STATE = "UNSUBMITTED";

    private ReliablityKey() {}
}
