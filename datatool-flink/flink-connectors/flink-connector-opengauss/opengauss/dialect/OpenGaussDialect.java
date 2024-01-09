/*
 * 文 件 名:  OpenGaussDialect.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2021/10/15
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.opengauss.dialect;

import com.huawei.dataservice.sql.connector.gaussdb.table.AbstractGaussBaseDialect;

import java.util.Optional;

/**
 * openGauss dialect
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2021/10/15]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class OpenGaussDialect extends AbstractGaussBaseDialect {
    private static final long serialVersionUID = -7708002701942035704L;

    /**
     * dialect name
     */
    public static final String DIALECT_NAME = "openGauss";

    @Override
    public Optional<String> defaultDriverName() {
        return Optional.of("org.postgresql.Driver");
    }

    @Override
    public String dialectName() {
        return DIALECT_NAME;
    }
}
