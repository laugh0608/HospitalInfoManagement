package com.graduation.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 医生实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "doctor")
public class Doctor extends BaseEntity {

    @Column(name = "doctor_no", unique = true, nullable = false)
    private String doctorNo;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "specialty")
    private String specialty;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SysUser user;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "photo")
    private String photo;

    @Column(name = "introduction")
    private String introduction;
}