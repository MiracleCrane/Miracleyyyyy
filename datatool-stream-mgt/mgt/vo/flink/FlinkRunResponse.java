/*
 * 文 件 名:  FlinkRunResponse.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo.flink;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class FlinkRunResponse {
    private List<String> successNames;
    private List<FlinkRunErrorInfo> errorInfo;

    /**
     * 无参构造
     */
    public FlinkRunResponse() {}

    /**
     * 全参构造
     *
     * @param successNames successNames
     * @param errorInfo errorInfo
     */
    public FlinkRunResponse(List<String> successNames, List<FlinkRunErrorInfo> errorInfo) {
        this.successNames = successNames;
        this.errorInfo = errorInfo;
    }

    public List<String> getSuccessNames() {
        return successNames;
    }

    public void setSuccessNames(List<String> successNames) {
        this.successNames = successNames;
    }

    public List<FlinkRunErrorInfo> getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(List<FlinkRunErrorInfo> errorInfo) {
        this.errorInfo = errorInfo;
    }
}
