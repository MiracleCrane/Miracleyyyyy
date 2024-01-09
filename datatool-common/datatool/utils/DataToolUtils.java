/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.utils;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * datatool工具类，一些与datatool业务相关的业务方法可以放在这里
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/10/24]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DataToolUtils {
    private static final String DATABASE_ACCESS_BLACKLIST = "DATABASE_ACCESS_BLACKLIST";

    private DataToolUtils() {
    }

    /**
     * 获取数据库黑名单
     *
     * @return 数据库黑名单，全部是小写
     */
    public static Set<String> getBlacklistLowerCase() {
        Set<String> blacklist = new HashSet<>();
        String blacklistStr = System.getenv(DATABASE_ACCESS_BLACKLIST);
        blacklistStr = (blacklistStr == null) ? "" : blacklistStr;
        StringTokenizer stringTokenizer = new StringTokenizer(blacklistStr, ",");
        while (stringTokenizer.hasMoreTokens()) {
            blacklist.add(stringTokenizer.nextToken().trim().toLowerCase(Locale.ROOT));
        }
        return blacklist;
    }

    /**
     * 判断是否是黑名单数据库，不区分大小写
     *
     * @param database 数据库名称
     * @return true/false
     */
    public static boolean isBlackListDataBase(String database) {
        if (StringUtils.isBlank(database)) {
            return false;
        }

        Set<String> blacklist = DataToolUtils.getBlacklistLowerCase();
        String toLowerCaseDatabase = database.toLowerCase(Locale.ROOT);
        // 判断待连接的数据库是否在黑名单中
        if (blacklist.contains(toLowerCaseDatabase)) {
            return true;
        }
        return false;
    }
}