package com.graduation.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 预约挂号实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "appointment")
public class Appointment extends BaseEntity {

    @Column(name = "appointment_no", unique = true, nullable = false)
    private String appointmentNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "appointment_time")
    private LocalDateTime appointmentTime;

    @Column(name = "status")
    private String status;

    @Column(name = "reason")
    private String reason;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "cancel_time")
    private LocalDateTime cancelTime;

    @Column(name = "cancel_reason")
    private String cancelReason;
}