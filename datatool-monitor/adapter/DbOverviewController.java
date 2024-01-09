/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 */

package com.huawei.smartcampus.datatool.monitor.adapter;

import com.huawei.smartcampus.datatool.concurrentlimit.ConcurrentLimit;
import com.huawei.smartcampus.datatool.model.BaseResponse;
import com.huawei.smartcampus.datatool.monitor.application.service.DbStorageOverviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据库概览接口
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/19]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@RestController
@RequestMapping(value = "/v1/databases")
public class DbOverviewController {
    @Autowired
    private DbStorageOverviewService dbStorageOverviewService;

    @GetMapping(value = "/storage")
    @ConcurrentLimit()
    public BaseResponse queryDatabaseStore() {
        return BaseResponse.newOk(dbStorageOverviewService.queryDBStorage());
    }

    @GetMapping(value = "/tables/rownum/order")
    @ConcurrentLimit()
    public BaseResponse queryTableRowNumOrder() {
        return BaseResponse.newOk(dbStorageOverviewService.queryTableRowNumOrder());
    }

    @GetMapping(value = "/tables/storage/order")
    @ConcurrentLimit()
    public BaseResponse queryTableStorageOrder() {
        return BaseResponse.newOk(dbStorageOverviewService.queryTableStorageOrder());
    }
}