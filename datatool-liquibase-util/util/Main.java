/*
 * 文 件 名:  Main.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30006786
 * 修改时间： 2022/6/2
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.util;

import com.huawei.smartcampus.datatool.util.liquibase.LiquibaseUtil;
import com.huawei.smartcampus.datatool.util.liquibase.PrintUtil;

import java.util.Optional;

/**
 * liquibase执行工具类
 *
 * @author l30006786
 * @version [SmartCampus 22.2.0, 2022/11/4]
 * @see [相关类/方法]
 * @since [SmartCampus 22.2.0]
 */
public final class Main {
    private Main() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * datatool自定义liquibase工具类的入口函数
     * 接收入参，解析后调用liquibase的sdk进行数据库的版本维护
     *
     * @param args
     *            按照顺序分别传入dbType、ip、port、dbname、username、password密文、changelogPath
     */
    public static void main(String[] args) {
        try {
            int argsSize = Optional.ofNullable(args).map(val -> val.length).orElse(0);
            if (argsSize < 7 || argsSize > 8) {
                System.err.println(
                        "Incorrect number of input parameters! Please strictly pass in the parameters in the following format：");
                System.err.println(
                        "dbType、ip、port、dbname、username、password、changelogPath、schema(optional)");
                System.exit(1);
            }
            String dbType = args[0];
            String ip = args[1];
            String port = args[2];
            String dbname = args[3];
            String username = args[4];
            String password = args[5];
            String changelogPath = args[6];
            // schema参数仅用于opengauss场景
            String schema;
            if (argsSize == 7) {
                // 配置库刷库时，由于创建用户时设置了search_path与用户名一致，因此schema默认为用户名
                schema = username;
            } else {
                // 对接authcenter和webframe时，需要接收入参中指定的schema
                schema = args[7];
            }

            LiquibaseUtil util = new LiquibaseUtil(dbType, ip, port, dbname, schema, username, password, changelogPath);
            util.upgrade();
        } catch (Exception e) {
            // 打印异常堆栈后， 以状态1退出，提示调用的脚本本次执行失败
            PrintUtil.printCustomStackTrace(e);
            System.exit(1);
        }
    }
}
