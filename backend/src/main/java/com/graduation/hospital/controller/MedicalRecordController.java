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

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public Result<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        MedicalRecord saved = medicalRecordService.createMedicalRecord(medicalRecord);
        return Result.success(saved);
    }

    @GetMapping("/{id}")
    public Result<MedicalRecord> getMedicalRecordById(@PathVariable Long id) {
        return Result.success(medicalRecordService.getMedicalRecordById(id));
    }

    @GetMapping("/no/{recordNo}")
    public Result<MedicalRecord> getMedicalRecordByRecordNo(@PathVariable String recordNo) {
        return Result.success(medicalRecordService.getMedicalRecordByRecordNo(recordNo));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<List<MedicalRecord>> getAllMedicalRecords() {
        return Result.success(medicalRecordService.getAllMedicalRecords());
    }

    @GetMapping("/patient/{patientId}")
    public Result<List<MedicalRecord>> getMedicalRecordsByPatientId(@PathVariable Long patientId) {
        return Result.success(medicalRecordService.getMedicalRecordsByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public Result<List<MedicalRecord>> getMedicalRecordsByDoctorId(@PathVariable Long doctorId) {
        return Result.success(medicalRecordService.getMedicalRecordsByDoctorId(doctorId));
    }

    @GetMapping("/time-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public Result<List<MedicalRecord>> getMedicalRecordsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(medicalRecordService.getMedicalRecordsByTimeRange(startTime, endTime));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public Result<MedicalRecord> updateMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecord medicalRecord) {
        return Result.success(medicalRecordService.updateMedicalRecord(id, medicalRecord));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return Result.success();
    }
}