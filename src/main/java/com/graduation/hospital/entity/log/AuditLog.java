package com.graduation.hospital.entity.log;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审计日志实体
 * 存储用户操作审计记录（分表：audit_YYYYMM 或 audit_YYYYMMDD）
 */
@Data
@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 日志时间 */
    @Column(name = "log_time", nullable = false)
    private LocalDateTime logTime;

    /** 操作用户名 */
    @Column(name = "username")
    private String username;

    /** 用户 ID */
    @Column(name = "user_id")
    private Long userId;

    /** 操作类型 */
    @Column(name = "action_type", nullable = false)
    private String actionType;

    /** 操作模块 */
    @Column(name = "module")
    private String module;

    /** 操作描述 */
    @Column(name = "description")
    private String description;

    /** 目标对象 */
    @Column(name = "target")
    private String target;

    /** 请求 IP */
    @Column(name = "ip")
    private String ip;

    /** 请求方法 */
    @Column(name = "method")
    private String method;

    /** 请求 URL */
    @Column(name = "url")
    private String url;

    /** 是否成功 */
    @Column(name = "success")
    private Boolean success;

    /** 错误信息 */
    @Column(name = "error_message")
    private String errorMessage;
}