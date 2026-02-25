package com.graduation.hospital.repository;

import com.graduation.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 病人数据访问仓储
 * 继承 JpaRepository 提供基本的 CRUD 操作
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * 根据病人编号查询
     */
    Optional<Patient> findByPatientNo(String patientNo);

    /**
     * 检查病人编号是否存在
     */
    boolean existsByPatientNo(String patientNo);

    /**
     * 检查身份证号是否存在
     */
    boolean existsByIdCard(String idCard);

    /**
     * 检查手机号是否存在
     */
    boolean existsByPhone(String phone);

    /**
     * 根据姓名模糊查询
     */
    List<Patient> findByNameContaining(String name);

    /**
     * 关键字搜索（姓名、病人编号、手机号）
     * @param keyword 关键字
     */
    @Query("SELECT p FROM Patient p WHERE p.name LIKE %:keyword% OR p.patientNo LIKE %:keyword% OR p.phone LIKE %:keyword%")
    List<Patient> search(String keyword);
}