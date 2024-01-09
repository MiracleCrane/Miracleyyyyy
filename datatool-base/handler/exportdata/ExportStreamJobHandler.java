/*
 * 文 件 名:  ExportStreamJobHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/11
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.handler.exportdata;

import com.huawei.smartcampus.datatool.base.enumeration.FileSuffixEnum;
import com.huawei.smartcampus.datatool.base.handler.exportdata.base.ExportHandler;
import com.huawei.smartcampus.datatool.base.handler.exportdata.helper.ExportHelper;
import com.huawei.smartcampus.datatool.base.utils.CommonUtils;
import com.huawei.smartcampus.datatool.base.utils.FileOperateUtils;
import com.huawei.smartcampus.datatool.entity.StreamJobEntity;
import com.huawei.smartcampus.datatool.utils.CommonUtil;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;

import org.springframework.beans.BeanUtils;

import java.io.File;
import java.util.List;

/**
 * 导出流处理作业处理器
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/11]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ExportStreamJobHandler implements ExportHandler {
    private ExportHelper exportHelper = SpringContextHelper.getBean(ExportHelper.class);
    @Override
    public void handleExportTask(List<String> ids, String path) {
        // 遍历json写入文件夹
        for (StreamJobEntity streamJobEntity : exportHelper.findStreamJobEntitiesByIdIn(ids)) {
            StreamJobEntity obj = new StreamJobEntity();
            BeanUtils.copyProperties(streamJobEntity, obj, "flinkId", "id");
            obj.setFlinkSql(CommonUtil.base64Decode(streamJobEntity.getFlinkSql()));
            String filePath = path + File.separator + streamJobEntity.getName() + FileSuffixEnum.STREAM.suffix();
            // 将json写入name.stream文件
            FileOperateUtils.writeJsonToFile(CommonUtils.transObjectToJsonString(obj), filePath);
        }
    }
}