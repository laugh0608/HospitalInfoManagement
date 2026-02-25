package com.graduation.hospital.controller;

import com.graduation.hospital.common.Result;
import com.graduation.hospital.entity.Doctor;
import com.graduation.hospital.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医生信息管理控制器
 * 提供医生的 CRUD 操作接口
 */
@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    /**
     * 创建医生信息
     * 权限：仅管理员
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor saved = doctorService.createDoctor(doctor);
        return Result.success(saved);
    }

    /**
     * 根据 ID 获取医生信息
     */
    @GetMapping("/{id}")
    public Result<Doctor> getDoctorById(@PathVariable Long id) {
        return Result.success(doctorService.getDoctorById(id));
    }

    /**
     * 根据医生编号获取医生信息
     * 医生编号格式：D + 年月日 + 5位序号
     */
    @GetMapping("/no/{doctorNo}")
    public Result<Doctor> getDoctorByDoctorNo(@PathVariable String doctorNo) {
        return Result.success(doctorService.getDoctorByDoctorNo(doctorNo));
    }

    /**
     * 获取所有医生列表
     */
    @GetMapping
    public Result<List<Doctor>> getAllDoctors() {
        return Result.success(doctorService.getAllDoctors());
    }

    /**
     * 根据科室 ID 获取医生列表
     * @param departmentId 科室 ID
     */
    @GetMapping("/department/{departmentId}")
    public Result<List<Doctor>> getDoctorsByDepartmentId(@PathVariable Long departmentId) {
        return Result.success(doctorService.getDoctorsByDepartmentId(departmentId));
    }

    /**
     * 获取可接诊医生列表（状态为可用的医生）
     */
    @GetMapping("/available")
    public Result<List<Doctor>> getAvailableDoctors() {
        return Result.success(doctorService.getAvailableDoctors());
    }

    /**
     * 更新医生信息
     * 权限：仅管理员
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        return Result.success(doctorService.updateDoctor(id, doctor));
    }

    /**
     * 删除医生信息（逻辑删除）
     * 权限：仅管理员
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return Result.success();
    }

    /**
     * 设置医生可用状态（是否可接诊）
     * 权限：仅管理员
     * @param id 医生 ID
     * @param available 是否可用
     */
    @PutMapping("/{id}/availability")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> setAvailable(@PathVariable Long id, @RequestParam boolean available) {
        doctorService.setAvailable(id, available);
        return Result.success();
    }
}