/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

package com.huawei.dataservice.sql.utils;

import com.huawei.dataservice.sql.exp.SubmitRuntimeException;
import com.huawei.dataservice.sql.helpler.section.SqlSegment;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * 用于从sql文件解析出sql可执行对象
 *
 * @since 2020-12-30 10:22:31
 */
public final class SqlParser {
    private SqlParser() {
    }

    /**
     * 从sql文件解析出所有sql片段
     *
     * @param filePath sql文件路径
     * @return list {@link SqlSegment}
     */
    public static List<SqlSegment> getSqlListFromFile(String filePath) {
        List<SqlSegment> sqls = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(filePath),
                StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                // 忽略注释行
                if (line.trim().startsWith("--") || line.trim().isEmpty()) {
                    continue;
                }
                sql.append(line).append(System.getProperty("line.separator"));
                if (line.trim().endsWith(";")) {
                    String trimSql = sql.toString().trim();
                    sqls.add(new SqlSegment(trimSql.substring(0, trimSql.length() - 1)));
                    sql.setLength(0);
                }
            }
        } catch (FileNotFoundException e) {
            throw new SubmitRuntimeException("Sql file is not exist, pleas check!");
        } catch (IOException e) {
            throw new SubmitRuntimeException("Load sql file failed!. pleas check!");
        }
        return sqls;
    }

    /**
     * 从sql base64字符串中解析出sql片段 {@link SqlSegment}。
     *
     * @param sql base64编码的sql字符串
     * @return list of {@link SqlSegment}
     */
    public static List<SqlSegment> getSqlListFromSql(String sql) {
        String decodeSql = new String(Base64.getDecoder().decode(sql), StandardCharsets.UTF_8);
        List<SqlSegment> sqls = new ArrayList<>();
        String[] sqlArray = decodeSql.split("(;\\s*\\r\\n)|(;\\s*\\n)|(;$)");
        for (String sqlSgt : sqlArray) {
            if (!StringUtils.isBlank(sqlSgt)) {
                sqls.add(new SqlSegment(sqlSgt));
            }
        }
        return sqls;
    }
}
