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

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<Patient> createPatient(@RequestBody Patient patient) {
        Patient saved = patientService.createPatient(patient);
        return Result.success(saved);
    }

    @GetMapping("/{id}")
    public Result<Patient> getPatientById(@PathVariable Long id) {
        return Result.success(patientService.getPatientById(id));
    }

    @GetMapping("/no/{patientNo}")
    public Result<Patient> getPatientByPatientNo(@PathVariable String patientNo) {
        return Result.success(patientService.getPatientByPatientNo(patientNo));
    }

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

    @GetMapping("/search")
    public Result<List<Patient>> searchPatients(@RequestParam String keyword) {
        return Result.success(patientService.searchPatients(keyword));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        return Result.success(patientService.updatePatient(id, patient));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return Result.success();
    }
}