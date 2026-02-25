package com.graduation.hospital.common.log;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * HTTP 请求日志拦截器
 * 记录每个请求的详细信息，包括：
 * - 请求 ID（用于链路追踪）
 * - 请求 URL、Method
 * - 请求参数
 * - 响应状态码、耗时
 * - 客户端 IP
 */
@Slf4j
@Component
@Order(1)
public class RequestLoggingFilter implements Filter {

    private static final String REQUEST_ID = "requestId";
    private static final String START_TIME = "startTime";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();

        // 跳过 /api 路径（由 JwtAuthenticationFilter 记录）
        if (uri.startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 生成请求 ID
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(REQUEST_ID, requestId);
        httpResponse.setHeader("X-Request-Id", requestId);

        // 记录请求开始时间
        long startTime = System.currentTimeMillis();
        httpRequest.setAttribute(START_TIME, startTime);

        // 记录请求日志
        log.info("→ {} {} | IP: {} | UA: {}",
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                getClientIp(httpRequest),
                getUserAgent(httpRequest));

        try {
            chain.doFilter(request, response);
        } finally {
            // 计算耗时
            long duration = System.currentTimeMillis() - startTime;
            int status = httpResponse.getStatus();

            // 记录响应日志
            log.info("← {} {} | Status: {} | Duration: {}ms",
                    httpRequest.getMethod(),
                    httpRequest.getRequestURI(),
                    status,
                    duration);

            // 清理 MDC
            MDC.remove(REQUEST_ID);
        }
    }

    /**
     * 获取客户端真实 IP
     */
    private String getClientIp(HttpServletRequest request) {
        String[] headers = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // 多次代理时取第一个
                return ip.contains(",") ? ip.split(",")[0].trim() : ip;
            }
        }

        return request.getRemoteAddr();
    }

    /**
     * 获取用户代理
     */
    private String getUserAgent(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        if (ua == null || ua.isEmpty()) {
            return "-";
        }
        // 截取简化
        return ua.length() > 100 ? ua.substring(0, 100) + "..." : ua;
    }
}