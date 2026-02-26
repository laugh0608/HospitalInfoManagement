package com.graduation.hospital.common.log.db;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.resource.jdbc.spi.StatementInspector;

/**
 * SQL 日志拦截器
 * 通过 Hibernate statement_inspector 配置，由 Hibernate 实例化（非 Spring 管理），
 * 因此通过 SpringContextHolder 延迟获取 DbLoggingService。
 */
@Slf4j
public class SqlLoggingInterceptor implements StatementInspector {

    private volatile DbLoggingService dbLoggingService;

    @Override
    public String inspect(String sql) {
        try {
            DbLoggingService service = getDbLoggingService();
            if (service == null) {
                return sql;
            }

            String sqlType = extractSqlType(sql);
            String username = getCurrentUsername();
            String requestId = org.slf4j.MDC.get("requestId");

            // StatementInspector 只有执行前回调，无法精确测量耗时，duration 记为 null
            service.logSqlFromEvent(
                    Thread.currentThread().getName(),
                    sqlType,
                    sql,
                    null,
                    true,
                    null,
                    username,
                    requestId
            );
        } catch (Exception e) {
            // 静默处理，避免影响正常 SQL 执行
            log.debug("SQL 日志记录失败: {}", e.getMessage());
        }

        return sql;
    }

    /**
     * 延迟获取 DbLoggingService
     */
    private DbLoggingService getDbLoggingService() {
        if (dbLoggingService == null) {
            try {
                dbLoggingService = SpringContextHolder.getBean(DbLoggingService.class);
            } catch (Exception e) {
                // Spring 上下文尚未初始化，静默忽略
            }
        }
        return dbLoggingService;
    }

    /**
     * 从 SQL 中提取类型
     */
    private String extractSqlType(String sql) {
        if (sql == null) return "UNKNOWN";
        String trimmed = sql.trim().toUpperCase();
        if (trimmed.startsWith("SELECT")) return "SELECT";
        if (trimmed.startsWith("INSERT")) return "INSERT";
        if (trimmed.startsWith("UPDATE")) return "UPDATE";
        if (trimmed.startsWith("DELETE")) return "DELETE";
        return "OTHER";
    }

    /**
     * 获取当前登录用户名
     */
    private String getCurrentUsername() {
        try {
            var auth = org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()
                    && !"anonymousUser".equals(auth.getPrincipal())) {
                return auth.getName();
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
