package com.graduation.hospital.repository;

import com.graduation.hospital.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    Optional<Medicine> findByMedicineCode(String medicineCode);

    boolean existsByMedicineCode(String medicineCode);

    List<Medicine> findByNameContaining(String name);

    List<Medicine> findByCategory(String category);

    @Query("SELECT m FROM Medicine m WHERE m.stock <= m.minStock")
    List<Medicine> findLowStock();

    @Query("SELECT m FROM Medicine m WHERE m.name LIKE %:keyword% OR m.medicineCode LIKE %:keyword%")
    List<Medicine> search(String keyword);
}