/*
 * 文 件 名:  DWTier.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw;

import java.util.Locale;

/**
 * 数仓的分层，dm专题库层，dwr主题库层，dwi专题库层
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/16]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public enum DWTier {
    // 贴源层
    DWI("dwi"),
    // 主题库
    DWR("dwr"),
    // 专题库
    DM("dm"),
    // 其他不属于以上的分层
    OTHER("other");

    private String value;

    DWTier(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static DWTier from(String tier) {
        if (tier == null) {
            return OTHER;
        }
        for (DWTier item : DWTier.values()) {
            if (item.value().equals(tier.toLowerCase(Locale.ROOT))) {
                return item;
            }
        }
        return OTHER;
    }
}