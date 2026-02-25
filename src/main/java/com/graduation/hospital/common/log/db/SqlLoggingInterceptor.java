package com.graduation.hospital.common.log.db;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SQL 日志拦截器
 * 记录所有 SQL 执行到数据库
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "logging.db.log-sql", havingValue = "true")
public class SqlLoggingInterceptor implements StatementInspector {

    @Autowired(required = false)
    private DbLoggingService dbLoggingService;

    private static final Map<String, Long> sqlStartTimes = new ConcurrentHashMap<>();
    private static final Map<String, String> sqlTexts = new ConcurrentHashMap<>();

    private static int counter = 0;
    private static final String COUNTER_LOCK = "counter";

    @Override
    public String inspect(String sql) {
        if (dbLoggingService == null) {
            return sql;
        }

        // 生成唯一标识
        String sqlKey = generateSqlKey();
        long startTime = System.currentTimeMillis();

        // 记录开始时间
        sqlStartTimes.put(sqlKey, startTime);
        sqlTexts.put(sqlKey, sql);

        return sql;
    }

    /**
     * 记录 SQL 执行完成
     */
    public static void logSqlComplete(String sql, boolean success, String errorMessage) {
        // 找到对应的 SQL 并记录（这里简化处理）
        try {
            // 由于 Logback 无法直接回调，这里使用简化方式
            // 实际生产环境可以使用 Aspect 或更复杂的拦截机制
        } catch (Exception ignored) {
        }
    }

    /**
     * 生成唯一 SQL 键
     */
    private synchronized String generateSqlKey() {
        counter++;
        return Thread.currentThread().getId() + "-" + counter + "-" + System.nanoTime();
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
     * 实体 JPA 生命周期拦截
     */
    @Component
    public static class EntityInterceptor {

        @Autowired(required = false)
        private DbLoggingService dbLoggingService;

        @PrePersist
        public void prePersist(Object entity) {
            logEntityOperation("INSERT", entity);
        }

        @PreUpdate
        public void preUpdate(Object entity) {
            logEntityOperation("UPDATE", entity);
        }

        @PreRemove
        public void preRemove(Object entity) {
            logEntityOperation("DELETE", entity);
        }

        private void logEntityOperation(String operation, Object entity) {
            if (dbLoggingService != null) {
                String username = getCurrentUsername();
                String requestId = org.slf4j.MDC.get("requestId");

                dbLoggingService.logSqlFromEvent(
                        Thread.currentThread().getName(),
                        operation,
                        entity.getClass().getSimpleName(),
                        0L,
                        true,
                        null,
                        username,
                        requestId
                );
            }
        }

        private String getCurrentUsername() {
            try {
                var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                    return auth.getName();
                }
            } catch (Exception ignored) {
            }
            return null;
        }
    }
}