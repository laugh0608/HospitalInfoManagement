package com.graduation.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 病历记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "medical_record")
public class MedicalRecord extends BaseEntity {

    @Column(name = "record_no", unique = true, nullable = false)
    private String recordNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "visit_time")
    private LocalDateTime visitTime;

    @Column(name = "visit_type")
    private String visitType;

    @Column(name = "chief_complaint", columnDefinition = "TEXT")
    private String chiefComplaint;

    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "treatment_plan", columnDefinition = "TEXT")
    private String treatmentPlan;

    @Column(name = "prescription", columnDefinition = "TEXT")
    private String prescription;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "follow_up_date")
    private LocalDateTime followUpDate;
}