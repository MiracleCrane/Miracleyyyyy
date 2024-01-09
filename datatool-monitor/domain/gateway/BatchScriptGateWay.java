/*
 * 文 件 名:  BatchScriptGateWay.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.gateway;

import com.huawei.smartcampus.datatool.monitor.application.service.vo.export.job.BatchScriptDetail;

import java.util.List;

/**
 * 批处理脚本目录抽象接口
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/16]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public interface BatchScriptGateWay {
    /**
     * 获取脚本目录列表
     *
     * @param id 脚本id
     * @return 脚本目录列表
     */
    List<String> getScriptFullDir(String id);

    /**
     * 获取批处理脚本明细
     *
     * @return 批处理脚本明细
     */
    List<BatchScriptDetail> getScriptDetail();
}