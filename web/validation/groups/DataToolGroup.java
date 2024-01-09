/*
 * 文 件 名:  DataToolGroupSeq.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/10/19
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.validation.groups;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 * 自定义group顺序,Default是第一个执行的
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/10/19]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
@GroupSequence({Default.class, DataToolGroup.Second.class})
public interface DataToolGroup {
    interface Second {
    }
}