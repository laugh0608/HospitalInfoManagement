package com.graduation.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 病历记录实体
 * 存储患者就诊的病历信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "medical_record")
public class MedicalRecord extends BaseEntity {

    /**
     * 病历编号
     * 格式：MR + 年月日 + 5位序号，如 MR2026022500001
     */
    @Column(name = "record_no", unique = true, nullable = false)
    private String recordNo;

    /**
     * 就诊患者
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /**
     * 接诊医生
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    /**
     * 就诊时间
     */
    @Column(name = "visit_time")
    private LocalDateTime visitTime;

    /**
     * 就诊类型（门诊、急诊、复诊）
     */
    @Column(name = "visit_type")
    private String visitType;

    /**
     * 主诉（患者自述症状）
     */
    @Column(name = "chief_complaint", columnDefinition = "TEXT")
    private String chiefComplaint;

    /**
     * 诊断结果
     */
    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    /**
     * 治疗方案
     */
    @Column(name = "treatment_plan", columnDefinition = "TEXT")
    private String treatmentPlan;

    /**
     * 处方（开具的药品）
     */
    @Column(name = "prescription", columnDefinition = "TEXT")
    private String prescription;

    /**
     * 备注
     */
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    /**
     * 复诊日期
     */
    @Column(name = "follow_up_date")
    private LocalDateTime followUpDate;
}