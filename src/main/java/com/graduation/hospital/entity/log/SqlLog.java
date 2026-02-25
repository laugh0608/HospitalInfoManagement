package com.graduation.hospital.entity.log;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * SQL 日志实体
 * 存储 SQL 执行记录（分表：sql_YYYYMM 或 sql_YYYYMMDD）
 */
@Data
@Entity
@Table(name = "sql_log")
public class SqlLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 日志时间 */
    @Column(name = "log_time", nullable = false)
    private LocalDateTime logTime;

    /** 线程名 */
    @Column(name = "thread")
    private String thread;

    /** SQL 语句类型（SELECT/INSERT/UPDATE/DELETE） */
    @Column(name = "sql_type")
    private String sqlType;

    /** SQL 语句 */
    @Column(name = "sql_text", columnDefinition = "TEXT")
    private String sqlText;

    /** 耗时（毫秒） */
    @Column(name = "duration")
    private Long duration;

    /** 是否成功 */
    @Column(name = "success")
    private Boolean success;

    /** 错误信息（如有） */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    /** 操作用户 */
    @Column(name = "username")
    private String username;

    /** 请求 ID（链路追踪） */
    @Column(name = "request_id")
    private String requestId;
}