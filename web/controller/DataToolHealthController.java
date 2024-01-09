/*
 * 文 件 名:  HealthController.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  wWX643278
 * 修改时间： 2021/6/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.controller;

import com.huawei.smartcampus.datatool.model.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * stream健康检查接口
 *
 * @author wWX643278
 * @version [SmartCampus V100R001C00, 2021/6/2]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@RestController
public class DataToolHealthController {
    /**
     * 存活探针
     *
     * @return BaseResponse 响应结果
     */
    @RequestMapping("/health-check")
    public BaseResponse getResponse() {
        return BaseResponse.newOk();
    }
}
