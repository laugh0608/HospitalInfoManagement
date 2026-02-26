package com.graduation.hospital.config;

import com.graduation.hospital.common.util.JwtUtil;
import com.graduation.hospital.entity.SysRole;
import com.graduation.hospital.entity.SysUser;
import com.graduation.hospital.repository.SysUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


/**
 * JWT 认证过滤器
 * 拦截请求，验证 JWT Token，设置认证信息
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final SysUserRepository userRepository;

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 记录请求日志
        log.info("→ {} {} | IP: {} | UA: {}",
                request.getMethod(),
                request.getRequestURI(),
                getClientIp(request),
                getUserAgent(request));

        long startTime = System.currentTimeMillis();

        String authHeader = request.getHeader(AUTH_HEADER);

        // 检查是否包含有效的 Token
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            String token = authHeader.substring(TOKEN_PREFIX.length());

            try {
                // 验证 Token
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.getUsernameFromToken(token);

                    // 从数据库获取用户信息
                    SysUser user = userRepository.findByUsername(username).orElse(null);

                    if (user != null && user.getEnabled()) {
                        // 构建权限列表
                        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

                        // 添加角色权限
                        if (user.getRoles() != null) {
                            for (SysRole role : user.getRoles()) {
                                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
                            }
                        }

                        // 创建认证对象
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(user, null, authorities);

                        // 设置到安全上下文
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        log.debug("JWT 认证成功: username={}", username);
                    }
                }
            } catch (Exception e) {
                log.warn("JWT Token 验证失败: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // 记录响应日志
            long duration = System.currentTimeMillis() - startTime;
            log.info("← {} {} | Status: {} | Duration: {}ms",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration);
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
        return ua.length() > 100 ? ua.substring(0, 100) + "..." : ua;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 跳过不需要认证的路径
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/hello")
                || path.startsWith("/api/v2/hello")
                || path.startsWith("/api/hello")
                || path.startsWith("/api/v1/users/register")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/scalar")
                || path.startsWith("/v3/api-docs");
    }
}