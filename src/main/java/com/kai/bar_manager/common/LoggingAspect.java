package com.kai.bar_manager.common;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.kai.bar_manager..*Service.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end =  System.currentTimeMillis();

        log.info(
                "[{}] 실행시간: {}ms",
                joinPoint.getSignature().getName(),
                end - start
                );

        return  result;
    }
}
