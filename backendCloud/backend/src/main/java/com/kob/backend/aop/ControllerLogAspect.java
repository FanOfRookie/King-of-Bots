package com.kob.backend.aop;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllerLogAspect {
    @Pointcut("execution(public * com.kob.backend.controller..*.*(..))")
    public void pointcutMethod(){

    }

    @Around("pointcutMethod()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable{
        //获取全名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        StringBuilder info = new StringBuilder();
        info.append(methodSignature.getDeclaringTypeName()).append(methodSignature.getName());
        Object[] args = joinPoint.getArgs();
        info.append(" 请求入参: ").append(toJSONString(args));
        long start = System.currentTimeMillis();
        Object result;
        try { // 执行触发切点的方法（而非serviceMethods方法）
            result = joinPoint.proceed();
            long finish = System.currentTimeMillis();
            info.append(" Query cost time(ms): " + (finish - start));
        } catch (Throwable throwable) {
            info.append(" Exception detail: " + throwable);
            throw throwable;
        }finally {
            log.info(info.toString());
        }
        return result;
    }

    private String toJSONString(Object object){
        try {
            return JSONObject.toJSONString(object);
        }catch (Exception e){
            return "";
        }
    }
}
