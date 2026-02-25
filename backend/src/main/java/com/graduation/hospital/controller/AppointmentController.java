package com.graduation.hospital.controller;

import com.graduation.hospital.common.Result;
import com.graduation.hospital.entity.Appointment;
import com.graduation.hospital.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public Result<Appointment> createAppointment(@RequestBody Appointment appointment) {
        Appointment saved = appointmentService.createAppointment(appointment);
        return Result.success(saved);
    }

    @GetMapping("/{id}")
    public Result<Appointment> getAppointmentById(@PathVariable Long id) {
        return Result.success(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/no/{appointmentNo}")
    public Result<Appointment> getAppointmentByAppointmentNo(@PathVariable String appointmentNo) {
        return Result.success(appointmentService.getAppointmentByAppointmentNo(appointmentNo));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<List<Appointment>> getAllAppointments() {
        return Result.success(appointmentService.getAllAppointments());
    }

    @GetMapping("/patient/{patientId}")
    public Result<List<Appointment>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        return Result.success(appointmentService.getAppointmentsByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<List<Appointment>> getAppointmentsByDoctorId(@PathVariable Long doctorId) {
        return Result.success(appointmentService.getAppointmentsByDoctorId(doctorId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<List<Appointment>> getAppointmentsByStatus(@PathVariable String status) {
        return Result.success(appointmentService.getAppointmentsByStatus(status));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<Appointment> updateAppointmentStatus(@PathVariable Long id, @RequestParam String status) {
        return Result.success(appointmentService.updateAppointmentStatus(id, status));
    }

    @PostMapping("/{id}/cancel")
    public Result<Appointment> cancelAppointment(@PathVariable Long id, @RequestParam String reason) {
        return Result.success(appointmentService.cancelAppointment(id, reason));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return Result.success();
    }
}