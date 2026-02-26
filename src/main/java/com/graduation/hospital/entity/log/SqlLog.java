package com.graduation.hospital.entity.log;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * SQL 日志数据对象
 * 存储 SQL 执行记录，由 DbLoggingService 写入独立日志库
 */
@Data
public class SqlLog {

    private Long id;

    /** 日志时间 */
    private LocalDateTime logTime;

    /** 线程名 */
    private String thread;

    /** SQL 语句类型（SELECT/INSERT/UPDATE/DELETE） */
    private String sqlType;

    /** SQL 语句 */
    private String sqlText;

    /** 耗时（毫秒） */
    private Long duration;

    /** 是否成功 */
    private Boolean success;

    /** 错误信息（如有） */
    private String errorMessage;

    /** 操作用户 */
    private String username;

    /** 请求 ID（链路追踪） */
    private String requestId;
}
