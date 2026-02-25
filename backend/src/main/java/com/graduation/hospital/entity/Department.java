package com.graduation.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 科室实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "department")
public class Department extends BaseEntity {

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "phone")
    private String phone;
}