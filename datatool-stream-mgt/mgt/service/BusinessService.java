/*
 * 文 件 名:  BusinessService.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.service;

import com.huawei.smartcampus.datatool.stream.mgt.vo.BatchOperateResult;
import com.huawei.smartcampus.datatool.stream.mgt.vo.DeleteStreamJobsReq;
import com.huawei.smartcampus.datatool.stream.mgt.vo.JobStatusInfo;
import com.huawei.smartcampus.datatool.stream.mgt.vo.QueryDataStreamJobInfoResult;
import com.huawei.smartcampus.datatool.stream.mgt.vo.QueryStreamJobsResult;
import com.huawei.smartcampus.datatool.stream.mgt.vo.StreamJobQueryCondi;
import com.huawei.smartcampus.datatool.stream.mgt.vo.StreamJobReq;
import com.huawei.smartcampus.datatool.stream.mgt.vo.ValidateNameReq;
import com.huawei.smartcampus.datatool.stream.mgt.vo.ValidateNameResp;
import com.huawei.smartcampus.datatool.vo.QueryMaxJobNumResp;

/**
 * 业务接口类
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public interface BusinessService {
    /**
     * 删除作业
     *
     * @param deleteStreamJobsReq Request
     * @return BatchOperateResult
     */
    BatchOperateResult deleteJobs(DeleteStreamJobsReq deleteStreamJobsReq);

    /**
     * 保存作业
     *
     * @param jobInfo 作业内容
     * @return 作业id
     */
    String saveJob(StreamJobReq jobInfo);

    /**
     * 更新作业
     *
     * @param jobId 作业ID
     * @param jobInfo 作业内容
     * @return 作业id
     */
    String updateJob(String jobId, StreamJobReq jobInfo);

    /**
     * 查询作业
     *
     * @param condi 查询条件
     * @return QueryStreamJobsResult
     */
    QueryStreamJobsResult queryJobs(StreamJobQueryCondi condi);

    /**
     * 获取作业详情
     *
     * @param jobId 作业id
     * @return QueryDataStreamJobInfoResult
     */
    QueryDataStreamJobInfoResult getDataStreamJobInfo(String jobId);

    /**
     * 查询作业状态
     *
     * @param id 作业id
     * @return JobStatusInfo
     */
    JobStatusInfo queryJobStatusById(String id);

    /**
     * 校验重名
     *
     * @param validateNameReq 重名请求id、name
     * @return ValidateNameResp
     */
    ValidateNameResp checkJobName(ValidateNameReq validateNameReq);

    QueryMaxJobNumResp queryMaxJobs();
}
