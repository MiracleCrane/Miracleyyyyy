/*
 * 文 件 名:  Chain.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/12
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.handler.importdata.chain;

import com.huawei.smartcampus.datatool.base.handler.importdata.ImportBatchJobDataToolHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.ImportBatchJobDgcHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.ImportCipherDataToolHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.ImportConnDataToolHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.ImportConnDgcHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.ImportDliDgcHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.ImportDliVarDgcHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.ImportEnvHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.ImportJsonConnDgcHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.ImportScriptHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.ImportStreamJobDataToolHandler;
import com.huawei.smartcampus.datatool.base.handler.importdata.base.AbstractImportHandler;
import com.huawei.smartcampus.datatool.base.model.TaskModel;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 职责链组装类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/12]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Component
@DependsOn("springContextHelper")
public class ImportHandlerChain {
    private AbstractImportHandler streamJobDataToolHandler = SpringContextHelper
            .getBean(ImportStreamJobDataToolHandler.class);
    private AbstractImportHandler batchJobDataToolHandler = SpringContextHelper
            .getBean(ImportBatchJobDataToolHandler.class);
    private AbstractImportHandler batchJobDgcHandler = SpringContextHelper.getBean(ImportBatchJobDgcHandler.class);
    private AbstractImportHandler cipherDataToolHandler = SpringContextHelper
            .getBean(ImportCipherDataToolHandler.class);
    private AbstractImportHandler connDataToolHandler = SpringContextHelper.getBean(ImportConnDataToolHandler.class);
    private AbstractImportHandler connDgcHandler = SpringContextHelper.getBean(ImportConnDgcHandler.class);
    private AbstractImportHandler dliDgcHandler = SpringContextHelper.getBean(ImportDliDgcHandler.class);
    private AbstractImportHandler dliVarDgcHandler = SpringContextHelper.getBean(ImportDliVarDgcHandler.class);
    private AbstractImportHandler envHandler = SpringContextHelper.getBean(ImportEnvHandler.class);
    private AbstractImportHandler jsonConnDgcHandler = SpringContextHelper.getBean(ImportJsonConnDgcHandler.class);
    private AbstractImportHandler scriptHandler = SpringContextHelper.getBean(ImportScriptHandler.class);

    @PostConstruct
    public void init() {
        // 组装导入职责链
        streamJobDataToolHandler.nextHandler(batchJobDataToolHandler);
        batchJobDataToolHandler.nextHandler(batchJobDgcHandler);
        batchJobDgcHandler.nextHandler(cipherDataToolHandler);
        cipherDataToolHandler.nextHandler(connDataToolHandler);
        connDataToolHandler.nextHandler(connDgcHandler);
        connDgcHandler.nextHandler(dliDgcHandler);
        dliDgcHandler.nextHandler(dliVarDgcHandler);
        dliVarDgcHandler.nextHandler(envHandler);
        envHandler.nextHandler(jsonConnDgcHandler);
        jsonConnDgcHandler.nextHandler(scriptHandler);
    }

    public void handler(TaskModel taskModel) {
        streamJobDataToolHandler.importHandle(taskModel);
    }
}
