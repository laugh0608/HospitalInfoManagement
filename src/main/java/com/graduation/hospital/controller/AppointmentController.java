package com.graduation.hospital.controller;

import com.graduation.hospital.common.Result;
import com.graduation.hospital.entity.Appointment;
import com.graduation.hospital.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预约挂号管理控制器
 * 提供预约挂号的 CRUD 操作接口
 */
@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * 创建预约挂号
     * 公开接口，患者可以自主预约
     */
    @PostMapping
    public Result<Appointment> createAppointment(@RequestBody Appointment appointment) {
        Appointment saved = appointmentService.createAppointment(appointment);
        return Result.success(saved);
    }

    /**
     * 根据 ID 获取预约信息
     */
    @GetMapping("/{id}")
    public Result<Appointment> getAppointmentById(@PathVariable Long id) {
        return Result.success(appointmentService.getAppointmentById(id));
    }

    /**
     * 根据预约编号获取预约信息
     * 预约编号格式：A + 年月日 + 5位序号
     */
    @GetMapping("/no/{appointmentNo}")
    public Result<Appointment> getAppointmentByAppointmentNo(@PathVariable String appointmentNo) {
        return Result.success(appointmentService.getAppointmentByAppointmentNo(appointmentNo));
    }

    /**
     * 获取所有预约列表
     * 权限：管理员、医生、护士
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<List<Appointment>> getAllAppointments() {
        return Result.success(appointmentService.getAllAppointments());
    }

    /**
     * 根据患者 ID 获取预约列表
     * @param patientId 患者 ID
     */
    @GetMapping("/patient/{patientId}")
    public Result<List<Appointment>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        return Result.success(appointmentService.getAppointmentsByPatientId(patientId));
    }

    /**
     * 根据医生 ID 获取预约列表
     * 权限：管理员、医生、护士
     * @param doctorId 医生 ID
     */
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<List<Appointment>> getAppointmentsByDoctorId(@PathVariable Long doctorId) {
        return Result.success(appointmentService.getAppointmentsByDoctorId(doctorId));
    }

    /**
     * 根据状态获取预约列表
     * 权限：管理员、医生、护士
     * @param status 预约状态（PENDING/CONFIRMED/COMPLETED/CANCELLED）
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<List<Appointment>> getAppointmentsByStatus(@PathVariable String status) {
        return Result.success(appointmentService.getAppointmentsByStatus(status));
    }

    /**
     * 更新预约状态
     * 权限：管理员、医生、护士
     * @param id 预约 ID
     * @param status 新状态
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public Result<Appointment> updateAppointmentStatus(@PathVariable Long id, @RequestParam String status) {
        return Result.success(appointmentService.updateAppointmentStatus(id, status));
    }

    /**
     * 取消预约
     * 患者可以取消自己的预约
     * @param id 预约 ID
     * @param reason 取消原因
     */
    @PostMapping("/{id}/cancel")
    public Result<Appointment> cancelAppointment(@PathVariable Long id, @RequestParam String reason) {
        return Result.success(appointmentService.cancelAppointment(id, reason));
    }

    /**
     * 删除预约（逻辑删除）
     * 权限：仅管理员
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return Result.success();
    }
}