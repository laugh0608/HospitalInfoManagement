package com.graduation.hospital.config.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 数据库日志 Appender 基类
 * 提供异步写入数据库的能力
 * 注意：此类为基础设施，实际使用请通过 LogManager
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "logging.db.enabled", havingValue = "true", matchIfMissing = true)
public class DatabaseLogAppender {

    /**
     * 异步写入日志（由 LogManager 统一管理）
     * 此方法作为占位符，实际日志写入通过 LogManager 处理
     */
    @Async
    public void asyncLog(Runnable task) {
        try {
            task.run();
        } catch (Exception e) {
            log.error("写入数据库日志失败: {}", e.getMessage(), e);
        }
    }
}