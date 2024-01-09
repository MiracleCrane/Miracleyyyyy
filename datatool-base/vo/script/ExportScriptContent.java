/*
 * 文 件 名:  ExportScriptContent.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/20
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.vo.script;

/**
 * 导出脚本，文本内容类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/20]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ExportScriptContent {
    private String connectionName;
    private String content;
    private String database;
    private String directory;
    private String name;

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}