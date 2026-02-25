package com.graduation.hospital.repository;

import com.graduation.hospital.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByAppointmentNo(String appointmentNo);

    boolean existsByAppointmentNo(String appointmentNo);

    // 使用方法命名查询
    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByPatientIdOrderByAppointmentTimeDesc(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByDepartmentId(Long departmentId);

    List<Appointment> findByStatus(String status);

    // 复杂查询：医生 + 时间范围
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentTime BETWEEN :startTime AND :endTime")
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, LocalDateTime startTime, LocalDateTime endTime);

    // 复杂查询：状态 + 时间范围
    @Query("SELECT a FROM Appointment a WHERE a.status = :status AND a.appointmentTime BETWEEN :startTime AND :endTime")
    List<Appointment> findByStatusAndAppointmentTimeBetween(String status, LocalDateTime startTime, LocalDateTime endTime);
}