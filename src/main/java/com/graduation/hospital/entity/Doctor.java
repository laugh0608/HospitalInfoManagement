package com.graduation.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 医生实体
 * 存储医生基本信息和专业资质
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "doctor")
public class Doctor extends BaseEntity {

    /**
     * 医生编号
     * 格式：D + 年月日 + 5位序号，如 D2026022500001
     */
    @Column(name = "doctor_no", unique = true, nullable = false)
    private String doctorNo;

    /**
     * 姓名
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 职称（主任医师、副主任医师、主治医师、住院医师）
     */
    @Column(name = "title")
    private String title;

    /**
     * 专业特长（多个用逗号分隔）
     */
    @Column(name = "specialty")
    private String specialty;

    /**
     * 工作年限
     */
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    /**
     * 所属科室
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    /**
     * 关联的系统用户（用于医生登录）
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SysUser user;

    /**
     * 是否可接诊（true=可接诊，false=不可接诊）
     */
    @Column(name = "is_available")
    private Boolean isAvailable = true;

    /**
     * 照片URL
     */
    @Column(name = "photo")
    private String photo;

    /**
     * 个人简介
     */
    @Column(name = "introduction")
    private String introduction;
}