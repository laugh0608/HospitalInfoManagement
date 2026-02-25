package com.graduation.hospital.controller;

import com.graduation.hospital.common.Result;
import com.graduation.hospital.entity.Medicine;
import com.graduation.hospital.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    public Result<Medicine> createMedicine(@RequestBody Medicine medicine) {
        Medicine saved = medicineService.createMedicine(medicine);
        return Result.success(saved);
    }

    @GetMapping("/{id}")
    public Result<Medicine> getMedicineById(@PathVariable Long id) {
        return Result.success(medicineService.getMedicineById(id));
    }

    @GetMapping("/code/{medicineCode}")
    public Result<Medicine> getMedicineByMedicineCode(@PathVariable String medicineCode) {
        return Result.success(medicineService.getMedicineByMedicineCode(medicineCode));
    }

    @GetMapping
    public Result<List<Medicine>> getAllMedicines() {
        return Result.success(medicineService.getAllMedicines());
    }

    @GetMapping("/search")
    public Result<List<Medicine>> searchMedicines(@RequestParam String keyword) {
        return Result.success(medicineService.searchMedicines(keyword));
    }

    @GetMapping("/category/{category}")
    public Result<List<Medicine>> getMedicinesByCategory(@PathVariable String category) {
        return Result.success(medicineService.getMedicinesByCategory(category));
    }

    @GetMapping("/low-stock")
    public Result<List<Medicine>> getLowStockMedicines() {
        return Result.success(medicineService.getLowStockMedicines());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    public Result<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicine) {
        return Result.success(medicineService.updateMedicine(id, medicine));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return Result.success();
    }

    @PutMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    public Result<Medicine> updateStock(@PathVariable Long id, @RequestParam int quantity) {
        return Result.success(medicineService.updateStock(id, quantity));
    }
}