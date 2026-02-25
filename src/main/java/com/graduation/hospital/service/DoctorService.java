package com.graduation.hospital.service;

import com.graduation.hospital.entity.Doctor;

import java.util.List;

/**
 * 医生服务接口
 * 定义医生信息管理的业务操作
 */
public interface DoctorService {

    /**
     * 创建医生信息
     * 自动生成医生编号：D + 年月日 + 5位序号
     */
    Doctor createDoctor(Doctor doctor);

    /**
     * 根据 ID 获取医生信息
     */
    Doctor getDoctorById(Long id);

    /**
     * 根据医生编号获取医生信息
     * @param doctorNo 医生编号
     */
    Doctor getDoctorByDoctorNo(String doctorNo);

    /**
     * 获取所有医生列表
     */
    List<Doctor> getAllDoctors();

    /**
     * 根据科室 ID 获取医生列表
     * @param departmentId 科室 ID
     */
    List<Doctor> getDoctorsByDepartmentId(Long departmentId);

    /**
     * 获取可接诊医生列表（状态为可用）
     */
    List<Doctor> getAvailableDoctors();

    /**
     * 更新医生信息
     */
    Doctor updateDoctor(Long id, Doctor doctor);

    /**
     * 删除医生信息（逻辑删除）
     */
    void deleteDoctor(Long id);

    /**
     * 设置医生可用状态
     * @param id 医生 ID
     * @param available 是否可用
     */
    void setAvailable(Long id, boolean available);
}