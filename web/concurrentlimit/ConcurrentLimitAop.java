/*
 * 文 件 名:  AuditLogAspect.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  流控
 * 修 改 人:  s30009470
 * 修改时间： 2021/3/15
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.concurrentlimit;

import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * 并发控制切面
 *
 * @author s30009470
 * @version [Campus Core 23.1, 2023/11/6]
 * @see [相关类/方法]
 * @since [Campus Core 23.1]
 */
@Aspect
@Component
public class ConcurrentLimitAop {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentLimitAop.class);

    private final Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    /**
     * 并发访问控制
     */
    @Pointcut("@annotation(com.huawei.smartcampus.datatool.concurrentlimit.ConcurrentLimit)")
    public void concurrentLimitAnnotation() {
        // 声明切入点
    }

    @Around("concurrentLimitAnnotation()")
    public Object limitConcurrency(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ConcurrentLimit concurrentLimit = signature.getMethod().getAnnotation(ConcurrentLimit.class);
        String key = joinPoint.getSignature().getName();
        Semaphore semaphore = getSemaphoreForAnnotation(key, concurrentLimit);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("limitConcurrencyKey={}", key);
        }
        if (semaphore.tryAcquire()) {
            try {
                return joinPoint.proceed();
            } finally {
                semaphore.release();
            }
        } else {
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_CURRENT_REQUEST_EXCEED_LIMIT);
        }
    }

    private Semaphore getSemaphoreForAnnotation(String key, ConcurrentLimit concurrentLimit) {
        return semaphoreMap.computeIfAbsent(key, k -> new Semaphore(concurrentLimit.value()));
    }
}
