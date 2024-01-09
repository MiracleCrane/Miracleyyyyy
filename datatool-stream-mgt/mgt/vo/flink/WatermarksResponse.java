/*
 * 文 件 名:  WatermarksResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/14
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo.flink;

import com.alibaba.fastjson.JSONArray;

/**
 * 获取节点的watermarks的响应
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/14]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class WatermarksResponse {
    // 由于flink官网，REST-API接口说明响应是ANY类型，经调用分析，更确切的是JSONArray的类型
    private JSONArray watermarks;

    public JSONArray getWatermarks() {
        return watermarks;
    }

    public void setWatermarks(JSONArray watermarks) {
        this.watermarks = watermarks;
    }
}