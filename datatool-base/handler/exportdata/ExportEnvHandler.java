/*
 * 文 件 名:  ExportEnvHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/11
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.handler.exportdata;

import com.huawei.smartcampus.datatool.base.enumeration.ExportFileNameEnum;
import com.huawei.smartcampus.datatool.base.handler.exportdata.base.ExportHandler;
import com.huawei.smartcampus.datatool.base.handler.exportdata.helper.ExportHelper;
import com.huawei.smartcampus.datatool.base.utils.CommonUtils;
import com.huawei.smartcampus.datatool.base.utils.FileOperateUtils;
import com.huawei.smartcampus.datatool.base.vo.Params;
import com.huawei.smartcampus.datatool.base.vo.env.ExportEnvContent;
import com.huawei.smartcampus.datatool.entity.DtEnvironmentVariableEntity;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出环境变量处理器
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/11]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ExportEnvHandler implements ExportHandler {
    private ExportHelper exportHelper = SpringContextHelper.getBean(ExportHelper.class);
    @Override
    public void handleExportTask(List<String> ids, String srcPath) {
        // 组装文件内容
        Params<ExportEnvContent> contentParams = new Params<>();
        List<ExportEnvContent> envContents = new ArrayList<>();
        for (DtEnvironmentVariableEntity environmentVariableEntity : exportHelper
                .findDtEnvironmentVariableEntitiesByIdIn(ids)) {
            ExportEnvContent envContent = new ExportEnvContent();
            envContent.setName(environmentVariableEntity.getKey());
            envContent.setValue(environmentVariableEntity.getValue());
            envContents.add(envContent);
        }
        String filePath = srcPath + File.separator + ExportFileNameEnum.ENV.getName();
        contentParams.setParams(envContents);
        // 将json写入datatool.env文件
        FileOperateUtils.writeJsonToFile(CommonUtils.transObjectToJsonString(contentParams), filePath);
    }
}