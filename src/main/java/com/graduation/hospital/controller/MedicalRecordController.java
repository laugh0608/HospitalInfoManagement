package com.graduation.hospital.controller;

import com.graduation.hospital.common.Result;
import com.graduation.hospital.entity.MedicalRecord;
import com.graduation.hospital.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 病历记录管理控制器
 * 提供病历记录的 CRUD 操作接口
 */
@RestController
@RequestMapping("/api/v1/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    /**
     * 创建病历记录
     * 权限：管理员、医生
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public Result<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        MedicalRecord saved = medicalRecordService.createMedicalRecord(medicalRecord);
        return Result.success(saved);
    }

    /**
     * 根据 ID 获取病历记录
     */
    @GetMapping("/{id}")
    public Result<MedicalRecord> getMedicalRecordById(@PathVariable Long id) {
        return Result.success(medicalRecordService.getMedicalRecordById(id));
    }

    /**
     * 根据病历编号获取病历记录
     * 病历编号格式：MR + 年月日 + 5位序号
     */
    @GetMapping("/no/{recordNo}")
    public Result<MedicalRecord> getMedicalRecordByRecordNo(@PathVariable String recordNo) {
        return Result.success(medicalRecordService.getMedicalRecordByRecordNo(recordNo));
    }

    /**
     * 获取所有病历记录
     * 权限：管理员、医生、护士
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<List<MedicalRecord>> getAllMedicalRecords() {
        return Result.success(medicalRecordService.getAllMedicalRecords());
    }

    /**
     * 根据患者 ID 获取病历记录列表
     * @param patientId 患者 ID
     */
    @GetMapping("/patient/{patientId}")
    public Result<List<MedicalRecord>> getMedicalRecordsByPatientId(@PathVariable Long patientId) {
        return Result.success(medicalRecordService.getMedicalRecordsByPatientId(patientId));
    }

    /**
     * 根据医生 ID 获取病历记录列表
     * 权限：管理员、医生
     * @param doctorId 医生 ID
     */
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public Result<List<MedicalRecord>> getMedicalRecordsByDoctorId(@PathVariable Long doctorId) {
        return Result.success(medicalRecordService.getMedicalRecordsByDoctorId(doctorId));
    }

    /**
     * 根据时间范围获取病历记录
     * 权限：管理员、医生
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    @GetMapping("/time-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public Result<List<MedicalRecord>> getMedicalRecordsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(medicalRecordService.getMedicalRecordsByTimeRange(startTime, endTime));
    }

    /**
     * 更新病历记录
     * 权限：管理员、医生
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public Result<MedicalRecord> updateMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecord medicalRecord) {
        return Result.success(medicalRecordService.updateMedicalRecord(id, medicalRecord));
    }

    /**
     * 删除病历记录（逻辑删除）
     * 权限：仅管理员
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return Result.success();
    }
}