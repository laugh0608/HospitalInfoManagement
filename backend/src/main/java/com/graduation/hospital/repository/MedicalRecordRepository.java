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

    // 使用方法命名查询，Spring Data JPA 自动解析
    List<MedicalRecord> findByPatientId(Long patientId);

    List<MedicalRecord> findByPatientIdOrderByVisitTimeDesc(Long patientId);

    List<MedicalRecord> findByDoctorId(Long doctorId);

    List<MedicalRecord> findByVisitTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    // 复杂查询：医生 + 时间范围，需要手动 JPQL
    @Query("SELECT m FROM MedicalRecord m WHERE m.doctor.id = :doctorId AND m.visitTime BETWEEN :startTime AND :endTime")
    List<MedicalRecord> findByDoctorIdAndVisitTimeBetween(Long doctorId, LocalDateTime startTime, LocalDateTime endTime);
}