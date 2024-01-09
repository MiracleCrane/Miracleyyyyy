/*
 * 文 件 名:  OpenGaussDialectFactory.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  w00318695
 * 修改时间： 2022/9/28
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.opengauss.dialect;

import org.apache.flink.connector.jdbc.dialect.JdbcDialect;
import org.apache.flink.connector.jdbc.dialect.JdbcDialectFactory;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author w00318695
 * @version [SmartCampus V100R001C00, 2022/9/28]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class OpenGaussDialectFactory implements JdbcDialectFactory {
    @Override
    public boolean acceptsURL(String url) {
        return url.startsWith("jdbc:postgresql:");
    }

    @Override
    public JdbcDialect create() {
        return new OpenGaussDialect();
    }
}
