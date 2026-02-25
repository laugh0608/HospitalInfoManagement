package com.graduation.hospital.repository;

import com.graduation.hospital.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    Optional<MedicalRecord> findByRecordNo(String recordNo);

    boolean existsByRecordNo(String recordNo);

    List<MedicalRecord> findByPatientId(Long patientId);

    List<MedicalRecord> findByDoctorId(Long doctorId);

    @Query("SELECT m FROM MedicalRecord m WHERE m.patient.id = :patientId ORDER BY m.visitTime DESC")
    List<MedicalRecord> findByPatientIdOrderByVisitTimeDesc(Long patientId);

    @Query("SELECT m FROM MedicalRecord m WHERE m.visitTime BETWEEN :startTime AND :endTime")
    List<MedicalRecord> findByVisitTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT m FROM MedicalRecord m WHERE m.doctor.id = :doctorId AND m.visitTime BETWEEN :startTime AND :endTime")
    List<MedicalRecord> findByDoctorIdAndVisitTimeBetween(Long doctorId, LocalDateTime startTime, LocalDateTime endTime);
}