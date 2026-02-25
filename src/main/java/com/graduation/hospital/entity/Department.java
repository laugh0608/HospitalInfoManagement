package com.graduation.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 科室实体
 * 存储医院科室基本信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "department")
public class Department extends BaseEntity {

    /**
     * 科室名称（如：内科、外科、儿科）
     */
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    /**
     * 科室编码（如：InternalMedicine、Surgery）
     */
    @Column(name = "code", unique = true, nullable = false)
    private String code;

    /**
     * 科室描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 科室位置（如：门诊楼 2 楼）
     */
    @Column(name = "location")
    private String location;

    /**
     * 科室联系电话
     */
    @Column(name = "phone")
    private String phone;
}