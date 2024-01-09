/*
 * 文 件 名:  CustomStreamJobStrategy.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/16
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.strategy;

import com.huawei.smartcampus.datatool.entity.StreamJobEntity;
import com.huawei.smartcampus.datatool.enums.JobStatus;
import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;
import com.huawei.smartcampus.datatool.enums.SysConfigSuffixesKeysEnum;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.application.service.vo.streamjob.StreamJobInfo;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.SysConfigGateWay;
import com.huawei.smartcampus.datatool.monitor.infrastructure.repository.StreamJobCustomRepository;
import com.huawei.smartcampus.datatool.repository.StreamJobRepository;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;
import com.huawei.smartcampus.datatool.utils.TypeCastUtils;

import java.util.List;
import java.util.Optional;

/**
 * 定制流处理作业查询策略
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/16]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class CustomStreamJobStrategy implements CustomStatisticsStrategy {
    private final SysConfigGateWay dbOverviewGateWay = SpringContextHelper.getBean(SysConfigGateWay.class);
    private final StreamJobRepository streamJobRepository = SpringContextHelper.getBean(StreamJobRepository.class);
    private final StreamJobCustomRepository streamJobCustomRepository = SpringContextHelper
            .getBean(StreamJobCustomRepository.class);

    @Override
    public int countNum() {
        List<StreamJobEntity> streamJobEntityList = streamJobRepository.findAll();
        int count = 0;
        for (StreamJobEntity streamJobEntity : streamJobEntityList) {
            if (isCustom(streamJobEntity.getId())) {
                count = count + 1;
            }
        }
        return count;
    }

    @Override
    public int countStateNum(boolean state) {
        List<StreamJobInfo> allJobs = streamJobCustomRepository.queryStreamJobInfo(null);
        int count = 0;
        for (StreamJobInfo streamJobInfo : allJobs) {
            if (JobStatus.valueOf(streamJobInfo.getStatus()).judgeRun() == state && isCustom(streamJobInfo.getId())) {
                count = count + 1;
            }
        }
        return count;
    }

    @Override
    public boolean isCustom(String id) {
        Optional<StreamJobEntity> streamJobEntityOptional = streamJobRepository.findById(id);
        if (!streamJobEntityOptional.isPresent()) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_JOB_NOT_EXIST);
        }
        StreamJobEntity streamJobEntity = streamJobEntityOptional.get();
        Object suffixes = dbOverviewGateWay.getConfig(SysConfigNamesEnum.CUSTOM_FLAG.value(),
                SysConfigSuffixesKeysEnum.SUFFIX.value());
        List<String> suffixesList = TypeCastUtils.objectToList(suffixes, String.class);
        for (String suffix : suffixesList) {
            if (streamJobEntity.getName().endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}