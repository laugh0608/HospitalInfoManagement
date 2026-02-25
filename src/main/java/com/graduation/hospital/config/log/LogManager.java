package com.graduation.hospital.config.log;

import com.graduation.hospital.entity.log.AccessLog;
import com.graduation.hospital.entity.log.AuditLog;
import com.graduation.hospital.entity.log.SqlLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 日志管理服务
 * 负责日志数据库的初始化、写入和管理
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "logging.db.enabled", havingValue = "true", matchIfMissing = true)
public class LogManager {

    /** 分表模式 */
    @Value("${logging.db.split-mode:DAY}")
    private String splitMode;

    /** 日志数据库路径 */
    private static final String LOG_DB_PATH = "db/hospital.log.db";

    private Connection logDbConnection;

    /**
     * 初始化日志数据库连接
     */
    private synchronized Connection getConnection() throws SQLException {
        if (logDbConnection == null || logDbConnection.isClosed()) {
            String url = "jdbc:sqlite:" + LOG_DB_PATH;
            logDbConnection = DriverManager.getConnection(url);
            // 启用 WAL 模式提高并发写入性能
            try (Statement stmt = logDbConnection.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL");
                stmt.execute("PRAGMA synchronous=NORMAL");
            }
        }
        return logDbConnection;
    }

    /**
     * 初始化日志表（如果不存在）
     */
    public void initTables() {
        try (Connection conn = getConnection()) {
            // 创建 SQL 日志表（带分表后缀）
            String sqlTable = getTableName("sql");
            String createSqlTable = """
                CREATE TABLE IF NOT EXISTS %s (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    log_time DATETIME NOT NULL,
                    thread TEXT,
                    sql_type TEXT,
                    sql_text TEXT,
                    duration INTEGER,
                    success INTEGER,
                    error_message TEXT,
                    username TEXT,
                    request_id TEXT
                )
                """.formatted(sqlTable);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createSqlTable);
                stmt.execute("CREATE INDEX IF NOT EXISTS idx_sql_log_time ON %s(log_time)".formatted(sqlTable));
            }

            // 创建审计日志表
            String auditTable = getTableName("audit");
            String createAuditTable = """
                CREATE TABLE IF NOT EXISTS %s (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    log_time DATETIME NOT NULL,
                    username TEXT,
                    user_id INTEGER,
                    action_type TEXT NOT NULL,
                    module TEXT,
                    description TEXT,
                    target TEXT,
                    ip TEXT,
                    method TEXT,
                    url TEXT,
                    success INTEGER,
                    error_message TEXT
                )
                """.formatted(auditTable);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createAuditTable);
                stmt.execute("CREATE INDEX IF NOT EXISTS idx_audit_log_time ON %s(log_time)".formatted(auditTable));
                stmt.execute("CREATE INDEX IF NOT EXISTS idx_audit_username ON %s(username)".formatted(auditTable));
            }

            // 创建访问日志表
            String accessTable = getTableName("access");
            String createAccessTable = """
                CREATE TABLE IF NOT EXISTS %s (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    request_time DATETIME NOT NULL,
                    request_id TEXT,
                    method TEXT NOT NULL,
                    url TEXT NOT NULL,
                    ip TEXT,
                    user_agent TEXT,
                    status INTEGER,
                    duration INTEGER,
                    username TEXT
                )
                """.formatted(accessTable);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createAccessTable);
                stmt.execute("CREATE INDEX IF NOT EXISTS idx_access_time ON %s(request_time)".formatted(accessTable));
            }

            log.info("日志数据库表初始化完成: sql={}, audit={}, access={}", sqlTable, auditTable, accessTable);

        } catch (SQLException e) {
            log.error("初始化日志数据库表失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取当前分表模式
     */
    private LogDatabaseConfig.SplitMode getSplitMode() {
        return LogDatabaseConfig.SplitMode.valueOf(splitMode.toUpperCase());
    }

    /**
     * 根据分表模式获取表名
     */
    private String getTableName(String prefix) {
        LocalDate today = LocalDate.now();
        return switch (getSplitMode()) {
            case YEAR -> prefix + "_" + today.format(DateTimeFormatter.ofPattern("yyyy"));
            case MONTH -> prefix + "_" + today.format(DateTimeFormatter.ofPattern("yyyyMM"));
            case WEEK -> {
                int weekOfYear = (today.getDayOfYear() - 1) / 7 + 1;
                yield prefix + "_" + today.format(DateTimeFormatter.ofPattern("yyyy")) + "W" + String.format("%02d", weekOfYear);
            }
            case DAY -> prefix + "_" + today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        };
    }

    /**
     * 异步写入 SQL 日志
     */
    @Async
    public void logSql(SqlLog sqlLog) {
        String table = getTableName("sql");
        String sql = """
            INSERT INTO %s (log_time, thread, sql_type, sql_text, duration, success, error_message, username, request_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """.formatted(table);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(sqlLog.getLogTime()));
            pstmt.setString(2, sqlLog.getThread());
            pstmt.setString(3, sqlLog.getSqlType());
            pstmt.setString(4, sqlLog.getSqlText());
            pstmt.setObject(5, sqlLog.getDuration());
            pstmt.setObject(6, sqlLog.getSuccess() ? 1 : 0);
            pstmt.setString(7, sqlLog.getErrorMessage());
            pstmt.setString(8, sqlLog.getUsername());
            pstmt.setString(9, sqlLog.getRequestId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("写入 SQL 日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 异步写入审计日志
     */
    @Async
    public void logAudit(AuditLog auditLog) {
        String table = getTableName("audit");
        String sql = """
            INSERT INTO %s (log_time, username, user_id, action_type, module, description, target, ip, method, url, success, error_message)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """.formatted(table);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(auditLog.getLogTime()));
            pstmt.setString(2, auditLog.getUsername());
            pstmt.setObject(3, auditLog.getUserId());
            pstmt.setString(4, auditLog.getActionType());
            pstmt.setString(5, auditLog.getModule());
            pstmt.setString(6, auditLog.getDescription());
            pstmt.setString(7, auditLog.getTarget());
            pstmt.setString(8, auditLog.getIp());
            pstmt.setString(9, auditLog.getMethod());
            pstmt.setString(10, auditLog.getUrl());
            pstmt.setObject(11, auditLog.getSuccess() ? 1 : 0);
            pstmt.setString(12, auditLog.getErrorMessage());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("写入审计日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 异步写入访问日志
     */
    @Async
    public void logAccess(AccessLog accessLog) {
        String table = getTableName("access");
        String sql = """
            INSERT INTO %s (request_time, request_id, method, url, ip, user_agent, status, duration, username)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """.formatted(table);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(accessLog.getRequestTime()));
            pstmt.setString(2, accessLog.getRequestId());
            pstmt.setString(3, accessLog.getMethod());
            pstmt.setString(4, accessLog.getUrl());
            pstmt.setString(5, accessLog.getIp());
            pstmt.setString(6, accessLog.getUserAgent());
            pstmt.setObject(7, accessLog.getStatus());
            pstmt.setObject(8, accessLog.getDuration());
            pstmt.setString(9, accessLog.getUsername());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("写入访问日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 定期检查并创建新表（每天凌晨执行）
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkAndCreateTables() {
        initTables();
        log.debug("日志表检查完成");
    }

    /**
     * 清理过期日志数据（可配置保留天数）
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanExpiredLogs() {
        // 可根据配置的天数清理旧数据
        log.info("日志清理任务执行");
    }
}