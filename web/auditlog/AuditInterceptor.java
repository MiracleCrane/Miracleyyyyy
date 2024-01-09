/*
 * 文 件 名:  AuditLogAspect.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  审计日志
 * 修 改 人:  s30009470
 * 修改时间： 2021/3/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.auditlog;

import com.huawei.hicampus.campuscommon.auditlog.common.AuditLogValidateUtils;
import com.huawei.hicampus.campuscommon.auditlog.entity.AuditLog;
import com.huawei.hicampus.campuscommon.common.util.Constant;
import com.huawei.hicampus.campuscommon.common.util.SecureRandomUtils;
import com.huawei.smartcampus.datatool.entity.LocalAuditLogEntity;
import com.huawei.smartcampus.datatool.repository.LocalAuditLogRepository;
import com.huawei.smartcampus.datatool.utils.CommonUtil;
import com.huawei.smartcampus.datatool.utils.RequestContext;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 审计日志
 * 数据库审计日志，增删改记录
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/3/15]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Aspect
@Component
public class AuditInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditInterceptor.class);

    public static final String MODULE = "DataTool";

    @Autowired
    private LocalAuditLogRepository localAuditLogRepository;

    /**
     * 审计日志，切入事件类型注解
     * 涉及到增删改的接口需要加上 AuditLogTrack 注解，敏感信息查询接口也需要加上!
     */
    @Pointcut("@annotation(com.huawei.smartcampus.datatool.auditlog.AuditLogTrack)")
    public void auditLogAnnotation() {
        // 声明切入点
    }

    /**
     * 环绕日志
     *
     * @param joinPoint 连接点
     * @return 对象
     * @throws Throwable 异常
     */
    @Around("auditLogAnnotation()")
    public Object logAroundOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AuditLogTrack auditLogTrack = signature.getMethod().getAnnotation(AuditLogTrack.class);
        RequestContext.set(RequestContext.OPERATION_NAME, auditLogTrack.operation());
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();
        // 原本的操作对象从jpa操作中取，现在改为直接从注解取
        RequestContext.set(RequestContext.OPERATION_OBJECT, auditLogTrack.operationObject());

        if (auditLogTrack.isImportOrExport()) {
            addFileStreamDetail(parameterValues);
        } else {
            addDefaultDetail(parameterNames, parameterValues);
        }
        return joinPoint.proceed();
    }

    private void addDefaultDetail(String[] parameterNames, Object[] parameterValues) {
        Map<String, Object> paramMap = new HashMap<>();
        // 遍历参数数组，将参数名称和参数值保存到Map中
        // 对象需要实现 toString方法，不能有敏感信息
        for (int i = 0; i < parameterNames.length; i++) {
            paramMap.put(parameterNames[i], String.valueOf(parameterValues[i]));
        }
        RequestContext.setRequestDetail(RequestContext.REQUEST, paramMap);
    }

    private void addFileStreamDetail(Object[] parameterValues) {
        if (parameterValues == null || parameterValues.length == 0) {
            return;
        }
        List<Object> filteredParameterValues = new ArrayList<>();
        for (Object arg : parameterValues) {
            if (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)) {
                filteredParameterValues.add(arg);
            }
        }
        boolean needOperationObject = StringUtils.isEmpty((String) RequestContext.get(RequestContext.OPERATION_OBJECT));
        for (Object parameter : filteredParameterValues) {
            RequestContext.setRequestDetail(RequestContext.REQUEST, parameter.toString());
            // 注解中没有操作对象的时候，检查请求中是否有操作对象
            if (needOperationObject) {
                String resourceType = getResourceType(parameter);
                if (!StringUtils.isEmpty(resourceType)) {
                    RequestContext.set(RequestContext.OPERATION_OBJECT, resourceType);
                    needOperationObject = false;
                }
            }
        }
    }

    /**
     * 针对导入导出 获取操作对象
     * 
     * @param parameter 参数
     * @return 操作对象
     */
    private String getResourceType(Object parameter) {
        String resourceType = null;
        try {
            Method getResourceTypeMethod = parameter.getClass().getMethod("getResourceType");
            resourceType = (String) getResourceTypeMethod.invoke(parameter);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("Failed to get resource type", e);
        }
        return resourceType;
    }

    /**
     * 记录审计日志到本地表
     */
    private void addLocalAuditLog() {
        if (RequestContext.get(RequestContext.OPERATION_NAME) == null) {
            return;
        }
        AuditLog auditLog = new AuditLog();
        auditLog.setOperationName((String) RequestContext.get(RequestContext.OPERATION_NAME));
        auditLog.setOperationObject((String) RequestContext.get(RequestContext.OPERATION_OBJECT));
        auditLog.setRiskLevel(Constant.AuditLog.RISK_LEVEL_GENARAL);
        auditLog.setType(Constant.AuditLog.TYPE_OPERATION);
        auditLog.setModule(MODULE);
        auditLog.setDetail(RequestContext.getRequestDetail());
        auditLog.setClientIp((String) RequestContext.get(RequestContext.REQUEST_IP));
        auditLog.setRequestDate(new Date());
        auditLog.setUserName(RequestContext.getUserName());
        auditLog.setUserId(CommonUtil.convertToLong(RequestContext.get(RequestContext.USER_ID_FIELD)));
        String resCode = (String) RequestContext.get(RequestContext.RES_CODE);
        // 导入导出没有resCode
        if (resCode == null || resCode.equals("0")) {
            auditLog.setResultCode(Constant.AuditLog.RESULT_CODE_SUCCESS);
            auditLog.setResultMessage(Constant.AuditLog.RESULT_MESSAGE_SUCCESS);
        } else {
            auditLog.setResultCode((String) RequestContext.get(RequestContext.RES_CODE));
            auditLog.setResultMessage((String) RequestContext.get(RequestContext.RES_MSG));
        }
        auditLog.setUniqueId(SecureRandomUtils.generateUUID());
        // 校验日志信息
        try {
            AuditLogValidateUtils.validateAuditLog(auditLog);
            // 合法性检查通过，放入过滤后的列表中
        } catch (Exception e) {
            LOGGER.error("filter invalidAuditLog=" + JSON.toJSONString(auditLog) + "error=" + e.getMessage());
            return;
        }
        // 保存审计日志
        String content = JSONObject.toJSONString(auditLog);
        LocalAuditLogEntity entity = new LocalAuditLogEntity();
        entity.setContent(content);
        entity.setUniqueId(auditLog.getUniqueId());
        localAuditLogRepository.save(entity);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        try {
            addLocalAuditLog();
        } catch (Exception e) {
            LOGGER.error("An error occurred when saving audit logs", e);
        }
    }
}
