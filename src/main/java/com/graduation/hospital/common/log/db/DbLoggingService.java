package com.graduation.hospital.common.log.db;

import com.graduation.hospital.entity.log.AccessLog;
import com.graduation.hospital.entity.log.AuditLog;
import com.graduation.hospital.entity.log.SqlLog;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 数据库日志服务
 * 将日志写入独立的 SQLite 数据库，支持按天/周/月/年分表
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "logging.db.enabled", havingValue = "true")
public class DbLoggingService {

    /**
     * 分表模式
     */
    public enum SplitMode {
        YEAR,   // 按年分表：sql_2026
        MONTH,  // 按月分表：sql_202602
        WEEK,   // 按周分表：sql_2026W01
        DAY     // 按天分表：sql_20260225
    }

    @Value("${logging.db.split-mode:DAY}")
    private String splitMode;

    @Value("${logging.db.database-path:db/hospital.log.db}")
    private String databasePath;

    @Value("${logging.db.batch-size:100}")
    private int batchSize;

    @Value("${logging.db.flush-interval:5000}")
    private long flushInterval;

    @Value("${logging.db.log-sql:true}")
    private boolean logSql;

    @Value("${logging.db.log-audit:true}")
    private boolean logAudit;

    @Value("${logging.db.log-access:true}")
    private boolean logAccess;

    private Connection connection;
    private final Map<String, List<Object>> sqlLogBuffer = new ConcurrentHashMap<>();
    private final Map<String, List<Object>> auditLogBuffer = new ConcurrentHashMap<>();
    private final Map<String, List<Object>> accessLogBuffer = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final AtomicBoolean running = new AtomicBoolean(true);
    private ScheduledFuture<?> flushTask;

    @PostConstruct
    public void init() {
        try {
            // 加载 SQLite 驱动
            Class.forName("org.sqlite.JDBC");

            // 确保目录存在
            String dbDir = databasePath.substring(0, databasePath.lastIndexOf('/'));
            if (dbDir.length() > 0) {
                java.nio.file.Path dir = java.nio.file.Paths.get(dbDir);
                if (!java.nio.file.Files.exists(dir)) {
                    java.nio.file.Files.createDirectories(dir);
                }
            }

            // 创建数据库连接
            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            connection.setAutoCommit(false);

            // 创建表结构
            createTables();

            log.info("数据库日志表创建完成");

            // 启动定时刷新任务
            flushTask = executor.scheduleAtFixedRate(
                    this::flush,
                    flushInterval,
                    flushInterval,
                    TimeUnit.MILLISECONDS
            );

            log.info("数据库日志服务初始化完成: database={}, splitMode={}", databasePath, splitMode);
        } catch (Exception e) {
            log.error("数据库日志服务初始化失败", e);
        }
    }

