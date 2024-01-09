/*
 * 文 件 名:  ParamValidator.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  yWX890060
 * 修改时间： 2021/5/18
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 枚举的校验
 *
 * @author yWX890060
 * @version [SmartCampus V100R001C00, 2021/5/18]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class ChooseOneShortValidator implements ConstraintValidator<ChooseOneShort, Short> {
    private short[] values;

    @Override
    public void initialize(ChooseOneShort chooseOneShort) {
        this.values = chooseOneShort.value();
    }

    @Override
    public boolean isValid(Short value, ConstraintValidatorContext constraintValidatorContext) {
        for (int i = 0; i < values.length; i++) {
            if (value.equals(values[i])) {
                return true;
            }
        }
        return false;
    }
}
