/*
 * 文 件 名:  ImportScriptContent.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/22
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.vo.script;

import com.huawei.smartcampus.datatool.entity.DtScriptEntity;

/**
 * 导入脚本模型类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/22]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ImportScriptContent {
    private DtScriptEntity dtScriptEntity;
    private String dir;

    public DtScriptEntity getDtScriptEntity() {
        return dtScriptEntity;
    }

    public void setDtScriptEntity(DtScriptEntity dtScriptEntity) {
        this.dtScriptEntity = dtScriptEntity;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}