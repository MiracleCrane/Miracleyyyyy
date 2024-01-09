/*
 * 文 件 名:  ExportConnHandler.java
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
import com.huawei.smartcampus.datatool.base.vo.conn.ExportConnContent;
import com.huawei.smartcampus.datatool.entity.DtConnectionEntity;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import org.springframework.beans.BeanUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出数据连接处理器
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/11]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ExportConnHandler implements ExportHandler {
    private ExportHelper exportHelper = SpringContextHelper.getBean(ExportHelper.class);

    @Override
    public void handleExportTask(List<String> ids, String srcPath) {
        // 组装文件内容
        Params<ExportConnContent> contentParams = new Params<>();
        List<ExportConnContent> envContents = new ArrayList<>();
        for (DtConnectionEntity connectionEntity : exportHelper.findDtConnectionEntitiesByIdIn(ids)) {
            ExportConnContent connContent = new ExportConnContent();
            BeanUtils.copyProperties(connectionEntity, connContent, "password");
            connContent.setPassword("");
            connContent.setUsername(connectionEntity.getUser());
            envContents.add(connContent);
        }
        String filePath = srcPath + File.separator + ExportFileNameEnum.CONN.getName();
        contentParams.setParams(envContents);
        // 将json写入datatool.conn文件
        FileOperateUtils.writeJsonToFile(CommonUtils.transObjectToJsonString(contentParams), filePath);
    }
}