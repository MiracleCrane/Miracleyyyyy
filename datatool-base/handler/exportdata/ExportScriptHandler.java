/*
 * 文 件 名:  ExportScriptHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/11
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.handler.exportdata;

import com.huawei.smartcampus.datatool.base.enumeration.DataToolResourceTypeEnum;
import com.huawei.smartcampus.datatool.base.enumeration.FileSuffixEnum;
import com.huawei.smartcampus.datatool.base.handler.exportdata.base.ExportHandler;
import com.huawei.smartcampus.datatool.base.handler.exportdata.helper.ExportHelper;
import com.huawei.smartcampus.datatool.base.utils.CommonUtils;
import com.huawei.smartcampus.datatool.base.utils.FileOperateUtils;
import com.huawei.smartcampus.datatool.base.vo.script.ExportScriptContent;
import com.huawei.smartcampus.datatool.entity.DtScriptEntity;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import java.io.File;
import java.util.List;

/**
 * 导出脚本处理器
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/11]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ExportScriptHandler implements ExportHandler {
    private ExportHelper exportHelper = SpringContextHelper.getBean(ExportHelper.class);

    @Override
    public void handleExportTask(List<String> ids, String srcPath) {
        for (DtScriptEntity scriptEntity : exportHelper.findDtScriptEntitiesByIdIn(ids)) {
            ExportScriptContent scriptContent = new ExportScriptContent();
            scriptContent.setContent(CommonUtils.transformProperty(scriptEntity.getContent()));
            scriptContent.setName(scriptEntity.getName());
            scriptContent.setDatabase(CommonUtils.transformProperty(scriptEntity.getDatabase()));
            scriptContent.setConnectionName(CommonUtils.transformProperty(scriptEntity.getConnName()));
            String dir = exportHelper.getDirectory(scriptEntity.getDirId(), DataToolResourceTypeEnum.SCRIPT.type());
            scriptContent.setDirectory(dir);
            String filePath = srcPath + dir + File.separator + scriptEntity.getName() + FileSuffixEnum.SCRIPT.suffix();
            // 将json写入name.script文件
            FileOperateUtils.writeJsonToFile(CommonUtils.transObjectToJsonString(scriptContent), filePath);
        }
    }
}