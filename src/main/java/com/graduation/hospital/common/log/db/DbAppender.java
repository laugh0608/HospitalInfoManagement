package com.graduation.hospital.common.log.db;

import ch.qos.logback.core.AsyncAppenderBase;

/**
 * Logback 数据库 Appender
 * 异步将日志写入 SQLite 数据库
 * 注意：此类作为 Spring 组件加载，但在 Logback 初始化时通过静态方式获取 Bean
 */
public class DbAppender extends AsyncAppenderBase<Object> {

    public DbAppender() {
        // 设置队列大小和丢弃阈值
        setQueueSize(512);
        setDiscardingThreshold(0);
    }

    @Override
    protected void append(Object eventObject) {
        try {
            DbLoggingService dbLoggingService = SpringContextHolder.getBean(DbLoggingService.class);
            if (dbLoggingService == null) {
                return;
            }

            // 将日志事件转换为对应的日志实体并写入数据库
            if (eventObject instanceof SqlLogEvent sqlLogEvent) {
                dbLoggingService.logSqlFromEvent(
                        sqlLogEvent.getThread(),
                        sqlLogEvent.getSqlType(),
                        sqlLogEvent.getSqlText(),
                        sqlLogEvent.getDuration(),
                        sqlLogEvent.getSuccess(),
                        sqlLogEvent.getErrorMessage(),
                        sqlLogEvent.getUsername(),
                        sqlLogEvent.getRequestId()
                );
            } else if (eventObject instanceof AuditLogEvent auditLogEvent) {
                dbLoggingService.logAuditFromEvent(
                        auditLogEvent.getUsername(),
                        auditLogEvent.getUserId(),
                        auditLogEvent.getActionType(),
                        auditLogEvent.getModule(),
                        auditLogEvent.getDescription(),
                        auditLogEvent.getTarget(),
                        auditLogEvent.getIp(),
                        auditLogEvent.getMethod(),
                        auditLogEvent.getUrl(),
                        auditLogEvent.getSuccess(),
                        auditLogEvent.getErrorMessage()
                );
            } else if (eventObject instanceof AccessLogEvent accessLogEvent) {
                dbLoggingService.logAccessFromEvent(
                        accessLogEvent.getRequestId(),
                        accessLogEvent.getMethod(),
                        accessLogEvent.getUrl(),
                        accessLogEvent.getIp(),
                        accessLogEvent.getUserAgent(),
                        accessLogEvent.getStatus(),
                        accessLogEvent.getDuration(),
                        accessLogEvent.getUsername()
                );
            }
        } catch (Exception e) {
            // 静默处理，避免递归日志
        }
    }

    @Override
    public void stop() {
        super.stop();
        // 刷新所有待写入的日志
        try {
            DbLoggingService dbLoggingService = SpringContextHolder.getBean(DbLoggingService.class);
            if (dbLoggingService != null) {
                dbLoggingService.flush();
            }
        } catch (Exception ignored) {
        }
    }

    // ==================== 日志事件类 ====================

    /**
     * SQL 日志事件
     */
    public static class SqlLogEvent {
        private final String thread;
        private final String sqlType;
        private final String sqlText;
        private final Long duration;
        private final Boolean success;
        private final String errorMessage;
        private final String username;
        private final String requestId;

        public SqlLogEvent(String thread, String sqlType, String sqlText, Long duration,
                          Boolean success, String errorMessage, String username, String requestId) {
            this.thread = thread;
            this.sqlType = sqlType;
            this.sqlText = sqlText;
            this.duration = duration;
            this.success = success;
            this.errorMessage = errorMessage;
            this.username = username;
            this.requestId = requestId;
        }

        // Getters
        public String getThread() { return thread; }
        public String getSqlType() { return sqlType; }
        public String getSqlText() { return sqlText; }
        public Long getDuration() { return duration; }
        public Boolean getSuccess() { return success; }
        public String getErrorMessage() { return errorMessage; }
        public String getUsername() { return username; }
        public String getRequestId() { return requestId; }
    }

    /**
     * 审计日志事件
     */
    public static class AuditLogEvent {
        private final String username;
        private final Long userId;
        private final String actionType;
        private final String module;
        private final String description;
        private final String target;
        private final String ip;
        private final String method;
        private final String url;
        private final Boolean success;
        private final String errorMessage;

        public AuditLogEvent(String username, Long userId, String actionType, String module,
                           String description, String target, String ip, String method,
                           String url, Boolean success, String errorMessage) {
            this.username = username;
            this.userId = userId;
            this.actionType = actionType;
            this.module = module;
            this.description = description;
            this.target = target;
            this.ip = ip;
            this.method = method;
            this.url = url;
            this.success = success;
            this.errorMessage = errorMessage;
        }

        // Getters
        public String getUsername() { return username; }
        public Long getUserId() { return userId; }
        public String getActionType() { return actionType; }
        public String getModule() { return module; }
        public String getDescription() { return description; }
        public String getTarget() { return target; }
        public String getIp() { return ip; }
        public String getMethod() { return method; }
        public String getUrl() { return url; }
        public Boolean getSuccess() { return success; }
        public String getErrorMessage() { return errorMessage; }
    }

    /**
     * 访问日志事件
     */
    public static class AccessLogEvent {
        private final String requestId;
        private final String method;
        private final String url;
        private final String ip;
        private final String userAgent;
        private final Integer status;
        private final Long duration;
        private final String username;

        public AccessLogEvent(String requestId, String method, String url, String ip,
                            String userAgent, Integer status, Long duration, String username) {
            this.requestId = requestId;
            this.method = method;
            this.url = url;
            this.ip = ip;
            this.userAgent = userAgent;
            this.status = status;
            this.duration = duration;
            this.username = username;
        }

        // Getters
        public String getRequestId() { return requestId; }
        public String getMethod() { return method; }
        public String getUrl() { return url; }
        public String getIp() { return ip; }
        public String getUserAgent() { return userAgent; }
        public Integer getStatus() { return status; }
        public Long getDuration() { return duration; }
        public String getUsername() { return username; }
    }
}