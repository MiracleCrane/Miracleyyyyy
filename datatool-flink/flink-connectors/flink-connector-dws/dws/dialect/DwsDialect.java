/*
 * 文 件 名:  DwsDialect.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30009142
 * 修改时间： 2022/01/06
 * 修改内容:  <新增>
 */

package com.huawei.dataservice.sql.connector.dws.dialect;

import com.huawei.dataservice.sql.connector.gaussdb.table.AbstractGaussBaseDialect;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * dws dialect
 *
 * @author l30009142
 * @version [SmartCampus V100R001C00, 2021/10/15]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class DwsDialect extends AbstractGaussBaseDialect {
    private static final long serialVersionUID = 1478686313309763421L;

    /**
     * dialect name
     */
    public static final String DIALECT_NAME = "dws";

    @Override
    public Optional<String> defaultDriverName() {
        return Optional.of("org.postgresql.Driver");
    }

    @Override
    public String dialectName() {
        return DIALECT_NAME;
    }

    @Override
    public String getUpdateStatement(String tableName, String[] fieldNames, String[] conditionFields) {
        String setClause = Arrays.stream(fieldNames)
                // 过滤主键字段
                .filter((String name) -> {
                    for (String conditionField : conditionFields) {
                        if (name.equals(conditionField)) {
                            // false会被过滤
                            return false;
                        }
                    }
                    return true;
                }).map(field -> String.format(Locale.ROOT, "%s = :%s", quoteIdentifier(field), field))
                .collect(Collectors.joining(", "));
        String conditionClause = Arrays.stream(conditionFields)
                .map(field -> String.format(Locale.ROOT, "%s = :%s", quoteIdentifier(field), field))
                .collect(Collectors.joining(" AND "));
        return "UPDATE " + quoteIdentifier(tableName) + " SET " + setClause + " WHERE " + conditionClause;
    }
}
