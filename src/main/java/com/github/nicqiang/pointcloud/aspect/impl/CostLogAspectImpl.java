package com.github.nicqiang.pointcloud.aspect.impl;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * <h></h>
 *
 * @author nicqiang
 * @since 2019-06-01
 */
@Aspect
@Slf4j
public class CostLogAspectImpl {


    @Pointcut("@annotation(com.github.nicqiang.pointcloud.aspect.CostLogAspect)")
    public void costLog(){}

    @Around("costLog()")
    public Object cost(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        try {
            return joinPoint.proceed();
        } finally {
            log.info("methodName={}||cost={}", methodName, System.currentTimeMillis() - startTime);
        }
    }
}
