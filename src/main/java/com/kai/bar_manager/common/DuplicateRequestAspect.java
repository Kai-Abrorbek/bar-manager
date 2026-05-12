package com.kai.bar_manager.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class DuplicateRequestAspect {

    private final RedisTemplate<String, String> redisTemplate;

    @Around("@annotation(com.kai.bar_manager.common.PreventDuplicate)")
    public Object preventDuplicate(ProceedingJoinPoint joinPoint) throws Throwable{

        String key = "duplicate" + joinPoint.getSignature().getName() + ":" + Arrays.toString(joinPoint.getArgs());

        Boolean isFirst =redisTemplate.opsForValue().setIfAbsent(key, "lock", 3, TimeUnit.SECONDS);
        if(Boolean.FALSE.equals(isFirst)){
            throw new CustomException("중복 요청입니다.", HttpStatus.TOO_MANY_REQUESTS);
        }

        return joinPoint.proceed();
    }
}
