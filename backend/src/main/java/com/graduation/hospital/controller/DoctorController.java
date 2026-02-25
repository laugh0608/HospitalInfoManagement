package com.graduation.hospital.controller;

import com.graduation.hospital.common.Result;
import com.graduation.hospital.entity.Doctor;
import com.graduation.hospital.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor saved = doctorService.createDoctor(doctor);
        return Result.success(saved);
    }

    @GetMapping("/{id}")
    public Result<Doctor> getDoctorById(@PathVariable Long id) {
        return Result.success(doctorService.getDoctorById(id));
    }

    @GetMapping("/no/{doctorNo}")
    public Result<Doctor> getDoctorByDoctorNo(@PathVariable String doctorNo) {
        return Result.success(doctorService.getDoctorByDoctorNo(doctorNo));
    }

    @GetMapping
    public Result<List<Doctor>> getAllDoctors() {
        return Result.success(doctorService.getAllDoctors());
    }

    @GetMapping("/department/{departmentId}")
    public Result<List<Doctor>> getDoctorsByDepartmentId(@PathVariable Long departmentId) {
        return Result.success(doctorService.getDoctorsByDepartmentId(departmentId));
    }

    @GetMapping("/available")
    public Result<List<Doctor>> getAvailableDoctors() {
        return Result.success(doctorService.getAvailableDoctors());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        return Result.success(doctorService.updateDoctor(id, doctor));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return Result.success();
    }

    @PutMapping("/{id}/availability")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> setAvailable(@PathVariable Long id, @RequestParam boolean available) {
        doctorService.setAvailable(id, available);
        return Result.success();
    }
}