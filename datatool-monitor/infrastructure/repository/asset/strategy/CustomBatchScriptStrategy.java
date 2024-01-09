/*
 * 文 件 名:  CustomBatchScriptStrategy.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  Campus Core 23.1
 * 描    述:  <描述>
 * 修 改 人:  j00826364
 * 修改时间： 2023/11/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.infrastructure.repository.asset.strategy;

import com.huawei.smartcampus.datatool.entity.DtScriptEntity;
import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;
import com.huawei.smartcampus.datatool.enums.SysConfigSuffixesKeysEnum;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.BatchScriptGateWay;
import com.huawei.smartcampus.datatool.monitor.domain.gateway.SysConfigGateWay;
import com.huawei.smartcampus.datatool.repository.DtScriptRepository;
import com.huawei.smartcampus.datatool.utils.SpringContextHelper;
import com.huawei.smartcampus.datatool.utils.TypeCastUtils;

import java.util.List;
import java.util.Optional;

/**
 * 定制批处理脚本查询策略
 *
 * @author j00826364
 * @version [Campus Core 23.1, 2023/11/15]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
public class CustomBatchScriptStrategy implements CustomStatisticsStrategy {
    private final SysConfigGateWay dbOverviewGateWay = SpringContextHelper.getBean(SysConfigGateWay.class);
    private final BatchScriptGateWay batchScriptGateWay = SpringContextHelper.getBean(BatchScriptGateWay.class);
    private final DtScriptRepository dtScriptRepository = SpringContextHelper.getBean(DtScriptRepository.class);

    @Override
    public int countNum() {
        List<DtScriptEntity> dtScriptEntityList = dtScriptRepository.findAll();
        int count = 0;
        for (DtScriptEntity dtScriptEntity : dtScriptEntityList) {
            if (isCustom(dtScriptEntity.getId())) {
                count = count + 1;
            }
        }
        return count;
    }

    @Override
    public int countStateNum(boolean state) {
        // 脚本状态数量不统计，直接返回0即可
        return 0;
    }

    @Override
    public boolean isCustom(String id) {
        Optional<DtScriptEntity> dtScriptEntityOptional = dtScriptRepository.findById(id);
        if (!dtScriptEntityOptional.isPresent()) {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_SCRIPT_NOT_EXIST);
        }
        Object suffixes = dbOverviewGateWay.getConfig(SysConfigNamesEnum.CUSTOM_FLAG.value(),
                SysConfigSuffixesKeysEnum.SUFFIX.value());
        List<String> suffixesList = TypeCastUtils.objectToList(suffixes, String.class);
        DtScriptEntity dtScriptEntity = dtScriptEntityOptional.get();
        List<String> dirList = batchScriptGateWay.getScriptFullDir(dtScriptEntity.getId());
        for (String suffix : suffixesList) {
            // 判断脚本名称是否带后缀
            if (dtScriptEntity.getName().endsWith(suffix)) {
                return true;
            }
            // 判断脚本目录是否带后缀
            for (String dirStr : dirList) {
                if (dirStr.endsWith(suffix)) {
                    return true;
                }
            }
        }
        return false;
    }
}