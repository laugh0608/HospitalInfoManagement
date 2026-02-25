package com.graduation.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 药品实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "medicine")
public class Medicine extends BaseEntity {

    @Column(name = "medicine_code", unique = true, nullable = false)
    private String medicineCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "specification")
    private String specification;

    @Column(name = "unit")
    private String unit;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "category")
    private String category;

    @Column(name = "stock")
    private Integer stock = 0;

    @Column(name = "min_stock")
    private Integer minStock = 0;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "approval_number")
    private String approvalNumber;

    @Column(name = "storage_conditions")
    private String storageConditions;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}