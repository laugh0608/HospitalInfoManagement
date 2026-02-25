package com.graduation.hospital.controller;

import com.graduation.hospital.common.PageResult;
import com.graduation.hospital.common.Result;
import com.graduation.hospital.entity.Patient;
import com.graduation.hospital.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 病人信息管理控制器
 * 提供病人档案的 CRUD 操作接口
 */
@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    /**
     * 创建病人档案
     * 权限：管理员、医生、护士
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<Patient> createPatient(@RequestBody Patient patient) {
        Patient saved = patientService.createPatient(patient);
        return Result.success(saved);
    }

    /**
     * 根据 ID 获取病人信息
     */
    @GetMapping("/{id}")
    public Result<Patient> getPatientById(@PathVariable Long id) {
        return Result.success(patientService.getPatientById(id));
    }

    /**
     * 根据病人编号获取病人信息
     * 病人编号格式：P + 年月日 + 5位序号
     */
    @GetMapping("/no/{patientNo}")
    public Result<Patient> getPatientByPatientNo(@PathVariable String patientNo) {
        return Result.success(patientService.getPatientByPatientNo(patientNo));
    }

    /**
     * 分页获取所有病人列表
     * @param page 页码（从 0 开始）
     * @param size 每页数量
     */
    @GetMapping
    public Result<PageResult<Patient>> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Patient> patients = patientService.getAllPatients();
        int total = patients.size();
        int from = page * size;
        int to = Math.min(from + size, total);
        List<Patient> paged = from < total ? patients.subList(from, to) : List.of();
        return Result.success(PageResult.of(paged, total, page, size));
    }

    /**
     * 搜索病人（按姓名、手机号、身份证号）
     * @param keyword 关键字
     */
    @GetMapping("/search")
    public Result<List<Patient>> searchPatients(@RequestParam String keyword) {
        return Result.success(patientService.searchPatients(keyword));
    }

    /**
     * 更新病人信息
     * 权限：管理员、医生、护士
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        return Result.success(patientService.updatePatient(id, patient));
    }

    /**
     * 删除病人档案（逻辑删除）
     * 权限：仅管理员
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return Result.success();
    }
}