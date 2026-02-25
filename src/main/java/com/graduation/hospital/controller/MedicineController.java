package com.graduation.hospital.controller;

import com.graduation.hospital.common.Result;
import com.graduation.hospital.entity.Medicine;
import com.graduation.hospital.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 药品管理控制器
 * 提供药品的 CRUD 操作接口
 */
@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    /**
     * 添加药品
     * 权限：管理员、护士
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    public Result<Medicine> createMedicine(@RequestBody Medicine medicine) {
        Medicine saved = medicineService.createMedicine(medicine);
        return Result.success(saved);
    }

    /**
     * 根据 ID 获取药品信息
     */
    @GetMapping("/{id}")
    public Result<Medicine> getMedicineById(@PathVariable Long id) {
        return Result.success(medicineService.getMedicineById(id));
    }

    /**
     * 根据药品编码获取药品信息
     * @param medicineCode 药品编码
     */
    @GetMapping("/code/{medicineCode}")
    public Result<Medicine> getMedicineByMedicineCode(@PathVariable String medicineCode) {
        return Result.success(medicineService.getMedicineByMedicineCode(medicineCode));
    }

    /**
     * 获取所有药品列表
     */
    @GetMapping
    public Result<List<Medicine>> getAllMedicines() {
        return Result.success(medicineService.getAllMedicines());
    }

    /**
     * 搜索药品（按名称、编码、规格）
     * @param keyword 关键字
     */
    @GetMapping("/search")
    public Result<List<Medicine>> searchMedicines(@RequestParam String keyword) {
        return Result.success(medicineService.searchMedicines(keyword));
    }

    /**
     * 根据分类获取药品列表
     * @param category 药品分类（如：西药、中药）
     */
    @GetMapping("/category/{category}")
    public Result<List<Medicine>> getMedicinesByCategory(@PathVariable String category) {
        return Result.success(medicineService.getMedicinesByCategory(category));
    }

    /**
     * 获取库存不足的药品列表（低于库存预警值）
     */
    @GetMapping("/low-stock")
    public Result<List<Medicine>> getLowStockMedicines() {
        return Result.success(medicineService.getLowStockMedicines());
    }

    /**
     * 更新药品信息
     * 权限：管理员、护士
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    public Result<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicine) {
        return Result.success(medicineService.updateMedicine(id, medicine));
    }

    /**
     * 删除药品（逻辑删除）
     * 权限：仅管理员
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return Result.success();
    }

    /**
     * 更新药品库存
     * 权限：管理员、护士
     * @param id 药品 ID
     * @param quantity 库存变更数量（正数增加，负数减少）
     */
    @PutMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    public Result<Medicine> updateStock(@PathVariable Long id, @RequestParam int quantity) {
        return Result.success(medicineService.updateStock(id, quantity));
    }
}