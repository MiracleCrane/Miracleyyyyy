/*
 * 文 件 名:  ExportCipherHandler.java
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
import com.huawei.smartcampus.datatool.base.vo.cipher.ExportCipherContent;
import com.huawei.smartcampus.datatool.entity.DtCipherVariableEntity;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出密码箱处理器
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/11]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ExportCipherHandler implements ExportHandler {
    private ExportHelper exportHelper = SpringContextHelper.getBean(ExportHelper.class);

    @Override
    public void handleExportTask(List<String> ids, String path) {
        // 组装文件内容
        List<ExportCipherContent> cipherContents = new ArrayList<>();
        for (DtCipherVariableEntity dtCipherVariableEntity : exportHelper.findCipherVariableEntitiesByIdIn(ids)) {
            ExportCipherContent cipherContent = new ExportCipherContent();
            cipherContent.setCipherKey(dtCipherVariableEntity.getKey());
            cipherContent.setCipherText("");
            cipherContents.add(cipherContent);
        }
        String filePath = path + File.separator + ExportFileNameEnum.CIPHER.getName();
        Params<ExportCipherContent> contentParams = new Params<>();
        contentParams.setParams(cipherContents);
        // 将json写入datatool.cipher文件
        FileOperateUtils.writeJsonToFile(CommonUtils.transObjectToJsonString(contentParams), filePath);
    }
}