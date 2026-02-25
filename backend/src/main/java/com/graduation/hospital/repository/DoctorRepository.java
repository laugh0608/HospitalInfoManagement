package com.graduation.hospital.repository;

import com.graduation.hospital.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByDoctorNo(String doctorNo);

    boolean existsByDoctorNo(String doctorNo);

    List<Doctor> findByDepartmentId(Long departmentId);

    List<Doctor> findByIsAvailableTrue();

    @Query("SELECT d FROM Doctor d WHERE d.isAvailable = true AND d.department.id = :departmentId")
    List<Doctor> findAvailableByDepartmentId(Long departmentId);
}