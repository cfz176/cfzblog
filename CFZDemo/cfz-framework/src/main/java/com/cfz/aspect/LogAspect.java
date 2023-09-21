package com.cfz.aspect;

import com.alibaba.fastjson.JSON;
import com.cfz.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.cfz.annotation.SystemLog)")
    public void pt() {

    }

    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res;
        try {
            handleBefore(joinPoint);
            res =  joinPoint.proceed();
            hanleAfter(res);
        } finally {
            log.info("===========END============",System.lineSeparator());
        }

        return res;
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        SystemLog systemLog = getSystemLog(joinPoint);

        log.info("=========Start==========");
        //打印请求 URL
        log.info("URL                  : {}",request.getRequestURL());
        //打印描述信息
        log.info("BusionessName        : {}",systemLog.businessName());
        //打印 Http method
        log.info("Http method          : {}",request.getMethod());
        //打印调用 controller 的全局路径以及执行方法
        log.info("Class Method         : {}.{}",joinPoint.getSignature().getDeclaringTypeName(),joinPoint.getSignature().getName());
        //打印 打印请求的 IP
        log.info("IP                   : {}",request.getRemoteHost());
        //打印请求入参
        log.info("Request Args         : {}", JSON.toJSONString(joinPoint.getArgs()));
        //打印结束后换行
        log.info("=========Start==========");

    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        SystemLog systemLog = methodSignature.getMethod().getAnnotation(SystemLog.class);
        return systemLog;
    }

    private void hanleAfter(Object res) {
        //打印请求入参
        log.info("Response             : {}", JSON.toJSONString(res));
    }
    
}
