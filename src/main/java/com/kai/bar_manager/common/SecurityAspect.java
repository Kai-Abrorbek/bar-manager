package com.kai.bar_manager.common;

import com.kai.bar_manager.staff.dto.AuthRequest;
import com.kai.bar_manager.staff.dto.StaffRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class SecurityAspect {

    @Before("@annotation(com.kai.bar_manager.common.AdminOnly)")
    public void checkAdmin(JoinPoint joinPoint){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!userName.equals("Admin")){
            throw new RuntimeException("관리자만 접근이 가능합니다.");
        }
    }
}
