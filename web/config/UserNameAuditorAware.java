/*
 * 文 件 名:  UserNameAuditorAware.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/12
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.config;

import com.huawei.smartcampus.datatool.utils.RequestContext;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 自动更新表中创建人和最后修改人
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/12]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
@Component
public class UserNameAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(RequestContext.getUserName());
    }
}
