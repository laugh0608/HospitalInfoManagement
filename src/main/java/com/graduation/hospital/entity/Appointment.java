package com.graduation.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 预约挂号实体
 * 存储患者的预约挂号信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "appointment")
public class Appointment extends BaseEntity {

    /**
     * 预约编号
     * 格式：A + 年月日 + 5位序号，如 A2026022500001
     */
    @Column(name = "appointment_no", unique = true, nullable = false)
    private String appointmentNo;

    /**
     * 预约患者
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /**
     * 预约医生
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    /**
     * 预约科室
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    /**
     * 预约就诊时间
     */
    @Column(name = "appointment_time")
    private LocalDateTime appointmentTime;

    /**
     * 预约状态
     * PENDING（待确认）、CONFIRMED（已确认）、COMPLETED（已完成）、CANCELLED（已取消）
     */
    @Column(name = "status")
    private String status;

    /**
     * 预约原因/症状描述
     */
    @Column(name = "reason")
    private String reason;

    /**
     * 备注信息
     */
    @Column(name = "remarks")
    private String remarks;

    /**
     * 取消时间
     */
    @Column(name = "cancel_time")
    private LocalDateTime cancelTime;

    /**
     * 取消原因
     */
    @Column(name = "cancel_reason")
    private String cancelReason;
}