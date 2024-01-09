/*
 * 文 件 名:  JobType.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/5/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.overview;

import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.ExportI18nCode;

import java.util.Locale;

/**
 * 模型分层
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/20]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public enum ModelLayerEnum {
    DM("dm"),
    DWR("dwr"),
    DWI("dwi");

    private String value;

    ModelLayerEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static String getI18nCode(String value) {
        switch (ModelLayerEnum.valueOf(value.toUpperCase(Locale.ROOT))) {
            case DM:
                return ExportI18nCode.DATATOOL_DM;
            case DWR:
                return ExportI18nCode.DATATOOL_DWR;
            case DWI:
                return ExportI18nCode.DATATOOL_DWI;
            default:
                return "";
        }
    }
}
