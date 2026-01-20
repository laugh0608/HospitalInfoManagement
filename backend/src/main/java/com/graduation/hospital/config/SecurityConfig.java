package com.graduation.hospital.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security 配置
 * 开发阶段：允许所有请求访问，但配置 CSRF 和 CORS 保护
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // 开发阶段允许所有请求
            )
            .csrf(csrf -> csrf
                // 针对非安全方法（GET, HEAD, TRACE, OPTIONS）不需要 CSRF token
                // 对于修改数据的方法（POST, PUT, DELETE, PATCH）需要 CSRF token
                // 可以通过 Cookie 自动处理
                .ignoringRequestMatchers("/api/hello")  // 测试接口暂时忽略 CSRF
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    /**
     * CORS 配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 允许的源（开发环境）
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",      // 前端开发服务器
            "http://127.0.0.1:3000"       // 本地环回地址
        ));

        // 允许的 HTTP 方法
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // 允许的请求头
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "X-XSRF-TOKEN"  // CSRF token 头
        ));

        // 暴露的响应头
        configuration.setExposedHeaders(Arrays.asList(
            "X-XSRF-TOKEN"
        ));

        // 是否允许发送 Cookie
        configuration.setAllowCredentials(true);

        // 预检请求的缓存时间（秒）
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }
}
