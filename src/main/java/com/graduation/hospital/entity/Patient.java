package com.graduation.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 病人实体
 * 存储病人基本信息档案
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "patient")
public class Patient extends BaseEntity {

    /**
     * 病人编号
     * 格式：P + 年月日 + 5位序号，如 P2026022500001
     */
    @Column(name = "patient_no", unique = true, nullable = false)
    private String patientNo;

    /**
     * 姓名
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 性别（男/女）
     */
    @Column(name = "gender")
    private String gender;

    /**
     * 出生日期
     */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * 身份证号（唯一）
     */
    @Column(name = "id_card", unique = true)
    private String idCard;

    /**
     * 联系电话
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 家庭住址
     */
    @Column(name = "address")
    private String address;

    /**
     * 医保类型（城镇职工/城乡居民/自费等）
     */
    @Column(name = "insurance_type")
    private String insuranceType;

    /**
     * 过敏史（多个人用逗号分隔）
     */
    @Column(name = "allergies", columnDefinition = "TEXT")
    private String allergies;

    /**
     * 既往病史
     */
    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    /**
     * 紧急联系人
     */
    @Column(name = "emergency_contact")
    private String emergencyContact;

    /**
     * 紧急联系电话
     */
    @Column(name = "emergency_phone")
    private String emergencyPhone;

    /**
     * 关联的系统用户（可选，用于患者自助登录）
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SysUser user;
}