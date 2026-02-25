package com.graduation.hospital.repository;

import com.graduation.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPatientNo(String patientNo);

    boolean existsByPatientNo(String patientNo);

    boolean existsByIdCard(String idCard);

    boolean existsByPhone(String phone);

    List<Patient> findByNameContaining(String name);

    @Query("SELECT p FROM Patient p WHERE p.name LIKE %:keyword% OR p.patientNo LIKE %:keyword% OR p.phone LIKE %:keyword%")
    List<Patient> search(String keyword);
}