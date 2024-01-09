/*
 * 文 件 名:  CustomBatchJobStrategy.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.strategy;

import com.huawei.smartcampus.datatool.entity.DtBatchJobEntity;
import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;
import com.huawei.smartcampus.datatool.enums.SysConfigSuffixesKeysEnum;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.BatchJobGateway;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.SysConfigGateWay;
import com.huawei.smartcampus.datatool.repository.DtBatchJobRepository;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;
import com.huawei.smartcampus.datatool.utils.TypeCastUtils;

import java.util.List;
import java.util.Optional;

/**
 * 定制批处理作业查询策略
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/16]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class CustomBatchJobStrategy implements CustomStatisticsStrategy {
    private final SysConfigGateWay dbOverviewGateWay = SpringContextHelper.getBean(SysConfigGateWay.class);
    private final BatchJobGateway batchJobGateWay = SpringContextHelper.getBean(BatchJobGateway.class);
    private final DtBatchJobRepository dtBatchJobRepository = SpringContextHelper.getBean(DtBatchJobRepository.class);

    @Override
    public int countNum() {
        List<DtBatchJobEntity> dtBatchJobEntityList = dtBatchJobRepository.findAll();
        int count = 0;
        for (DtBatchJobEntity dtBatchJobEntity : dtBatchJobEntityList) {
            if (isCustom(dtBatchJobEntity.getId())) {
                count = count + 1;
            }
        }
        return count;
    }

    @Override
    public int countStateNum(boolean state) {
        List<DtBatchJobEntity> dtBatchJobEntityList = dtBatchJobRepository.findByState(state);
        int count = 0;
        for (DtBatchJobEntity dtBatchJobEntity : dtBatchJobEntityList) {
            if (isCustom(dtBatchJobEntity.getId())) {
                count = count + 1;
            }
        }
        return count;
    }

    @Override
    public boolean isCustom(String id) {
        Optional<DtBatchJobEntity> dtBatchJobEntityOptional = dtBatchJobRepository.findById(id);
        if (!dtBatchJobEntityOptional.isPresent()) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_JOB_NOT_EXIST);
        }
        Object suffixes = dbOverviewGateWay.getConfig(SysConfigNamesEnum.CUSTOM_FLAG.value(),
                SysConfigSuffixesKeysEnum.SUFFIX.value());
        List<String> suffixesList = TypeCastUtils.objectToList(suffixes, String.class);
        DtBatchJobEntity dtBatchJobEntity = dtBatchJobEntityOptional.get();
        List<String> dirList = batchJobGateWay.getJobFullDir(dtBatchJobEntity.getId());
        for (String suffix : suffixesList) {
            // 判断作业名称是否带后缀
            if (dtBatchJobEntity.getName().endsWith(suffix)) {
                return true;
            }
            // 判断作业目录是否带后缀
            for (String dirStr : dirList) {
                if (dirStr.endsWith(suffix)) {
                    return true;
                }
            }
        }
        return false;
    }
}