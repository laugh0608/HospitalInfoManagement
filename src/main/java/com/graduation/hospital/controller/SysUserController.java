package com.graduation.hospital.controller;

import com.graduation.hospital.common.Result;
import com.graduation.hospital.common.audit.AuditLogger;
import com.graduation.hospital.common.util.JwtUtil;
import com.graduation.hospital.dto.AuthResponse;
import com.graduation.hospital.dto.LoginRequest;
import com.graduation.hospital.entity.SysRole;
import com.graduation.hospital.entity.SysUser;
import com.graduation.hospital.service.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;
    private final JwtUtil jwtUtil;
    private final AuditLogger auditLogger;
    private final HttpServletRequest httpServletRequest;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        String ip = httpServletRequest.getRemoteAddr();
        SysUser user;
        try {
            // 验证用户名密码
            user = userService.login(request.getUsername(), request.getPassword());
        } catch (Exception e) {
            // 登录失败审计
            auditLogger.logLoginFailed(request.getUsername(), ip, e.getMessage());
            throw e;
        }

        // 登录成功审计
        auditLogger.logLoginSuccess(user.getUsername(), user.getId(), ip);

        // 生成 JWT Token
        Set<String> roles = new HashSet<>();
        if (user.getRoles() != null) {
            for (SysRole role : user.getRoles()) {
                roles.add(role.getCode());
            }
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getId());

        AuthResponse response = AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .userId(user.getId())
                .roles(roles)
                .expiration(jwtUtil.getExpiration())
                .build();

        return Result.success(response);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<SysUser> register(@RequestBody SysUser user) {
        SysUser saved = userService.register(user);
        saved.setPassword(null);
        return Result.success(saved);
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/me")
    public Result<SysUser> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // principal 是 JwtAuthenticationFilter 中设置的 SysUser 对象
        SysUser principal = (SysUser) auth.getPrincipal();

        SysUser user = userService.getUserByUsername(principal.getUsername());
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public Result<SysUser> getUserById(@PathVariable Long id) {
        SysUser user = userService.getUserById(id);
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 获取所有用户（仅管理员）
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<SysUser>> getAllUsers() {
        List<SysUser> users = userService.getAllUsers();
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result<SysUser> updateUser(@PathVariable Long id, @RequestBody SysUser user) {
        SysUser updated = userService.updateUser(id, user);
        updated.setPassword(null);
        return Result.success(updated);
    }

    /**
     * 删除用户（仅管理员）
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check/username")
    public Result<Boolean> checkUsername(@RequestParam String username) {
        return Result.success(userService.existsByUsername(username));
    }

    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check/email")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        return Result.success(userService.existsByEmail(email));
    }

    /**
     * 启用用户（仅管理员）
     */
    @PostMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> enableUser(@PathVariable Long id) {
        userService.enableUser(id);
        return Result.success();
    }

    /**
     * 禁用用户（仅管理员）
     */
    @PostMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> disableUser(@PathVariable Long id) {
        userService.disableUser(id);
        return Result.success();
    }
}