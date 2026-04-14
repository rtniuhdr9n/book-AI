package com.bookai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 1. 设置允许跨域的路径，"/**" 表示允许所有路径
        registry.addMapping("/**")
                // 2. 设置允许跨域的源 (前端地址)
                // 开发环境通常是 http://localhost:5173 (Vite) 或 http://localhost:8080 (其他)
                // 生产环境填你的域名，如 "https://www.mywebsite.com"
                // ⚠️ 注意：如果允许携带 Cookie (allowCredentials=true)，这里不能写 "*"，必须写具体域名
                .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173")

                // 3. 设置允许的请求方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

                // 4. 允许发送的请求头 (Header)
                .allowedHeaders("*")

                // 5. 是否允许携带 Cookie / 认证信息
                // 如果你的登录需要保存 Session 或 Token 在 Cookie 中，这里必须为 true
                .allowCredentials(true)

                // 6. 预检请求 (OPTIONS) 的缓存时间 (秒)
                // 避免每次请求都先发一个 OPTIONS 询问，提高性能
                .maxAge(3600);
    }
}