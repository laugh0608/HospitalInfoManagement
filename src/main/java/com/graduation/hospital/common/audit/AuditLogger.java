package com.graduation.hospital.common.audit;

import com.graduation.hospital.entity.SysUser;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 审计日志记录器
 * 记录用户的关键操作行为：
 * - 登录/登出
 * - 数据创建、修改、删除
 * - 敏感操作（权限修改等）
 */
@Slf4j
@Component
public class AuditLogger {

    /**
     * 操作类型枚举
     */
    public enum ActionType {
        LOGIN("用户登录"),
        LOGOUT("用户登出"),
        CREATE("创建"),
        UPDATE("更新"),
        DELETE("删除"),
        QUERY("查询"),
        EXPORT("导出"),
        IMPORT("导入"),
        LOGIN_FAILED("登录失败"),
        PASSWORD_CHANGE("修改密码"),
        PERMISSION_CHANGE("权限变更");

        private final String description;

        ActionType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 审计日志内容
     */
    @Data
    @Builder
    public static class AuditLog {
        /** 操作时间 */
        private LocalDateTime timestamp;
        /** 操作用户 */
        private String username;
        /** 用户 ID */
        private Long userId;
        /** 操作类型 */
        private ActionType actionType;
        /** 操作模块 */
        private String module;
        /** 操作描述 */
        private String description;
        /** 目标对象（如：patientId=1） */
        private String target;
        /** 请求 IP */
        private String ip;
        /** 请求方法 */
        private String method;
        /** 请求 URL */
        private String url;
        /** 是否成功 */
        private boolean success;
        /** 错误信息（如有） */
        private String errorMessage;
    }

    /**
     * 记录审计日志
     */
    public void log(AuditLog auditLog) {
        // 构建日志内容
        StringBuilder sb = new StringBuilder();
        sb.append("AUDIT | ");
        sb.append("用户: ").append(auditLog.getUsername()).append(" | ");
        sb.append("操作: ").append(auditLog.getActionType().getDescription()).append(" | ");
        sb.append("模块: ").append(auditLog.getModule()).append(" | ");
        sb.append("描述: ").append(auditLog.getDescription());

        if (auditLog.getTarget() != null) {
            sb.append(" | 目标: ").append(auditLog.getTarget());
        }

        if (!auditLog.isSuccess()) {
            sb.append(" | 失败: ").append(auditLog.getErrorMessage());
        }

        // 记录日志
        if (auditLog.isSuccess()) {
            log.info(sb.toString());
        } else {
            log.warn(sb.toString());
        }
    }

    /**
     * 记录登录成功
     */
    public void logLoginSuccess(String username, Long userId, String ip) {
        log(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .username(username)
                .userId(userId)
                .actionType(ActionType.LOGIN)
                .module("认证")
                .description("用户登录成功")
                .ip(ip)
                .success(true)
                .build());
    }

    /**
     * 记录登录失败
     */
    public void logLoginFailed(String username, String ip, String reason) {
        log(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .username(username)
                .actionType(ActionType.LOGIN_FAILED)
                .module("认证")
                .description("用户登录失败")
                .ip(ip)
                .success(false)
                .errorMessage(reason)
                .build());
    }

    /**
     * 记录登出
     */
    public void logLogout(String username, Long userId, String ip) {
        log(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .username(username)
                .userId(userId)
                .actionType(ActionType.LOGOUT)
                .module("认证")
                .description("用户退出登录")
                .ip(ip)
                .success(true)
                .build());
    }

    /**
     * 记录业务操作
     */
    public void logAction(ActionType actionType, String module, String description,
                          String target, boolean success, String errorMessage) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anonymous";
        Long userId = getCurrentUserId();

        log(AuditLog.builder()
                .timestamp(LocalDateTime.now())
                .username(username)
                .userId(userId)
                .actionType(actionType)
                .module(module)
                .description(description)
                .target(target)
                .success(success)
                .errorMessage(errorMessage)
                .build());
    }

    /**
     * 记录创建操作
     */
    public void logCreate(String module, String description, Long targetId) {
        logAction(ActionType.CREATE, module, description,
                targetId != null ? "id=" + targetId : null, true, null);
    }

    /**
     * 记录更新操作
     */
    public void logUpdate(String module, String description, Long targetId) {
        logAction(ActionType.UPDATE, module, description,
                targetId != null ? "id=" + targetId : null, true, null);
    }

    /**
     * 记录删除操作
     */
    public void logDelete(String module, String description, Long targetId) {
        logAction(ActionType.DELETE, module, description,
                targetId != null ? "id=" + targetId : null, true, null);
    }

    /**
     * 获取当前登录用户 ID
     */
    private Long getCurrentUserId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof SysUser user) {
                return user.getId();
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}