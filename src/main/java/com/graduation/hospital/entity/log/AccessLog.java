package com.graduation.hospital.entity.log;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 访问日志数据对象
 * 存储 HTTP 请求访问记录，由 DbLoggingService 写入独立日志库
 */
@Data
public class AccessLog {

    private Long id;

    /** 请求时间 */
    private LocalDateTime requestTime;

    /** 请求 ID（链路追踪） */
    private String requestId;

    /** 请求方法（GET/POST/PUT/DELETE） */
    private String method;

    /** 请求 URL */
    private String url;

    /** 客户端 IP */
    private String ip;

    /** User-Agent */
    private String userAgent;

    /** 响应状态码 */
    private Integer status;

    /** 响应耗时（毫秒） */
    private Long duration;

    /** 操作用户（如已登录） */
    private String username;
}
