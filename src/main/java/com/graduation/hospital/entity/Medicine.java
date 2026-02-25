package com.graduation.hospital.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 药品实体
 * 存储药品基本信息及库存
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "medicine")
public class Medicine extends BaseEntity {

    /**
     * 药品编码（如：M20260200001）
     */
    @Column(name = "medicine_code", unique = true, nullable = false)
    private String medicineCode;

    /**
     * 药品名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 规格（如：10mg*10片/盒）
     */
    @Column(name = "specification")
    private String specification;

    /**
     * 单位（盒、瓶、支）
     */
    @Column(name = "unit")
    private String unit;

    /**
     * 单价（元）
     */
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * 分类（西药、中药、保健品）
     */
    @Column(name = "category")
    private String category;

    /**
     * 当前库存数量
     */
    @Column(name = "stock")
    private Integer stock = 0;

    /**
     * 库存预警数量（低于此值提醒补货）
     */
    @Column(name = "min_stock")
    private Integer minStock = 0;

    /**
     * 生产厂家
     */
    @Column(name = "manufacturer")
    private String manufacturer;

    /**
     * 批准文号
     */
    @Column(name = "approval_number")
    private String approvalNumber;

    /**
     * 储存条件（常温、冷藏、阴凉保存）
     */
    @Column(name = "storage_conditions")
    private String storageConditions;

    /**
     * 药品说明/功效
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}