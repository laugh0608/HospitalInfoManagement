package com.graduation.hospital.entity.log;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 访问日志实体
 * 存储 HTTP 请求访问记录（分表：access_YYYYMM 或 access_YYYYMMDD）
 */
@Data
@Entity
@Table(name = "access_log")
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 请求时间 */
    @Column(name = "request_time", nullable = false)
    private LocalDateTime requestTime;

    /** 请求 ID（链路追踪） */
    @Column(name = "request_id")
    private String requestId;

    /** 请求方法（GET/POST/PUT/DELETE） */
    @Column(name = "method", nullable = false)
    private String method;

    /** 请求 URL */
    @Column(name = "url", nullable = false)
    private String url;

    /** 客户端 IP */
    @Column(name = "ip")
    private String ip;

    /** User-Agent */
    @Column(name = "user_agent")
    private String userAgent;

    /** 响应状态码 */
    @Column(name = "status")
    private Integer status;

    /** 响应耗时（毫秒） */
    @Column(name = "duration")
    private Long duration;

    /** 操作用户（如已登录） */
    @Column(name = "username")
    private String username;
}