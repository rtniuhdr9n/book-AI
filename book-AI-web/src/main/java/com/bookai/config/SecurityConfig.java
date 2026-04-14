package com.bookai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println(">>> [DEBUG] SecurityConfig filterChain is building...");

        http
                // 1. 关闭 CSRF (前后端分离必须关闭，否则 POST 请求会被拦截)
                .csrf(csrf -> csrf.disable())

                // 2. 配置授权规则
                .authorizeHttpRequests(auth -> auth
                        // 【核心修复】显式指定 POST 方法的 /user/register 和 /user/login 放行
                        .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                        //swagger测试接口
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // 管理员专用接口示例（可以根据需要添加更多）
                        // .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        // VIP 用户接口示例
                        // .requestMatchers("/vip/**").access("@securityChecker.hasVipRole(request)")
                        // 3. 其他所有请求必须认证
                        .anyRequest().authenticated()
                )

                // 4. 配置异常处理 (返回 401 JSON 而不是跳转登录页)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"code\": 401, \"msg\": \"未授权访问\"}");
                        })
                )

                // 5. 设置无状态会话 (JWT 标准配置)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                


        System.out.println(">>> [DEBUG] SecurityFilterChain built successfully!");
        return http.build();
    }
}