package com.graduation.hospital.service;

import com.graduation.hospital.common.audit.AuditLogger;
import com.graduation.hospital.entity.Medicine;
import com.graduation.hospital.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final AuditLogger auditLogger;

    @Override
    @Transactional
    public Medicine createMedicine(Medicine medicine) {
        if (medicineRepository.existsByMedicineCode(medicine.getMedicineCode())) {
            throw new IllegalArgumentException("药品编码已存在");
        }
        if (medicine.getStock() == null) {
            medicine.setStock(0);
        }
        if (medicine.getMinStock() == null) {
            medicine.setMinStock(0);
        }
        Medicine saved = medicineRepository.save(medicine);
        log.info("创建药品成功: medicineCode={}, name={}", saved.getMedicineCode(), saved.getName());
        auditLogger.logCreate("药品管理", "添加药品: " + saved.getMedicineCode(), saved.getId());
        return saved;
    }

    @Override
    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("药品不存在"));
    }

    @Override
    public Medicine getMedicineByMedicineCode(String medicineCode) {
        return medicineRepository.findByMedicineCode(medicineCode)
                .orElseThrow(() -> new IllegalArgumentException("药品不存在"));
    }

    @Override
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    @Override
    public List<Medicine> searchMedicines(String keyword) {
        return medicineRepository.search(keyword);
    }

    @Override
    public List<Medicine> getMedicinesByCategory(String category) {
        return medicineRepository.findByCategory(category);
    }

    @Override
    public List<Medicine> getLowStockMedicines() {
        return medicineRepository.findLowStock();
    }

    @Override
    @Transactional
    public Medicine updateMedicine(Long id, Medicine medicine) {
        Medicine existing = getMedicineById(id);
        if (medicine.getName() != null) {
            existing.setName(medicine.getName());
        }
        if (medicine.getSpecification() != null) {
            existing.setSpecification(medicine.getSpecification());
        }
        if (medicine.getUnit() != null) {
            existing.setUnit(medicine.getUnit());
        }
        if (medicine.getPrice() != null) {
            existing.setPrice(medicine.getPrice());
        }
        if (medicine.getCategory() != null) {
            existing.setCategory(medicine.getCategory());
        }
        if (medicine.getMinStock() != null) {
            existing.setMinStock(medicine.getMinStock());
        }
        if (medicine.getManufacturer() != null) {
            existing.setManufacturer(medicine.getManufacturer());
        }
        if (medicine.getApprovalNumber() != null) {
            existing.setApprovalNumber(medicine.getApprovalNumber());
        }
        if (medicine.getStorageConditions() != null) {
            existing.setStorageConditions(medicine.getStorageConditions());
        }
        if (medicine.getDescription() != null) {
            existing.setDescription(medicine.getDescription());
        }
        Medicine updated = medicineRepository.save(existing);
        log.info("更新药品信息成功: id={}", id);
        auditLogger.logUpdate("药品管理", "更新药品信息", id);
        return updated;
    }

    @Override
    @Transactional
    public void deleteMedicine(Long id) {
        if (!medicineRepository.existsById(id)) {
            throw new IllegalArgumentException("药品不存在");
        }
        medicineRepository.deleteById(id);
        log.info("删除药品成功: id={}", id);
        auditLogger.logDelete("药品管理", "删除药品", id);
    }

    @Override
    @Transactional
    public Medicine updateStock(Long id, int quantity) {
        Medicine medicine = getMedicineById(id);
        int newStock = medicine.getStock() + quantity;
        if (newStock < 0) {
            throw new IllegalArgumentException("库存不足");
        }
        medicine.setStock(newStock);
        Medicine updated = medicineRepository.save(medicine);
        log.info("更新药品库存: id={}, quantity={}, newStock={}", id, quantity, newStock);
        auditLogger.logUpdate("药品管理", "更新库存: quantity=" + quantity, id);
        return updated;
    }

    @Override
    public boolean existsByMedicineCode(String medicineCode) {
        return medicineRepository.existsByMedicineCode(medicineCode);
    }
}