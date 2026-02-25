package com.graduation.hospital.service;

import com.graduation.hospital.entity.Appointment;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约挂号服务接口
 * 定义预约挂号的业务操作
 */
public interface AppointmentService {

    /**
     * 创建预约挂号
     * 自动生成预约编号：A + 年月日 + 5位序号
     */
    Appointment createAppointment(Appointment appointment);

    /**
     * 根据 ID 获取预约信息
     */
    Appointment getAppointmentById(Long id);

    /**
     * 根据预约编号获取预约信息
     * @param appointmentNo 预约编号
     */
    Appointment getAppointmentByAppointmentNo(String appointmentNo);

    /**
     * 获取所有预约列表
     */
    List<Appointment> getAllAppointments();

    /**
     * 根据患者 ID 获取预约列表
     * @param patientId 患者 ID
     */
    List<Appointment> getAppointmentsByPatientId(Long patientId);

    /**
     * 根据医生 ID 获取预约列表
     * @param doctorId 医生 ID
     */
    List<Appointment> getAppointmentsByDoctorId(Long doctorId);

    /**
     * 根据状态获取预约列表
     * @param status 状态（PENDING/CONFIRMED/COMPLETED/CANCELLED）
     */
    List<Appointment> getAppointmentsByStatus(String status);

    /**
     * 更新预约状态
     */
    Appointment updateAppointmentStatus(Long id, String status);

    /**
     * 取消预约
     * @param id 预约 ID
     * @param reason 取消原因
     */
    Appointment cancelAppointment(Long id, String reason);

    /**
     * 删除预约（逻辑删除）
     */
    void deleteAppointment(Long id);
}