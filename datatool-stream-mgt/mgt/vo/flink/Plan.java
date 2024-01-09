/*
 * 文 件 名:  Plan.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/8/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.vo.flink;

import java.util.List;

/**
 * 查询作业计划，部分响应
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/8/15]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class Plan {
    private String jid;
    private String name;
    private String type;
    private List<PlanNode> nodes;

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PlanNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<PlanNode> nodes) {
        this.nodes = nodes;
    }
}
