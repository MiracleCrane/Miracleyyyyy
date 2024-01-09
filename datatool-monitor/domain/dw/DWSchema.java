/*
 * 文 件 名:  DWSchema.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw;

/**
 * 表的schema模型
 * pg数据库中有schema概念
 * mysql数据库中database和schema是类似的概念，没有额外的schema的概念
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/16]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DWSchema {
    private DWTier tier;

    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DWTier getTier() {
        return tier;
    }

    public void setTier(DWTier tier) {
        this.tier = tier;
    }

    public static DWSchema from(String schemaName, String tier, String description) {
        DWSchema schema = new DWSchema();
        schema.setTier(DWTier.from(tier));
        schema.setName(schemaName);
        schema.setDescription(description);
        return schema;
    }

    public static DWSchema from(String schemaName, String tier) {
        return from(schemaName, tier, null);
    }
}