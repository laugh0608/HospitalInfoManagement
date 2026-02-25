package com.graduation.hospital.config.log;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志数据库配置
 * 负责管理日志数据库连接和分表策略
 */
@Configuration
@ConditionalOnProperty(name = "logging.db.enabled", havingValue = "true", matchIfMissing = true)
public class LogDatabaseConfig {

    /** 分表模式 */
    public enum SplitMode {
        YEAR,   // 按年分表（sql_2026）
        MONTH,  // 按月分表（sql_202602）
        WEEK,   // 按周分表（sql_2026W01）
        DAY     // 按天分表（sql_20260225）
    }

    private final DataSource dataSource;

    /** 日志数据库路径 */
    public static final String LOG_DB_PATH = "db/hospital.log.db";

    public LogDatabaseConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 获取 SQL 日志表名
     * @param splitMode 分表模式
     * @return 表名，如 sql_20260225
     */
    public static String getSqlTableName(SplitMode splitMode) {
        return getTableName("sql", splitMode);
    }

    /**
     * 获取审计日志表名
     * @param splitMode 分表模式
     * @return 表名，如 audit_20260225
     */
    public static String getAuditTableName(SplitMode splitMode) {
        return getTableName("audit", splitMode);
    }

    /**
     * 获取访问日志表名
     * @param splitMode 分表模式
     * @return 表名，如 access_20260225
     */
    public static String getAccessTableName(SplitMode splitMode) {
        return getTableName("access", splitMode);
    }

    /**
     * 根据分表模式获取表名
     */
    private static String getTableName(String prefix, SplitMode splitMode) {
        LocalDate today = LocalDate.now();
        return switch (splitMode) {
            case YEAR -> prefix + "_" + today.format(DateTimeFormatter.ofPattern("yyyy"));
            case MONTH -> prefix + "_" + today.format(DateTimeFormatter.ofPattern("yyyyMM"));
            case WEEK -> {
                int weekOfYear = today.getDayOfYear() / 7 + 1;
                yield prefix + "_" + today.format(DateTimeFormatter.ofPattern("yyyy")) + "W" + String.format("%02d", weekOfYear);
            }
            case DAY -> prefix + "_" + today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        };
    }

    /**
     * 获取当前日期对应的表名（根据配置的分表模式）
     */
    public static Map<String, String> getCurrentTableNames(SplitMode splitMode) {
        Map<String, String> tables = new HashMap<>();
        tables.put("sql", getSqlTableName(splitMode));
        tables.put("audit", getAuditTableName(splitMode));
        tables.put("access", getAccessTableName(splitMode));
        return tables;
    }
}