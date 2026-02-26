package com.graduation.hospital.entity.log;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审计日志数据对象
 * 存储用户操作审计记录，由 DbLoggingService 写入独立日志库
 */
@Data
public class AuditLog {

    private Long id;

    /** 日志时间 */
    private LocalDateTime logTime;

    /** 操作用户名 */
    private String username;

    /** 用户 ID */
    private Long userId;

    /** 操作类型 */
    private String actionType;

    /** 操作模块 */
    private String module;

    /** 操作描述 */
    private String description;

    /** 目标对象 */
    private String target;

    /** 请求 IP */
    private String ip;

    /** 请求方法 */
    private String method;

    /** 请求 URL */
    private String url;

    /** 是否成功 */
    private Boolean success;

    /** 错误信息 */
    private String errorMessage;
}
