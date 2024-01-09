/*
 * 文 件 名:  SysConfigRepository.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2023/9/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.sysconfig;

import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.Configuration;
import com.huawei.smartcampus.datatool.base.sysconfig.application.service.vo.SysConfigVo;
import com.huawei.smartcampus.datatool.entity.DtConnectionEntity;
import com.huawei.smartcampus.datatool.entity.SystemConfigurationEntity;
import com.huawei.smartcampus.datatool.enums.OverviewDbKeysEnum;
import com.huawei.smartcampus.datatool.enums.SysConfigNamesEnum;
import com.huawei.smartcampus.datatool.repository.DtConnectionRepository;
import com.huawei.smartcampus.datatool.repository.DtSystemConfigurationRepository;
import com.huawei.smartcampus.datatool.utils.TimeUtils;

import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 系统配置仓
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/10/21]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Component
public class SysConfigRepository implements SysConfigGateWay {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysConfigRepository.class);

    @Autowired
    private DtSystemConfigurationRepository sysRepository;

    @Autowired
    private DtConnectionRepository dtConnectionRepository;

    @Override
    public SysConfigNamesEnum getSysConfigName(String config) {
        if (config == null || config.trim().isEmpty()) {
            return null;
        }
        try {
            return SysConfigNamesEnum.valueOf(config.replace("-", "_").toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public List<Configuration> querySysConfig(SysConfigNamesEnum name) {
        List<SystemConfigurationEntity> entities = queryOriginSysConfig(name);
        List<Configuration> configurations = new ArrayList<>();
        if (entities == null || entities.isEmpty()) {
            return configurations;
        }
        for (SystemConfigurationEntity entity : entities) {
            String key = entity.getKey();
            Object value = JSONObject.parseObject(entity.getValue()).get("val");
            if (OverviewDbKeysEnum.CONNID.value().equals(key)) {
                DtConnectionEntity conn = dtConnectionRepository.findDtConnectionEntityById((String) value);
                Configuration connName = new Configuration();
                connName.setKey("connName");
                if (conn != null) {
                    // 数据连接存在
                    connName.setValue(conn.getName());
                } else {
                    // 数据连接不存在，id也设置为null
                    value = null;
                }
                configurations.add(connName);
            }
            if ("createdDate".equals(key) || "lastModifiedDate".equals(key)) {
                value = TimeUtils.longToUtcDate((Long) value);
            }
            Configuration item = new Configuration();
            item.setKey(key);
            item.setValue(value);
            configurations.add(item);
        }
        return configurations;
    }

    @Override
    public List<SystemConfigurationEntity> queryOriginSysConfig(SysConfigNamesEnum name) {
        return sysRepository.findAllByName(name.value());
    }

    @Override
    public void saveAll(SysConfigNamesEnum configname, List<SysConfigVo> configVos) {
        List<SystemConfigurationEntity> entities = sysRepository.findAllByName(configname.value());
        List<SystemConfigurationEntity> newEntities = new ArrayList<>();

        for (SysConfigVo config : configVos) {
            String convertedValue = convertToJson(config.getValue());
            boolean isNew = true;
            for (SystemConfigurationEntity entity : entities) {
                if (config.getKey().equals(entity.getKey()) && config.getName().equals(entity.getName())) {
                    entity.setValue(convertToJson(config.getValue()));
                    newEntities.add(entity);
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                newEntities.add(new SystemConfigurationEntity(config.getName(), config.getKey(),
                        convertToJson(config.getValue())));
            }
            config.setValue(convertedValue);
        }
        sysRepository.saveAll(newEntities);
    }

    private String convertToJson(Object value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("val", value);
        return jsonObject.toString();
    }
}
