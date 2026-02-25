package com.graduation.hospital.service;

import com.graduation.hospital.entity.Medicine;

import java.util.List;

public interface MedicineService {

    Medicine createMedicine(Medicine medicine);

    Medicine getMedicineById(Long id);

    Medicine getMedicineByMedicineCode(String medicineCode);

    List<Medicine> getAllMedicines();

    List<Medicine> searchMedicines(String keyword);

    List<Medicine> getMedicinesByCategory(String category);

    List<Medicine> getLowStockMedicines();

    Medicine updateMedicine(Long id, Medicine medicine);

    void deleteMedicine(Long id);

    Medicine updateStock(Long id, int quantity);

    boolean existsByMedicineCode(String medicineCode);
}