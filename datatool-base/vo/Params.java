/*
 * 文 件 名:  Params.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/19
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.vo;

import java.util.List;

/**
 * 导出格式外层类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/19]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class Params<T> {
    private List<T> params;

    public List<T> getParams() {
        return params;
    }

    public void setParams(List<T> params) {
        this.params = params;
    }
}