    @PreDestroy
    public void destroy() {
        running.set(false);
        if (flushTask != null) {
            flushTask.cancel(false);
        }
        flush(); // 确保所有日志都已刷新
        executor.shutdown();
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            log.error("关闭数据库连接失败", e);
        }
    }

    /**
     * 根据分表模式获取表名
     */
    public String getTableName(String prefix, LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        String tableName = switch (SplitMode.valueOf(splitMode)) {
            case YEAR -> prefix + "_" + dateTime.format(DateTimeFormatter.ofPattern("yyyy"));
            case MONTH -> prefix + "_" + dateTime.format(DateTimeFormatter.ofPattern("yyyyMM"));
            case WEEK -> {
                int weekOfYear = date.getDayOfYear() / 7 + 1;
                yield prefix + "_" + dateTime.format(DateTimeFormatter.ofPattern("yyyy")) + "W" + String.format("%02d", weekOfYear);
            }
            case DAY -> prefix + "_" + dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        };
        return tableName;
    }

    /**
     * 创建表结构
     */
    private void createTables() throws SQLException {
        String[] tableNames = {"sql_log", "audit_log", "access_log"};
        String[] createStatements = {
                "CREATE TABLE IF NOT EXISTS sql_log (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "log_time TEXT NOT NULL, " +
                    "thread TEXT, " +
                    "sql_type TEXT, " +
                    "sql_text TEXT, " +
                    "duration INTEGER, " +
                    "success INTEGER, " +
                    "error_message TEXT, " +
                    "username TEXT, " +
                    "request_id TEXT)",
                "CREATE TABLE IF NOT EXISTS audit_log (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "log_time TEXT NOT NULL, " +
                    "username TEXT, " +
                    "user_id INTEGER, " +
                    "action_type TEXT NOT NULL, " +
                    "module TEXT, " +
                    "description TEXT, " +
                    "target TEXT, " +
                    "ip TEXT, " +
                    "method TEXT, " +
                    "url TEXT, " +
                    "success INTEGER, " +
                    "error_message TEXT)",
                "CREATE TABLE IF NOT EXISTS access_log (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "request_time TEXT NOT NULL, " +
                    "request_id TEXT, " +
                    "method TEXT NOT NULL, " +
                    "url TEXT NOT NULL, " +
                    "ip TEXT, " +
                    "user_agent TEXT, " +
                    "status INTEGER, " +
                    "duration INTEGER, " +
                    "username TEXT)"
        };

        try (Statement stmt = connection.createStatement()) {
            for (String sql : createStatements) {
                stmt.execute(sql);
            }
            connection.commit();
        }
    }

    /**
     * 记录 SQL 日志
     */
    public void logSql(SqlLog sqlLog) {
        if (!logSql || !running.get()) return;

        String tableName = getTableName("sql_log", sqlLog.getLogTime());
        sqlLogBuffer.computeIfAbsent(tableName, k -> Collections.synchronizedList(new ArrayList<>())).add(sqlLog);

        if (sqlLogBuffer.get(tableName).size() >= batchSize) {
            flushTable(tableName, sqlLogBuffer);
        }
    }

    /**
     * 记录审计日志
     */
    public void logAudit(AuditLog auditLog) {
        if (!logAudit || !running.get()) return;

        String tableName = getTableName("audit_log", auditLog.getLogTime());
        auditLogBuffer.computeIfAbsent(tableName, k -> Collections.synchronizedList(new ArrayList<>())).add(auditLog);

        if (auditLogBuffer.get(tableName).size() >= batchSize) {
            flushTable(tableName, auditLogBuffer);
        }
    }

    /**
     * 记录访问日志
     */
    public void logAccess(AccessLog accessLog) {
        if (!logAccess || !running.get()) return;

        String tableName = getTableName("access_log", accessLog.getRequestTime());
        log.debug("记录访问日志: tableName={}, url={}", tableName, accessLog.getUrl());
        accessLogBuffer.computeIfAbsent(tableName, k -> Collections.synchronizedList(new ArrayList<>())).add(accessLog);

        if (accessLogBuffer.get(tableName).size() >= batchSize) {
            log.debug("缓冲区已满，刷新表: {}", tableName);
            flushTable(tableName, accessLogBuffer);
        }
    }

    /**
     * 刷新所有缓冲日志
     */
    public synchronized void flush() {
        log.debug("开始刷新缓冲区: sql_log={}, audit_log={}, access_log={}",
                sqlLogBuffer.size(), auditLogBuffer.size(), accessLogBuffer.size());

        // 刷新所有表
        sqlLogBuffer.keySet().forEach(tableName -> flushTable(tableName, sqlLogBuffer));
        auditLogBuffer.keySet().forEach(tableName -> flushTable(tableName, auditLogBuffer));
        accessLogBuffer.keySet().forEach(tableName -> flushTable(tableName, accessLogBuffer));
    }

    /**
     * 刷新指定表的日志
     * @param actualTableName 实际的分表名，如 access_log_20260226
     * @param buffer 对应的日志缓冲区
     */
    private void flushTable(String actualTableName, Map<String, List<Object>> buffer) {
        List<Object> list = buffer.get(actualTableName);
        if (list == null || list.isEmpty()) {
            log.debug("缓冲区为空: {}", actualTableName);
            return;
        }

        // 从分表名提取基础表名，用于建表和参数设置
        String baseTableName = getBaseTableName(actualTableName);

        synchronized (list) {
            if (list.isEmpty()) return;

            try {
                // 创建分表（如果不存在）
                ensureTableExists(baseTableName, actualTableName);

                // 批量插入到分表
                String sql = getInsertSql(actualTableName);
                log.debug("执行SQL: {}", sql);
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    for (Object obj : list) {
                        setInsertParams(pstmt, baseTableName, obj);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }

                connection.commit();
                log.info("成功写入 {} 条日志到表: {}", list.size(), actualTableName);
                list.clear();
            } catch (Exception e) {
                log.error("写入日志到表 {} 失败: {}", actualTableName, e.getMessage(), e);
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    log.error("回滚事务失败", ex);
                }
            }
        }
    }

    /**
     * 确保表存在（分表场景）
     */
    private void ensureTableExists(String type, String tableName) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // 检查表是否存在
            ResultSet rs = stmt.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'");
            if (!rs.next()) {
                // 创建表
                String createSql = getCreateTableSql(type, tableName);
                stmt.execute(createSql);
            }
        }
    }

    /**
     * 获取创建表的 SQL
     */
    private String getCreateTableSql(String type, String tableName) {
        return switch (type) {
            case "sql_log" -> "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "log_time TEXT NOT NULL, " +
                    "thread TEXT, " +
                    "sql_type TEXT, " +
                    "sql_text TEXT, " +
                    "duration INTEGER, " +
                    "success INTEGER, " +
                    "error_message TEXT, " +
                    "username TEXT, " +
                    "request_id TEXT)";
            case "audit_log" -> "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "log_time TEXT NOT NULL, " +
                    "username TEXT, " +
                    "user_id INTEGER, " +
                    "action_type TEXT NOT NULL, " +
                    "module TEXT, " +
                    "description TEXT, " +
                    "target TEXT, " +
                    "ip TEXT, " +
                    "method TEXT, " +
                    "url TEXT, " +
                    "success INTEGER, " +
                    "error_message TEXT)";
            case "access_log" -> "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "request_time TEXT NOT NULL, " +
                    "request_id TEXT, " +
                    "method TEXT NOT NULL, " +
                    "url TEXT NOT NULL, " +
                    "ip TEXT, " +
                    "user_agent TEXT, " +
                    "status INTEGER, " +
                    "duration INTEGER, " +
                    "username TEXT)";
            default -> throw new IllegalArgumentException("Unknown log type: " + type);
        };
    }

    /**
     * 从分表名中提取基础表名
     * 例如：access_log_20260226 -> access_log
     */
    private String getBaseTableName(String tableName) {
        if (tableName.startsWith("sql_log")) return "sql_log";
        if (tableName.startsWith("audit_log")) return "audit_log";
        if (tableName.startsWith("access_log")) return "access_log";
        throw new IllegalArgumentException("Unknown log table: " + tableName);
    }

    /**
     * 获取插入 SQL
     */
    private String getInsertSql(String tableName) {
        if (tableName.startsWith("sql_log")) {
            return "INSERT INTO " + tableName + " (log_time, thread, sql_type, sql_text, duration, success, error_message, username, request_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else if (tableName.startsWith("audit_log")) {
            return "INSERT INTO " + tableName + " (log_time, username, user_id, action_type, module, description, target, ip, method, url, success, error_message) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else if (tableName.startsWith("access_log")) {
            return "INSERT INTO " + tableName + " (request_time, request_id, method, url, ip, user_agent, status, duration, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }
        throw new IllegalArgumentException("Unknown log table: " + tableName);
    }

    /**
     * 设置插入参数
     */
    private void setInsertParams(PreparedStatement pstmt, String type, Object obj) throws SQLException {
        if (obj instanceof SqlLog sqlLog) {
            pstmt.setString(1, sqlLog.getLogTime() != null ? sqlLog.getLogTime().toString() : null);
            pstmt.setString(2, sqlLog.getThread());
            pstmt.setString(3, sqlLog.getSqlType());
            pstmt.setString(4, sqlLog.getSqlText());
            pstmt.setObject(5, sqlLog.getDuration());
            pstmt.setObject(6, sqlLog.getSuccess() != null ? (sqlLog.getSuccess() ? 1 : 0) : null);
            pstmt.setString(7, sqlLog.getErrorMessage());
            pstmt.setString(8, sqlLog.getUsername());
            pstmt.setString(9, sqlLog.getRequestId());
        } else if (obj instanceof AuditLog auditLog) {
            pstmt.setString(1, auditLog.getLogTime() != null ? auditLog.getLogTime().toString() : null);
            pstmt.setString(2, auditLog.getUsername());
            pstmt.setObject(3, auditLog.getUserId());
            pstmt.setString(4, auditLog.getActionType());
            pstmt.setString(5, auditLog.getModule());
            pstmt.setString(6, auditLog.getDescription());
            pstmt.setString(7, auditLog.getTarget());
            pstmt.setString(8, auditLog.getIp());
            pstmt.setString(9, auditLog.getMethod());
            pstmt.setString(10, auditLog.getUrl());
            pstmt.setObject(11, auditLog.getSuccess() != null ? (auditLog.getSuccess() ? 1 : 0) : null);
            pstmt.setString(12, auditLog.getErrorMessage());
        } else if (obj instanceof AccessLog accessLog) {
            pstmt.setString(1, accessLog.getRequestTime() != null ? accessLog.getRequestTime().toString() : null);
            pstmt.setString(2, accessLog.getRequestId());
            pstmt.setString(3, accessLog.getMethod());
            pstmt.setString(4, accessLog.getUrl());
            pstmt.setString(5, accessLog.getIp());
            pstmt.setString(6, accessLog.getUserAgent());
            pstmt.setObject(7, accessLog.getStatus());
            pstmt.setObject(8, accessLog.getDuration());
            pstmt.setString(9, accessLog.getUsername());
        }
    }

    // ==================== 日志记录便捷方法 ====================

    /**
     * 记录 SQL 日志（从日志事件中提取）
     */
    public void logSqlFromEvent(String thread, String sqlType, String sqlText,
                                Long duration, Boolean success, String errorMessage,
                                String username, String requestId) {
        SqlLog sqlLog = new SqlLog();
        sqlLog.setLogTime(LocalDateTime.now());
        sqlLog.setThread(thread);
        sqlLog.setSqlType(sqlType);
        sqlLog.setSqlText(sqlText);
        sqlLog.setDuration(duration);
        sqlLog.setSuccess(success);
        sqlLog.setErrorMessage(errorMessage);
        sqlLog.setUsername(username);
        sqlLog.setRequestId(requestId);
        logSql(sqlLog);
    }

    /**
     * 记录审计日志（从日志事件中提取）
     */
    public void logAuditFromEvent(String username, Long userId, String actionType,
                                  String module, String description, String target,
                                  String ip, String method, String url,
                                  Boolean success, String errorMessage) {
        AuditLog auditLog = new AuditLog();
        auditLog.setLogTime(LocalDateTime.now());
        auditLog.setUsername(username);
        auditLog.setUserId(userId);
        auditLog.setActionType(actionType);
        auditLog.setModule(module);
        auditLog.setDescription(description);
        auditLog.setTarget(target);
        auditLog.setIp(ip);
        auditLog.setMethod(method);
        auditLog.setUrl(url);
        auditLog.setSuccess(success);
        auditLog.setErrorMessage(errorMessage);
        logAudit(auditLog);
    }

    /**
     * 记录访问日志（从日志事件中提取）
     */
    public void logAccessFromEvent(String requestId, String method, String url,
                                   String ip, String userAgent, Integer status,
                                   Long duration, String username) {
        AccessLog accessLog = new AccessLog();
        accessLog.setRequestTime(LocalDateTime.now());
        accessLog.setRequestId(requestId);
        accessLog.setMethod(method);
        accessLog.setUrl(url);
        accessLog.setIp(ip);
        accessLog.setUserAgent(userAgent);
        accessLog.setStatus(status);
        accessLog.setDuration(duration);
        accessLog.setUsername(username);
        logAccess(accessLog);
    }
}