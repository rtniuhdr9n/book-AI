package com.bookai.config;

import com.bookai.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/user/**", "/admin/**")
                .excludePathPatterns(
                        "/user/register",
                        "/user/login",
                        "/admin/login",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/user/book/**",
                        "/user/category/**",
                        "/user/forum/sections",
                        "/user/forum/section/**",
                        "/user/forum/posts",
                        "/user/forum/post/*"
                );
    }
}
