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

        filterChain.doFilter(request, response);
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