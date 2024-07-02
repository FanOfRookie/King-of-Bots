package com.kob.backend.config.intercepter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    @Autowired
    private MDCIntercepter mdcIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mdcIntercepter)
                .addPathPatterns("/**"); // 拦截所有请求
//                .excludePathPatterns("/login", "/register"); // 排除不需要拦截的请求
    }
}
