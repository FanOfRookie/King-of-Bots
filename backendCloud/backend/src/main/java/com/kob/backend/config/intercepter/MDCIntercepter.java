package com.kob.backend.config.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class MDCIntercepter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getHeader("requestID") == null || request.getHeader("requestID").isEmpty())
            MDC.put("requestID", Objects.toString(Thread.currentThread().getId() + System.currentTimeMillis()));
        else
            MDC.put("requestID", request.getHeader("requestID"));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
